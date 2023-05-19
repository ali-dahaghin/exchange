package ir.iau.exchange.controllers.api;

import ir.iau.exchange.dto.requests.LoginRequestDto;
import ir.iau.exchange.dto.requests.RegisterRequestDto;
import ir.iau.exchange.dto.responses.BaseResultResponse;
import ir.iau.exchange.dto.responses.LoginResponseDto;
import ir.iau.exchange.dto.responses.RegisterResponseDto;
import ir.iau.exchange.exceptions.BadRequestRuntimeException;
import ir.iau.exchange.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("user")
public class UserRestController extends BaseRestController {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public ResponseEntity<BaseResultResponse<RegisterResponseDto>> register(@RequestBody RegisterRequestDto requestDto) {
        userService.register(requestDto);
        return ok();
    }

//    @PostMapping("login")
//    public ResponseEntity<BaseResultResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto requestDto) {
//        userService.login(requestDto);
//        return ok();
//    }

}
