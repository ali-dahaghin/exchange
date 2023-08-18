package ir.iau.exchange.service.impl;

import ir.iau.exchange.dto.requestes.GetUserBalanceRequestDto;
import ir.iau.exchange.dto.requestes.PaymentRequestDto;
import ir.iau.exchange.dto.responses.GetUserBalanceResponseDto;
import ir.iau.exchange.dto.responses.PaymentResponseDto;
import ir.iau.exchange.entity.*;
import ir.iau.exchange.repository.PaymentHistoryRepository;
import ir.iau.exchange.service.AssetService;
import ir.iau.exchange.service.PaymentService;
import ir.iau.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private UserService userService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private ApiUserServiceImpl apiUserService;

    @Override
    @Transactional
    public PaymentResponseDto pay(PaymentRequestDto paymentRequestDto) {
        User user = userService.findByUsername(paymentRequestDto.getUsername());
        if (user == null) {
            PaymentResponseDto responseDto = new PaymentResponseDto();
            responseDto.setResponseCode("user.not_found");
            return responseDto;
        }
        
        Asset asset = assetService.findByCode(paymentRequestDto.getAssetCode());
        if (asset == null) {
            PaymentResponseDto responseDto = new PaymentResponseDto();
            responseDto.setResponseCode("asset.not_found");
            return responseDto;
        }

        UserAsset userAsset = userService.getUserAsset(user, asset);
        if (userAsset.getBalance().compareTo(paymentRequestDto.getAmount()) < 0) {
            PaymentResponseDto responseDto = new PaymentResponseDto();
            responseDto.setResponseCode("balance.not_enough");
            return responseDto;
        }

        userService.decreaseUserAsset(user, asset, paymentRequestDto.getAmount());

        PaymentHistory paymentHistory = new PaymentHistory();
        paymentHistory.setUser(user);
        paymentHistory.setAsset(asset);
        paymentHistory.setAmount(paymentRequestDto.getAmount());
        paymentHistory.setDescription(paymentRequestDto.getDescription());
        paymentHistory.setIntegrationDate(new Date());
        String trackingCode = generateTrackingCode();
        paymentHistory.setTrackingCode(trackingCode);

        String apiUserUsername = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiUser apiUser = apiUserService.findByUsername(apiUserUsername);
        paymentHistory.setApiUser(apiUser);

        paymentHistoryRepository.save(paymentHistory);

        PaymentResponseDto responseDto = new PaymentResponseDto();
        responseDto.setTrackingCode(trackingCode);
        responseDto.setResponseCode("ok");

        return responseDto;
    }

    private static String generateTrackingCode() {
        return UUID.randomUUID().toString();
    }

    @Override
    public GetUserBalanceResponseDto getUserBalance(GetUserBalanceRequestDto requestDto) {
        User user = userService.findByUsername(requestDto.getUsername());
        if (user == null) {
            GetUserBalanceResponseDto responseDto = new GetUserBalanceResponseDto();
            responseDto.setResponseCode("user.not_found");
            return responseDto;
        }

        Asset asset = assetService.findByCode(requestDto.getAssetCode());
        if (asset == null) {
            GetUserBalanceResponseDto responseDto = new GetUserBalanceResponseDto();
            responseDto.setResponseCode("asset.not_found");
            return responseDto;
        }

        UserAsset userAsset = userService.getUserAsset(user, asset);

        GetUserBalanceResponseDto responseDto = new GetUserBalanceResponseDto();
        responseDto.setBalance(userAsset.getBalance());
        responseDto.setResponseCode("ok");
        
        return responseDto;
    }
}
