package ir.iau.exchange.service;

import ir.iau.exchange.entity.Asset;

import java.util.List;

public interface AssetService {

    Asset create(String code);

    Asset findByCode(String code);

    List<Asset> findAll();

}
