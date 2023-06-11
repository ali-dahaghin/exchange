package ir.iau.exchange.service;

import ir.iau.exchange.dto.requestes.CreateUserRequestDto;
import ir.iau.exchange.entity.Asset;
import ir.iau.exchange.entity.User;
import ir.iau.exchange.entity.UserAsset;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    User createUser(CreateUserRequestDto dto);

    User findByUsername(String username);

    List<User> findAll();

    User getCurrentUser();

    UserAsset getCurrentUserAsset(String assetCode);

    UserAsset getCurrentUserAsset(Asset asset);

    List<UserAsset> getCurrentUserAssets();

    UserAsset getUserAsset(User user, Asset asset);

    UserAsset increaseCurrentUserAsset(String assetCode, BigDecimal amount);

    UserAsset increaseUserAsset(User user, Asset asset, BigDecimal amount);

    UserAsset decreaseCurrentUserAsset(String assetCode, BigDecimal amount);

    UserAsset decreaseUserAsset(User user, Asset asset, BigDecimal amount);

}