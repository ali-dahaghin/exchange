package ir.iau.exchange.dto.data;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDataDto {

    private BigDecimal bid;
    private BigDecimal amount;
    private Integer numberOfOrders;

}
