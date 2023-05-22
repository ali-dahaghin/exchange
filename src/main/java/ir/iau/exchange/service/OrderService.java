package ir.iau.exchange.service;

import ir.iau.exchange.dto.ExchangeData;
import ir.iau.exchange.dto.requestes.SubmitOrderRequestDto;
import ir.iau.exchange.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order submitForCurrentUser(SubmitOrderRequestDto requestDto);

    ExchangeData getExchangeData(String sourceCode, String destinationCode);

    List<Order> getOpenOrdersForCurrentUser();

    List<Order> getClosedOrdersForCurrentUser();

    Order findByTrackingCode(String trackingCode);

}
