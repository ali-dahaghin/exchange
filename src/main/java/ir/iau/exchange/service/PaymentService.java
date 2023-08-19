package ir.iau.exchange.service;

import ir.iau.exchange.dto.PaymentHistoryDto;
import ir.iau.exchange.dto.requestes.GetUserBalanceRequestDto;
import ir.iau.exchange.dto.requestes.PaymentRequestDto;
import ir.iau.exchange.dto.responses.GetUserBalanceResponseDto;
import ir.iau.exchange.dto.responses.PaymentResponseDto;
import ir.iau.exchange.entity.PaymentHistory;

import java.util.List;

public interface PaymentService {

    PaymentResponseDto pay(PaymentRequestDto paymentRequestDto);

    GetUserBalanceResponseDto getUserBalance(GetUserBalanceRequestDto getUserBalanceRequestDto);

    List<PaymentHistoryDto> getHistoryForUser(String username);

}
