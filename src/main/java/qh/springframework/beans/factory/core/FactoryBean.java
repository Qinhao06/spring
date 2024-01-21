package qh.springframework.beans.factory.core;

public interface FactoryBean<T> {

    T getObject() throws BeansException;

    Class getObjectType();

    boolean isSingleton();


}
