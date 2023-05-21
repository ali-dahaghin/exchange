package ir.iau.exchange.service;

import ir.iau.exchange.dto.CreateUserDto;
import ir.iau.exchange.dto.UserDto;
import ir.iau.exchange.entity.User;

import java.util.List;

public interface UserService {

    User createUser(CreateUserDto dto);

    User findByUsername(String username);

    List<User> findAll();

}