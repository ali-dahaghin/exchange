package ir.iau.exchange.services;

import ir.iau.exchange.dto.data.DashboardDataDto;
import ir.iau.exchange.dto.data.UserAssetDataDto;
import ir.iau.exchange.dto.requests.LoginRequestDto;
import ir.iau.exchange.dto.requests.RegisterRequestDto;
import ir.iau.exchange.enums.AssetEnum;

public interface UserService {

    void register(RegisterRequestDto requestDto);

    void login(LoginRequestDto requestDto);

    DashboardDataDto getDashboardDataForCurrentSession();

    UserAssetDataDto getAssetDataForCurrentSession(AssetEnum assetEnum);

}
