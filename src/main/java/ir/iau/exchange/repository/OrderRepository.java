package ir.iau.exchange.repository;

import ir.iau.exchange.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByIsClosedFalseAndSource_codeAndDestination_code(String sourceCode, String destinationCode);

    List<Order> findByIsClosedFalseAndSource_codeAndDestination_codeOrderBySubmitDateDesc(String sourceCode, String destinationCode);

    List<Order> findByIsClosedFalseAndRequester_username(String username);

    List<Order> findByIsClosedTrueAndRequester_username(String username);

    Optional<Order> findByTrackingCode(UUID uuid);

}
