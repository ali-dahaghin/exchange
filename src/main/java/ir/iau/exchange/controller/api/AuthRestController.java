package ir.iau.exchange.controller.api;

import ir.iau.exchange.dto.requestes.LoginRequestDto;
import ir.iau.exchange.entity.ApiUser;
import ir.iau.exchange.service.ApiUserService;
import ir.iau.exchange.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthRestController {

    private static final Logger logger = LoggerFactory.getLogger(AuthRestController.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.expire.millisecond}")
    private Integer jwtExpireMillisecond;

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto loginRequestDto) {
        try {
            ApiUser apiUser = apiUserService.findByUsername(loginRequestDto.getUsername());
            if (apiUser == null || !passwordEncoder.matches(loginRequestDto.getPassword(), apiUser.getPassword())) {
                return ResponseEntity.badRequest().body("invalid");
            }

            String jwt = jwtService.generateToken(loginRequestDto.getUsername(), jwtExpireMillisecond);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            logger.error("login: ", e);
            return ResponseEntity.internalServerError().body("error");
        }
    }

}
