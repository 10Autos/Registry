package net.ckranz.registry;

public class MissingKeyException extends RuntimeException {
    public MissingKeyException() {
        super();
    }

    public MissingKeyException(String message) {
        super(message);
    }

    public MissingKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingKeyException(Throwable cause) {
        super(cause);
    }

    protected MissingKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}