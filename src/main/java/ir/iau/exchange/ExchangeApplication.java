package ir.iau.exchange;

import ir.iau.exchange.dto.requestes.CreateUserRequestDto;
import ir.iau.exchange.entity.Role;
import ir.iau.exchange.repository.RoleRepository;
import ir.iau.exchange.service.AssetService;
import ir.iau.exchange.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExchangeApplication{

	public static void main(String[] args) {
		SpringApplication.run(ExchangeApplication.class, args);
	}

}
