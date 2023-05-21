package ir.iau.exchange;

import ir.iau.exchange.dto.CreateUserDto;
import ir.iau.exchange.dto.UserDto;
import ir.iau.exchange.entity.Role;
import ir.iau.exchange.entity.User;
import ir.iau.exchange.repository.RoleRepository;
import ir.iau.exchange.service.AssetService;
import ir.iau.exchange.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Arrays;
import java.util.Collections;

@SpringBootApplication
public class ExchangeApplication {

	private static final Logger logger = LoggerFactory.getLogger(ExchangeApplication.class);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ExchangeApplication.class, args);
		UserService userService = context.getBean(UserService.class);
		RoleRepository roleRepository = context.getBean(RoleRepository.class);
		AssetService assetService = context.getBean(AssetService.class);

		Role userRole = new Role();
		if (roleRepository.findByName("ROLE_USER") == null) {
			userRole.setName("ROLE_USER");
			roleRepository.save(userRole);
			logger.info("ROLE_USER created");
		}

		Role adminRole = new Role();
		if (roleRepository.findByName("ROLE_ADMIN") == null) {
			adminRole.setName("ROLE_ADMIN");
			roleRepository.save(adminRole);
			logger.info("ROLE_ADMIN created");
		}

		if (userService.findByUsername("admin") == null) {
			CreateUserDto userDto = new CreateUserDto();
			userDto.setUsername("admin");
			userDto.setPassword("password");
			userDto.setIsAdmin(true);
			userService.createUser(userDto);
			logger.info("admin created");
		}

		if (assetService.findByCode("A") == null) {
			assetService.create("A");
			logger.info("asset a created");
		}

		if (assetService.findByCode("B") == null) {
			assetService.create("B");
			logger.info("asset b created");
		}
	}

}
