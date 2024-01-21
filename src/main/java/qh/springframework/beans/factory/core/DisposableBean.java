package qh.springframework.beans.factory.core;

public interface DisposableBean {

    void destroy() throws BeansException;

}
