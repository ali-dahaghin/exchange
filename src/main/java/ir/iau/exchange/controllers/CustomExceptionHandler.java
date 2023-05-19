package ir.iau.exchange.controllers;

import ir.iau.exchange.dto.responses.BaseResultResponse;
import ir.iau.exchange.exceptions.BadRequestRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    @ExceptionHandler(Throwable.class)
    public String handleThrowable(Throwable t) {
        logger.error("handleThrowable: ", t);
        return "internal";
    }

    @ExceptionHandler(BadRequestRuntimeException.class)
    public ResponseEntity<BaseResultResponse<?>> handleBadRequestRuntimeException(BadRequestRuntimeException e) {
        BaseResultResponse<Object> response = new BaseResultResponse<>();
        response.setResult(e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

}
