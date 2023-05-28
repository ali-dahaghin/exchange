package ir.iau.exchange.dto.requestes;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GiveADto {

    private String username;
    private BigDecimal amount;

}
