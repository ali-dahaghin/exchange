package ir.iau.exchange.controller;

import ir.iau.exchange.dto.PaymentHistoryDto;
import ir.iau.exchange.entity.User;
import ir.iau.exchange.service.PaymentService;
import ir.iau.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserService userService;

    @GetMapping("list")
    public ModelAndView getListOfPaymentHistory() {
        User currentUser = userService.getCurrentUser();
        List<PaymentHistoryDto> historyForUser = paymentService.getHistoryForUser(currentUser.getUsername());

        ModelAndView modelAndView = new ModelAndView("user/payments");
        modelAndView.addObject("historyForUser", historyForUser);

        return modelAndView;
    }

}
