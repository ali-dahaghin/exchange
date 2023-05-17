package ir.iau.exchange.controllers;

import ir.iau.exchange.dto.data.DashboardDataDto;
import ir.iau.exchange.dto.requests.LoginRequestDto;
import ir.iau.exchange.dto.requests.RegisterRequestDto;
import ir.iau.exchange.exceptions.BadRequestRuntimeException;
import ir.iau.exchange.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("register")
    public String registerPage() {
        return "user/dashboard";
    }

    @PostMapping("register")
    public String register(@RequestBody RegisterRequestDto requestDto, HttpServletResponse response, Model model) throws IOException {
        try {
            userService.register(requestDto);
            response.sendRedirect("/user/dashboard");
        } catch (BadRequestRuntimeException badRequest) {
            model.addAttribute("error", badRequest.getMessage());
        }

        return "user/dashboard";
    }

    @GetMapping("login")
    public String loginPage() {
        return "user/login";
    }

    @PostMapping("login")
    public String login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response, Model model) throws IOException {
        try {
            userService.login(requestDto);
            response.sendRedirect("/user/dashboard");
        } catch (BadRequestRuntimeException badRequest) {
            model.addAttribute("error", badRequest.getMessage());
        }

        return "user/dashboard";
    }

    @GetMapping("dashboard")
    public String dashboard(Model model) {
        DashboardDataDto dashB = userService.getDashboardDataForCurrentSession();
        model.addAttribute("dashB", dashB);
        return "user/dashboard";
    }

}
