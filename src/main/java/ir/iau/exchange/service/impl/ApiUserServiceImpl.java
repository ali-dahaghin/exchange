package ir.iau.exchange.service.impl;

import ir.iau.exchange.entity.ApiUser;
import ir.iau.exchange.repository.ApiUserRepository;
import ir.iau.exchange.service.ApiUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ApiUserServiceImpl implements ApiUserService {

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ApiUser create(ApiUser apiUser, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        apiUser.setPassword(hashedPassword);

        return apiUserRepository.save(apiUser);
    }

    @Override
    public ApiUser findByUsername(String username) {
        return apiUserRepository.findByUsername(username).orElse(null);
    }
}
