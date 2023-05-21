package ir.iau.exchange.service;

import ir.iau.exchange.dto.CreateUserDto;
import ir.iau.exchange.dto.UserDto;
import ir.iau.exchange.entity.Role;
import ir.iau.exchange.entity.User;
import ir.iau.exchange.exceptions.BadRequestRuntimeException;
import ir.iau.exchange.repository.RoleRepository;
import ir.iau.exchange.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User createUser(CreateUserDto dto) {
        if (findByUsername(dto.getUsername()) != null) {
            throw new BadRequestRuntimeException("user already exists");
        }

        Role role = dto.getIsAdmin() ? roleRepository.findByName("ROLE_ADMIN") : roleRepository.findByName("ROLE_USER");

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

}
