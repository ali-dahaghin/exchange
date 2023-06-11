package ir.iau.exchange.controllers;

import ir.iau.exchange.entity.UserAsset;
import ir.iau.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/dashboard")
    public ModelAndView home() {
        List<UserAsset> currentUserAssets = userService.getCurrentUserAssets();

        ModelAndView modelAndView = new ModelAndView("/user/dashboard");
        modelAndView.addObject("userAssets", currentUserAssets);
        return modelAndView;
    }

}
