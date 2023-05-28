package ir.iau.exchange.repository;

import ir.iau.exchange.entity.Asset;
import ir.iau.exchange.entity.User;
import ir.iau.exchange.entity.UserAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAssetRepository extends JpaRepository<UserAsset, Long> {

    Optional<UserAsset> findByAsset_codeAndUser_username(String assetCode, String username);

    UserAsset findByAssetAndUser(Asset asset, User user);
}
