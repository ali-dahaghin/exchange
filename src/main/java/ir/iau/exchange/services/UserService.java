package ir.iau.exchange.services;

import ir.iau.exchange.dto.requests.LoginRequestDto;
import ir.iau.exchange.dto.requests.RegisterRequestDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {

    UserDetails register(RegisterRequestDto requestDto);

}
