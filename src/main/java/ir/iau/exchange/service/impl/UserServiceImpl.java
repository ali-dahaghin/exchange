package ir.iau.exchange.service.impl;

import ir.iau.exchange.dto.requestes.CreateUserRequestDto;
import ir.iau.exchange.entity.Asset;
import ir.iau.exchange.entity.Role;
import ir.iau.exchange.entity.User;
import ir.iau.exchange.entity.UserAsset;
import ir.iau.exchange.exceptions.BadRequestRuntimeException;
import ir.iau.exchange.repository.RoleRepository;
import ir.iau.exchange.repository.UserAssetRepository;
import ir.iau.exchange.repository.UserRepository;
import ir.iau.exchange.service.AssetService;
import ir.iau.exchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserAssetRepository userAssetRepository;

    @Autowired
    private AssetService assetService;

    @Override
    @Transactional
    public User createUser(CreateUserRequestDto dto) {
        if (findByUsername(dto.getUsername()) != null) {
            throw new BadRequestRuntimeException("user already exists");
        }

        Role role = dto.getIsAdmin() ? roleRepository.findByName("ROLE_ADMIN") : roleRepository.findByName("ROLE_USER");

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.getRoles().add(role);

        user = userRepository.save(user);

        if (!dto.getIsAdmin()) {
            List<Asset> assetList = assetService.findAll();
            for (Asset asset : assetList) {
                UserAsset userAsset = new UserAsset();
                userAsset.setAsset(asset);
                userAsset.setUser(user);
                userAsset.setBalance(BigDecimal.ZERO);
                userAssetRepository.save(userAsset);
            }
        }

        return user;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BadRequestRuntimeException("not authenticated");
        }
        Object principal = authentication.getPrincipal();
        UserDetails userDetails = (UserDetails) principal;

        return findByUsername(userDetails.getUsername());
    }

    @Override
    public UserAsset getCurrentUserAsset(String assetCode) {
        return userAssetRepository.findByAsset_codeAndUser_username(assetCode, getCurrentUser().getUsername()).orElse(null);
    }

    @Override
    public UserAsset getCurrentUserAsset(Asset asset) {
        return userAssetRepository.findByAssetAndUser(asset, getCurrentUser());
    }

    @Override
    public List<UserAsset> getCurrentUserAssets() {
        List<Asset> assetList = assetService.findAll();
        return assetList.stream().map(this::getCurrentUserAsset).collect(Collectors.toList());
    }

    @Override
    public UserAsset getUserAsset(User user, Asset asset) {
        return userAssetRepository.findByAssetAndUser(asset, user);
    }

    @Override
    @Transactional
    public UserAsset increaseCurrentUserAsset(String assetCode, BigDecimal amount) {
        UserAsset currentUserAsset = getCurrentUserAsset(assetCode);
        currentUserAsset.setBalance(currentUserAsset.getBalance().add(amount));
        return userAssetRepository.save(currentUserAsset);
    }

    @Override
    @Transactional
    public UserAsset increaseUserAsset(User user, Asset asset, BigDecimal amount) {
        UserAsset userAsset = getUserAsset(user, asset);
        userAsset.setBalance(userAsset.getBalance().add(amount));
        return userAssetRepository.save(userAsset);
    }

    @Override
    @Transactional
    public UserAsset decreaseCurrentUserAsset(String assetCode, BigDecimal amount) {
        UserAsset currentUserAsset = getCurrentUserAsset(assetCode);
        currentUserAsset.setBalance(currentUserAsset.getBalance().subtract(amount));
        return userAssetRepository.save(currentUserAsset);
    }

    @Override
    public UserAsset decreaseUserAsset(User user, Asset asset, BigDecimal amount) {
        UserAsset userAsset = getUserAsset(user, asset);
        userAsset.setBalance(userAsset.getBalance().subtract(amount));
        return userAssetRepository.save(userAsset);
    }
}
