package ir.iau.exchange.controllers;

import ir.iau.exchange.dto.requestes.BankConfirmDTO;
import ir.iau.exchange.dto.requestes.BankInitDTO;
import ir.iau.exchange.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("bank")
public class BankController {

    @Autowired
    private BankService bankService;

    @GetMapping("buy")
    public ModelAndView buyPage() {
        ModelAndView modelAndView = new ModelAndView("bank/buy");
        BankInitDTO bankInitDTO = new BankInitDTO();
        modelAndView.addObject("req", bankInitDTO);
        return modelAndView;
    }

    @PostMapping("buy")
    public ModelAndView buy(@ModelAttribute BankInitDTO bankInitDTO) {
        String token = bankService.init(bankInitDTO);
        return new ModelAndView("redirect:/mock-ipg/click?token=" + token);
    }

    @PostMapping("confirm")
    public ModelAndView buy(@ModelAttribute BankConfirmDTO bankConfirmDTO) {
        bankService.confirm(bankConfirmDTO);
        return new ModelAndView("redirect:/");
    }

}
