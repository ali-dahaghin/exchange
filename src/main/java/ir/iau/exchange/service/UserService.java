package ir.iau.exchange.service;

import ir.iau.exchange.dto.requestes.CreateUserRequestDto;
import ir.iau.exchange.entity.User;

import java.util.List;

public interface UserService {

    User createUser(CreateUserRequestDto dto);

    User findByUsername(String username);

    List<User> findAll();

    User getCurrentUser();

}