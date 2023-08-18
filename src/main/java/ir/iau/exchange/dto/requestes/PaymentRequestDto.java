package ir.iau.exchange.dto.requestes;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDto {

    private String username;
    private String description;
    private BigDecimal amount;
    private String assetCode;

}
