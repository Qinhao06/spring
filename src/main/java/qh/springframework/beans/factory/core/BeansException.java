package qh.springframework.beans.factory.core;

public class BeansException extends RuntimeException{

    public BeansException(String errorMessage) {
        super(errorMessage);
    }

    public BeansException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }


}
