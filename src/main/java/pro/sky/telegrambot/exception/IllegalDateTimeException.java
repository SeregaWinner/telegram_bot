package pro.sky.telegrambot.exception;

public class IllegalDateTimeException extends RuntimeException{
    public IllegalDateTimeException() {
    }

    public IllegalDateTimeException(String message) {
        super(message);
    }

    public IllegalDateTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalDateTimeException(Throwable cause) {
        super(cause);
    }

    public IllegalDateTimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
