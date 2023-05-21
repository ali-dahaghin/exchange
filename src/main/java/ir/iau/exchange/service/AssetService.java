package ir.iau.exchange.service;

import ir.iau.exchange.entity.Asset;

public interface AssetService {

    Asset create(String code);

    Asset findByCode(String code);

}
