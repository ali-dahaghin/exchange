package ir.iau.exchange.dto;

import lombok.Data;

@Data
public class CreateUserDto {

    private String username;
    private String password;
    private Boolean isAdmin = false;

}
