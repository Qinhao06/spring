package qh.springframework.factory.bean.core;

public interface ObjectFactory<T> {

    T getObject() throws BeansException;
}
