package ir.iau.exchange.services;

import ir.iau.exchange.dto.data.ExchangeDataDto;
import ir.iau.exchange.dto.requests.OrderRequestDto;
import ir.iau.exchange.enums.ExchangeEnum;

public interface OrderService {

    void order(OrderRequestDto requestDto);

    ExchangeDataDto getExchangeData(ExchangeEnum exchangeEnum);

}
