package ir.iau.exchange.exception;

public class BadRequestRuntimeException extends RuntimeException {

    public BadRequestRuntimeException() {
    }

    public BadRequestRuntimeException(String message) {
        super(message);
    }

    public BadRequestRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestRuntimeException(Throwable cause) {
        super(cause);
    }

    public BadRequestRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
