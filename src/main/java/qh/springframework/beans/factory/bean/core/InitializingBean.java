package qh.springframework.beans.factory.bean.core;

public interface InitializingBean {

    void afterPropertiesSet() throws BeansException;

}
