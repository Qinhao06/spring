package qh.springframework.factory.bean.core;

public interface InitializingBean {

    void afterPropertiesSet() throws BeansException;

}
