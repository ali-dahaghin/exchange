package ir.iau.exchange.service;

import ir.iau.exchange.dto.requestes.GetUserBalanceRequestDto;
import ir.iau.exchange.dto.requestes.PaymentRequestDto;
import ir.iau.exchange.dto.responses.GetUserBalanceResponseDto;
import ir.iau.exchange.dto.responses.PaymentResponseDto;

public interface PaymentService {

    PaymentResponseDto pay(PaymentRequestDto paymentRequestDto);

    GetUserBalanceResponseDto getUserBalance(GetUserBalanceRequestDto getUserBalanceRequestDto);

}
