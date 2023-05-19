package ir.iau.exchange.dto.responses;

import lombok.Data;

@Data
public class BaseResultResponse<T> {
    private String result;
    private T data;
}
