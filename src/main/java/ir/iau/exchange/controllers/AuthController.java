package ir.iau.exchange.controllers;

import ir.iau.exchange.dto.CreateUserDto;
import ir.iau.exchange.exceptions.BadRequestRuntimeException;
import ir.iau.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/auth/register")
    public ModelAndView registerPage() {
        ModelAndView modelAndView = new ModelAndView();
        CreateUserDto createUserDto = new CreateUserDto();
        modelAndView.addObject("user", createUserDto);
        return modelAndView;
    }

    @PostMapping("/auth/register/save")
    public String register(@ModelAttribute CreateUserDto createUserDto) {
        try {
            userService.createUser(createUserDto);
            return "redirect:/auth/login";
        } catch (BadRequestRuntimeException e) {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/auth/login")
    public String login() {
        return "auth/login";
    }
}