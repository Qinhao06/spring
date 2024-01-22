package qh.springframework.beans.factory.bean.core;

public interface ObjectFactory<T> {

    T getObject() throws BeansException;
}
