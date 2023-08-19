package ir.iau.exchange.repository;

import ir.iau.exchange.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

    List<PaymentHistory> findByUser_usernameOrderByIntegrationDateDesc(String username);

}
