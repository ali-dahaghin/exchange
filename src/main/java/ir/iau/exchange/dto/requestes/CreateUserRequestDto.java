package ir.iau.exchange.dto.requestes;

import lombok.Data;

@Data
public class CreateUserRequestDto {

    private String username;
    private String password;
    private Boolean isAdmin = false;

}
