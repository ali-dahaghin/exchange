package ir.iau.exchange.controller;

import ir.iau.exchange.dto.ExchangeData;
import ir.iau.exchange.dto.requestes.SubmitOrderRequestDto;
import ir.iau.exchange.service.OrderService;
import ir.iau.exchange.service.UserService;
import ir.iau.exchange.service.impl.OrderServiceImpl;
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

    @GetMapping
    public ModelAndView orderPage() {
        ModelAndView modelAndView = new ModelAndView("order/order");
        SubmitOrderRequestDto requestDto = new SubmitOrderRequestDto();
        modelAndView.addObject("req", requestDto);
        ExchangeData exchangeData = orderService.getExchangeData("A", "B");
        modelAndView.addObject("exAToB", exchangeData.getBuys());
        modelAndView.addObject("exBToA", exchangeData.getSells());
        modelAndView.addObject("open", orderService.getOpenOrdersForCurrentUser());
        modelAndView.addObject("closed", orderService.getClosedOrdersForCurrentUser());
        modelAndView.addObject("userAAsset", userService.getCurrentUserAsset("A"));
        modelAndView.addObject("userBAsset", userService.getCurrentUserAsset("B"));
        return modelAndView;
    }

    @PostMapping("/submit")
    public ModelAndView submitOrder(@ModelAttribute SubmitOrderRequestDto requestDto) {
        try {
            orderService.submitForCurrentUser(requestDto);
            return new ModelAndView("redirect:/order");
        } catch (OrderServiceImpl.SourceAssetNotFound e) {
            return new ModelAndView("redirect:/order?srcNF");
        } catch (OrderServiceImpl.DestinationAssetNotFound e) {
            return new ModelAndView("redirect:/order?destNF");
        } catch (OrderServiceImpl.NotEnoughBalance e) {
            return new ModelAndView("redirect:/order?balance");
        }
    }

}
