package ir.iau.exchange.controller;

import ir.iau.exchange.dto.requestes.BankConfirmDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("mock-ipg")
public class MockIPGController {

    @GetMapping("click")
    public ModelAndView click(@RequestParam("token") String token) {
        ModelAndView modelAndView = new ModelAndView("mock-ipg/click");
        BankConfirmDTO bankConfirmDTO = new BankConfirmDTO();
        bankConfirmDTO.setToken(token);
        modelAndView.addObject("req", bankConfirmDTO);
        return modelAndView;
    }

}
