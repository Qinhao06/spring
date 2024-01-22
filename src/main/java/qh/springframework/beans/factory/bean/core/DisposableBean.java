package qh.springframework.beans.factory.bean.core;

public interface DisposableBean {

    void destroy() throws BeansException;

}
