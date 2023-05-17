package ir.iau.exchange.dto.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserAssetDataDto {

    private String code;
    private String name;
    private BigDecimal amount;

}
