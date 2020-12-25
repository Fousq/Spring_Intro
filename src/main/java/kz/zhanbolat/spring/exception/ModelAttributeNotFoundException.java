package kz.zhanbolat.spring.exception;

public class ModelAttributeNotFoundException extends RuntimeException {
    public ModelAttributeNotFoundException(String message) {
        super(message);
    }

    public ModelAttributeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
