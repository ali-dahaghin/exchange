package ir.iau.exchange.controllers.api;

import ir.iau.exchange.dto.responses.BaseResultResponse;
import org.springframework.http.ResponseEntity;

public class BaseRestController {

    protected <T> ResponseEntity<BaseResultResponse<T>> ok() {
        BaseResultResponse<T> response = new BaseResultResponse<>();
        response.setResult("ok");
        response.setData(null);
        return ResponseEntity.ok(response);
    }

    protected <T> ResponseEntity<BaseResultResponse<T>> ok(String result) {
        BaseResultResponse<T> response = new BaseResultResponse<>();
        response.setResult(result);
        response.setData(null);
        return ResponseEntity.ok(response);
    }

    protected <T> ResponseEntity<BaseResultResponse<T>> ok(T body, String result) {
        BaseResultResponse<T> response = new BaseResultResponse<>();
        response.setResult(result);
        response.setData(body);
        return ResponseEntity.ok(response);
    }

}
