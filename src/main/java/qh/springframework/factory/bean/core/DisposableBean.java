package qh.springframework.factory.bean.core;

public interface DisposableBean {

    void destroy() throws BeansException;

}
