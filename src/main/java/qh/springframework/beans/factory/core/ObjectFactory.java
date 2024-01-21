package qh.springframework.beans.factory.core;

public interface ObjectFactory<T> {

    T getObject() throws BeansException;
}
