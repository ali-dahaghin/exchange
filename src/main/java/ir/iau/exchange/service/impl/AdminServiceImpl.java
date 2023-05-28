package ir.iau.exchange.service.impl;

import ir.iau.exchange.dto.requestes.GiveADto;
import ir.iau.exchange.entity.Asset;
import ir.iau.exchange.entity.User;
import ir.iau.exchange.exceptions.BadRequestRuntimeException;
import ir.iau.exchange.service.AdminService;
import ir.iau.exchange.service.AssetService;
import ir.iau.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserService userService;

    @Autowired
    private AssetService assetService;

    @Override
    public void giveAAsset(GiveADto dto) {
        User user = userService.findByUsername(dto.getUsername());

        if (user == null) {
            throw new BadRequestRuntimeException("user not found");
        }

        Asset asset = assetService.findByCode("A");

        userService.increaseUserAsset(user, asset, dto.getAmount());
    }
}
