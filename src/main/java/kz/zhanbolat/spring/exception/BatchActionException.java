package kz.zhanbolat.spring.exception;

public class BatchActionException extends RuntimeException {
    public BatchActionException(String message) {
        super(message);
    }
}
