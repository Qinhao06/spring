package qh.springframework.factory.bean.core;

public interface FactoryBean<T> {

    T getObject() throws BeansException;

    Class getObjectType();

    boolean isSingleton();


}
