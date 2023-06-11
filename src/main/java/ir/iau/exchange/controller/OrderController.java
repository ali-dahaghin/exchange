package ir.iau.exchange.controller;

import ir.iau.exchange.dto.requestes.SubmitOrderRequestDto;
import ir.iau.exchange.service.OrderService;
import ir.iau.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ModelAndView orderPage() {
        ModelAndView modelAndView = new ModelAndView("order/order");
        SubmitOrderRequestDto requestDto = new SubmitOrderRequestDto();
        modelAndView.addObject("req", requestDto);
        modelAndView.addObject("ex-a-b", orderService.getExchangeData("A", "B"));
        modelAndView.addObject("ex-b-a", orderService.getExchangeData("B", "A"));
        modelAndView.addObject("open", orderService.getOpenOrdersForCurrentUser());
        modelAndView.addObject("closed", orderService.getClosedOrdersForCurrentUser());
        modelAndView.addObject("user-a", userService.getCurrentUserAsset("A"));
        modelAndView.addObject("user-b", userService.getCurrentUserAsset("B"));
        return modelAndView;
    }

    @PostMapping("/submit")
    public ModelAndView submitOrder(@ModelAttribute SubmitOrderRequestDto requestDto) {
        orderService.submitForCurrentUser(requestDto);
        return new ModelAndView("redirect:order/order");
    }

}
