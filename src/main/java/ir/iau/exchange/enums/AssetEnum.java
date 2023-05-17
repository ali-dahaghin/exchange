package ir.iau.exchange.enums;

import static ir.iau.exchange.Constants.ASSET_A_CODE;
import static ir.iau.exchange.Constants.ASSET_B_CODE;

public enum AssetEnum {

    A(ASSET_A_CODE), B(ASSET_B_CODE);

    private final String code;

    private AssetEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
