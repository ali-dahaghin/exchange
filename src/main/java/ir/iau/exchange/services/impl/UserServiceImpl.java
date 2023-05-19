package ir.iau.exchange.services.impl;

import ir.iau.exchange.dto.requests.RegisterRequestDto;
import ir.iau.exchange.exceptions.BadRequestRuntimeException;
import ir.iau.exchange.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDetailsManager userDetailsManager;

    @Override
    public UserDetails register(RegisterRequestDto requestDto) {
        if (userDetailsManager.userExists(requestDto.getUsername())) {
            throw new BadRequestRuntimeException("user already exists");
        }

        UserDetails user = User.builder().passwordEncoder((pass) -> passwordEncoder.encode(pass))
                .username(requestDto.getUsername())
                .password(requestDto.getPlainPassword())
                .roles("USER")
                .build();

        userDetailsManager.createUser(user);

        return user;
    }

}
