package qh.springframework.beans.factory.core;

public interface InitializingBean {

    void afterPropertiesSet() throws BeansException;

}
