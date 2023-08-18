package ir.iau.exchange.controller.api;

import ir.iau.exchange.dto.requestes.GetUserBalanceRequestDto;
import ir.iau.exchange.dto.requestes.PaymentRequestDto;
import ir.iau.exchange.dto.responses.GetUserBalanceResponseDto;
import ir.iau.exchange.dto.responses.PaymentResponseDto;
import ir.iau.exchange.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/payment")
public class PaymentRestController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("pay")
    public ResponseEntity<PaymentResponseDto> pay(@RequestBody PaymentRequestDto requestDto) {
        PaymentResponseDto responseDto = paymentService.pay(requestDto);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("get-balance")
    public ResponseEntity<GetUserBalanceResponseDto> getBalance(@RequestBody GetUserBalanceRequestDto requestDto) {
        GetUserBalanceResponseDto userBalance = paymentService.getUserBalance(requestDto);

        return ResponseEntity.ok(userBalance);
    }

}
