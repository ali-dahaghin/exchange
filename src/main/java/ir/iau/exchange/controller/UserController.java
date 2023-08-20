package ir.iau.exchange.controller;

import ir.iau.exchange.dto.PaymentHistoryDto;
import ir.iau.exchange.entity.User;
import ir.iau.exchange.entity.UserAsset;
import ir.iau.exchange.service.PaymentService;
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

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/user/dashboard")
    public ModelAndView home() {
        List<UserAsset> currentUserAssets = userService.getCurrentUserAssets();

        User currentUser = userService.getCurrentUser();
        List<PaymentHistoryDto> historyForUser = paymentService.getHistoryForUser(currentUser.getUsername());

        ModelAndView modelAndView = new ModelAndView("user/dashboard");
        modelAndView.addObject("historyForUser", historyForUser);
        modelAndView.addObject("userAssets", currentUserAssets);
        modelAndView.addObject("username", currentUser.getUsername());

        return modelAndView;
    }

}
