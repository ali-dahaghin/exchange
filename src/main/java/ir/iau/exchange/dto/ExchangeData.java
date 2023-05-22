package ir.iau.exchange.dto;

import ir.iau.exchange.entity.Order;
import lombok.Data;

import java.util.List;

@Data
public class ExchangeData {

    private List<Order> sells;
    private List<Order> buys;

}
