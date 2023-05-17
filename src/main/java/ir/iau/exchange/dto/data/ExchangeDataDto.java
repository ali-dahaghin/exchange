package ir.iau.exchange.dto.data;

import ir.iau.exchange.enums.ExchangeEnum;
import lombok.Data;

import java.util.List;

@Data
public class ExchangeDataDto {

    private ExchangeEnum exchangeEnum;
    private List<OrderDataDto> sellOrders;
    private List<OrderDataDto> buyOrders;

}
