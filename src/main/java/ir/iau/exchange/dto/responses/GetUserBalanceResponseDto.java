package ir.iau.exchange.dto.responses;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetUserBalanceResponseDto extends BaseResponseDto {

    private BigDecimal balance;

}
