package ir.iau.exchange.dto.requestes;

import lombok.Data;

@Data
public class GetUserBalanceRequestDto {

    private String username;
    private String assetCode;

}
