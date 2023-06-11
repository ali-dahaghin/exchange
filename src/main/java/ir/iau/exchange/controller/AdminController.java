package ir.iau.exchange.controller;

import ir.iau.exchange.dto.UserDto;
import ir.iau.exchange.dto.requestes.GiveADto;
import ir.iau.exchange.entity.User;
import ir.iau.exchange.exception.BadRequestRuntimeException;
import ir.iau.exchange.service.AdminService;
import ir.iau.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/give")
    public ModelAndView giveAct(@ModelAttribute GiveADto giveADto) {
        try {
            adminService.giveAAsset(giveADto);
            return new ModelAndView("redirect:/admin/give?success");
        } catch (BadRequestRuntimeException badRequestRuntimeException) {
            return new ModelAndView("redirect:/admin/give?userNotExists");
        }
    }

    @GetMapping("/users")
    public ModelAndView users() {
        ModelAndView modelAndView = new ModelAndView("/admin/users");
        List<User> dbUsers = userService.findAll();
        List<UserDto> userDtoList = dbUsers.stream().map(dbUser -> {
            UserDto userDto = new UserDto();
            userDto.setUsername(dbUser.getUsername());
            userDto.setIsAdmin(dbUser.getRoles().stream().anyMatch(role -> role.getName().equals("ROLE_ADMIN")));
            return userDto;
        }).collect(Collectors.toList());
        modelAndView.addObject("users", userDtoList);
        return modelAndView;
    }

}
