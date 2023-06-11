package ir.iau.exchange.service.impl;

import ir.iau.exchange.config.ApplicationStartup;
import ir.iau.exchange.dto.ExchangeData;
import ir.iau.exchange.dto.requestes.SubmitOrderRequestDto;
import ir.iau.exchange.entity.Asset;
import ir.iau.exchange.entity.Order;
import ir.iau.exchange.entity.User;
import ir.iau.exchange.entity.UserAsset;
import ir.iau.exchange.exception.BadRequestRuntimeException;
import ir.iau.exchange.repository.OrderRepository;
import ir.iau.exchange.repository.UserAssetRepository;
import ir.iau.exchange.service.AssetService;
import ir.iau.exchange.service.OrderService;
import ir.iau.exchange.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@ConditionalOnBean(ApplicationStartup.class)
public class OrderServiceImpl implements OrderService, ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AssetService assetService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserAssetRepository userAssetRepository;

    @Autowired
    private ApplicationContext applicationContext;

    private final Map<String, Map<String, List<Order>>> exchangeMap = new ConcurrentHashMap<>();


    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<Asset> assets = assetService.findAll();
        for (Asset source : assets) {
            for (Asset dest : assets) {
                if (source.getCode().equals(dest.getCode()))
                    continue;
                List<Order> orders = orderRepository.findByIsClosedFalseAndSource_codeAndDestination_codeOrderBySubmitDateDesc(source.getCode(), dest.getCode());
                Map<String, List<Order>> map = new ConcurrentHashMap<>();
                map.put(dest.getCode(), orders);
                exchangeMap.put(source.getCode(), map);
            }
        }
    }

    @Override
    @Transactional
    public synchronized Order submitForCurrentUser(SubmitOrderRequestDto requestDto) {
        // get source asset
        Asset sourceAsset = assetService.findByCode(requestDto.getSourceCode());
        if (sourceAsset == null) {
            throw new BadRequestRuntimeException("source asset not found");
        }

        // get dest asset
        Asset destAsset = assetService.findByCode(requestDto.getDestinationCode());
        if (destAsset == null) {
            throw new BadRequestRuntimeException("destination asset not found");
        }

        // get current user
        User currentUser = userService.getCurrentUser();

        // get current user asset
        UserAsset currentUserAsset = userAssetRepository.findByAssetAndUser(sourceAsset, currentUser);
        if (currentUserAsset.getBalance().compareTo(requestDto.getSourceAmount()) < 0) {
            throw new BadRequestRuntimeException("not enough balance");
        }

        // change current user asset
        currentUserAsset.setBalance(currentUserAsset.getBalance().subtract(requestDto.getSourceAmount()));
        userAssetRepository.save(currentUserAsset);

        // create order
        Order order = new Order();
        order.setRequester(currentUser);
        order.setSource(sourceAsset);
        order.setDestination(destAsset);
        order.setSourceAmount(requestDto.getSourceAmount());
        order.setDestinationAmount(requestDto.getDestinationAmount());
        order.setTrackingCode(UUID.randomUUID());
        order = orderRepository.save(order);

        // put it in in-memory exchange map
        exchangeMap.get(sourceAsset.getCode()).get(destAsset.getCode()).add(order);

        return order;
    }

    @Override
    public ExchangeData getExchangeData(String sourceCode, String destinationCode) {
        List<Order> buy = orderRepository.findByIsClosedFalseAndSource_codeAndDestination_code(sourceCode, destinationCode);
        List<Order> sell = orderRepository.findByIsClosedFalseAndSource_codeAndDestination_code(destinationCode, sourceCode);

        ExchangeData exchangeData = new ExchangeData();
        exchangeData.setBuys(buy);
        exchangeData.setSells(sell);
        return exchangeData;
    }

    @Override
    public List<Order> getOpenOrdersForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return orderRepository.findByIsClosedFalseAndRequester_username(currentUser.getUsername());
    }

    @Override
    public List<Order> getClosedOrdersForCurrentUser() {
        User currentUser = userService.getCurrentUser();
        return orderRepository.findByIsClosedTrueAndRequester_username(currentUser.getUsername());
    }

    @Override
    public Order findByTrackingCode(String trackingCode) {
        UUID uuid = UUID.fromString(trackingCode);
        return orderRepository.findByTrackingCode(uuid).orElse(null);
    }

    @Scheduled(fixedDelay = 5000, initialDelay = 10000)
    public synchronized void matchOrders() {
        for (Map.Entry<String, Map<String, List<Order>>> map1 : exchangeMap.entrySet()) {
            String sourceCode = map1.getKey();
            for (Map.Entry<String, List<Order>> map2 : map1.getValue().entrySet()) {
                String destinationCode = map2.getKey();
                List<Order> buy = map2.getValue();
                try {
                    List<Order> sell = exchangeMap.get(destinationCode).get(sourceCode);
                    List<SealTheDealDto> sealTheDealDto = new ArrayList<>();
                    for (Order buyOrder : buy) {
                        for (Order sellOrder : sell) {
                            if (buyOrder.getSourceAmount().compareTo(sellOrder.getDestinationAmount()) == 0 && sellOrder.getSourceAmount().compareTo(buyOrder.getDestinationAmount()) == 0) {
                                sealTheDealDto.add(new SealTheDealDto(buyOrder, sellOrder));
                            }
                        }
                    }
                    OrderServiceImpl thisBean = applicationContext.getBean(this.getClass());
                    sealTheDealDto.forEach(thisBean::sealTheDeal);

                } catch (Throwable t) {
                    logger.error("error in exchange {} to {} :", sourceCode, destinationCode, t);
                }
            }
        }
    }

    @Transactional
    public void sealTheDeal(SealTheDealDto dto) {
        Date now = new Date();
        closeOrder(dto.getOrder1().getId(), now);
        closeOrder(dto.getOrder2().getId(), now);

        String asset1 = dto.getOrder1().getSource().getCode();
        String asset2 = dto.getOrder1().getDestination().getCode();
        exchangeMap.get(asset1).get(asset2).remove(dto.getOrder1());
        exchangeMap.get(asset1).get(asset2).remove(dto.getOrder2());
        exchangeMap.get(asset2).get(asset1).remove(dto.getOrder1());
        exchangeMap.get(asset2).get(asset1).remove(dto.getOrder2());
    }

    private void closeOrder(Long orderId, Date closeDate) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order dbOrder = optionalOrder.get();
            dbOrder.setCompleted(dbOrder.getDestinationAmount());
            dbOrder.setIsClosed(true);
            dbOrder.setCloseDate(closeDate);
            orderRepository.save(dbOrder);

            User requester = dbOrder.getRequester();
            userService.increaseUserAsset(requester, dbOrder.getDestination(), dbOrder.getDestinationAmount());
        } else {
            logger.error("removed order found that must not: {}", orderId);
            throw new BadRequestRuntimeException("order not found");
        }
    }

    @AllArgsConstructor
    @Getter
    private static class SealTheDealDto {
        private Order order1;
        private Order order2;
    }

}
