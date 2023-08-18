package ir.iau.exchange.dto.responses;

import lombok.Data;

@Data
public class PaymentResponseDto extends BaseResponseDto {

    private String trackingCode;

}
