package ir.iau.exchange.enums;

import static ir.iau.exchange.Constants.A_A_CODE;
import static ir.iau.exchange.Constants.A_B_CODE;

public enum ExchangeEnum {

    A_A(A_A_CODE), A_B(A_B_CODE);

    private final String code;

    private ExchangeEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


}
