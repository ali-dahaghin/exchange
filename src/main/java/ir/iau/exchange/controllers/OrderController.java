package ir.iau.exchange.controllers;

import ir.iau.exchange.dto.data.ExchangeDataDto;
import ir.iau.exchange.dto.data.UserAssetDataDto;
import ir.iau.exchange.dto.requests.OrderRequestDto;
import ir.iau.exchange.enums.AssetEnum;
import ir.iau.exchange.enums.ExchangeEnum;
import ir.iau.exchange.exceptions.BadRequestRuntimeException;
import ir.iau.exchange.services.OrderService;
import ir.iau.exchange.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ir.iau.exchange.Constants.*;

@Controller("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping(A_A_CODE)
    public String AtoA(Model model) {
        ExchangeDataDto exchangeData = orderService.getExchangeData(ExchangeEnum.A_A);
        UserAssetDataDto assetData = userService.getAssetDataForCurrentSession(AssetEnum.A);
        model.addAttribute("exchangeData", exchangeData);
        model.addAttribute("assetData", assetData);
        return "order/" + A_A_CODE;
    }

    @GetMapping(A_B_CODE)
    public String AtoB(Model model) {
        ExchangeDataDto exchangeData = orderService.getExchangeData(ExchangeEnum.A_B);
        UserAssetDataDto assetAData = userService.getAssetDataForCurrentSession(AssetEnum.A);
        UserAssetDataDto assetBData = userService.getAssetDataForCurrentSession(AssetEnum.B);
        model.addAttribute("exchangeData", exchangeData);
        model.addAttribute("assetAData", assetAData);
        model.addAttribute("assetBData", assetBData);
        return "order/" + A_B_CODE;
    }

    @PostMapping
    public String order(@RequestBody OrderRequestDto requestDto, HttpServletResponse response, HttpServletRequest request, Model model) throws IOException {
        String referer = request.getHeader("Referer");
        try {
            orderService.order(requestDto);
            response.sendRedirect(referer);
        } catch (BadRequestRuntimeException badRequest) {
            model.addAttribute("error", badRequest.getMessage());
        }

        return referer;
    }

}
