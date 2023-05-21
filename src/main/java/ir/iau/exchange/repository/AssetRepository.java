package ir.iau.exchange.repository;

import ir.iau.exchange.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> findByCode(String code);

}
