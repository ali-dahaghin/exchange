package ir.iau.exchange.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class GeneralController {

    @GetMapping("/")
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView("index");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            modelAndView.addObject("auth", false);
        } else {
            Optional<? extends GrantedAuthority> roleAdmin = authentication.getAuthorities().stream().filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")).findAny();
            modelAndView.addObject("auth", true);
            modelAndView.addObject("isAdmin", roleAdmin.isPresent());
        }

        return modelAndView;
    }

}
