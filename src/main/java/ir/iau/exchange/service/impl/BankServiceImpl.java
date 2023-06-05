package ir.iau.exchange.service.impl;

import ir.iau.exchange.dto.requestes.BankConfirmDTO;
import ir.iau.exchange.dto.requestes.BankInitDTO;
import ir.iau.exchange.exceptions.BadRequestRuntimeException;
import ir.iau.exchange.service.BankService;
import ir.iau.exchange.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private UserService userService;

    private final List<BankInitRecord> bankInitRecords = new ArrayList<>();

    @Override
    public synchronized String init(BankInitDTO dto) {
        String randomToken = UUID.randomUUID().toString();
        BankInitRecord bankInitRecord = new BankInitRecord(randomToken, dto.getAmount(), new Date());
        bankInitRecords.add(bankInitRecord);
        return randomToken;
    }

    @Override
    public synchronized void confirm(BankConfirmDTO dto) {
        Optional<BankInitRecord> record = bankInitRecords.stream().filter(bankInitRecord -> bankInitRecord.getToken().equals(dto.getToken())).findFirst();
        if (!record.isPresent()) {
            throw new BadRequestRuntimeException("token invalid");
        }
        BankInitRecord foundRecord = record.get();
        bankInitRecords.remove(foundRecord);
        userService.increaseCurrentUserAsset("B", foundRecord.getAmount());
    }

    @Scheduled(fixedDelay = 1800000) // 30 min
    public void removeOldRecords() {
        List<BankInitRecord> temp = new ArrayList<>(bankInitRecords);
        long now = new Date().getTime();
        for (BankInitRecord record : temp) {
            if (record.getCreateDate().getTime() + 1800000 < now) {
                bankInitRecords.remove(record);
            }
        }
    }

    @Data
    @AllArgsConstructor
    private static class BankInitRecord {
        private String token;
        private BigDecimal amount;
        private Date createDate;
    }
}
