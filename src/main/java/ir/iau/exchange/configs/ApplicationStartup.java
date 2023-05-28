package ir.iau.exchange.configs;

import ir.iau.exchange.dto.requestes.CreateUserRequestDto;
import ir.iau.exchange.entity.Role;
import ir.iau.exchange.repository.RoleRepository;
import ir.iau.exchange.service.AssetService;
import ir.iau.exchange.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AssetService assetService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
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
            CreateUserRequestDto userDto = new CreateUserRequestDto();
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
