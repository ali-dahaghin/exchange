package ir.iau.exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableScheduling
public class ExchangeApplication{

	public static void main(String[] args) {
		SpringApplication.run(ExchangeApplication.class, args);
	}

}
