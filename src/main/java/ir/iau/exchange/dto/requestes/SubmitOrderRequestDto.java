package ir.iau.exchange.dto.requestes;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubmitOrderRequestDto {

    private String sourceCode;
    private String destinationCode;
    private BigDecimal amount;
    private BigDecimal price;

}
