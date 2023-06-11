package ir.iau.exchange.service.impl;

import ir.iau.exchange.entity.Asset;
import ir.iau.exchange.exception.BadRequestRuntimeException;
import ir.iau.exchange.repository.AssetRepository;
import ir.iau.exchange.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public Asset create(String code) {
        if (findByCode(code) != null) {
            throw new BadRequestRuntimeException("asset already exists");
        }

        Asset asset = new Asset();
        asset.setCode(code);

        return assetRepository.save(asset);
    }

    @Override
    public Asset findByCode(String code) {
        return assetRepository.findByCode(code).orElse(null);
    }

    @Override
    public List<Asset> findAll() {
        return assetRepository.findAll();
    }
}
