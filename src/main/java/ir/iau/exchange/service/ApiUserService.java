package ir.iau.exchange.service;

import ir.iau.exchange.entity.ApiUser;

public interface ApiUserService {

    ApiUser create(ApiUser apiUser, String rawPassword);

    ApiUser findByUsername(String username);

}
