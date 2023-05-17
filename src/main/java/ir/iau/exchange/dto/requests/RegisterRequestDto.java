package ir.iau.exchange.dto.requests;

import lombok.Data;

@Data
public class RegisterRequestDto {

    private String username;
    private String name;
    private String plainPassword;

}
