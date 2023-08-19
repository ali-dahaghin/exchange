package ir.iau.exchange.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentHistoryDto {

    private String integrationDate;
    private String description;
    private String assetCode;
    private BigDecimal amount;
    private String trackingCode;
    private String thirdParty;

}
