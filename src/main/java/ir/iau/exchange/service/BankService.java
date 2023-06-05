package ir.iau.exchange.service;

import ir.iau.exchange.dto.requestes.BankConfirmDTO;
import ir.iau.exchange.dto.requestes.BankInitDTO;

public interface BankService {

    String init(BankInitDTO dto);

    void confirm(BankConfirmDTO dto);

}
