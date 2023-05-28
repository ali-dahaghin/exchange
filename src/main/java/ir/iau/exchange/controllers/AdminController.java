package ir.iau.exchange.controllers;

import ir.iau.exchange.dto.requestes.GiveADto;
import ir.iau.exchange.service.AdminService;
import ir.iau.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/give")
    public ModelAndView give() {
        ModelAndView modelAndView = new ModelAndView("/admin/give");
        modelAndView.addObject("users", userService.findAll());

        GiveADto giveADto = new GiveADto();

        modelAndView.addObject("req", giveADto);
        return modelAndView;
    }

    @PostMapping("/give/act")
    public ModelAndView giveAct(@ModelAttribute GiveADto giveADto) {
        adminService.giveAAsset(giveADto);
        return new ModelAndView("redirect:/admin/give?success");
    }

}
