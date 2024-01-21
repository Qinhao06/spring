package qh.springframework.beans.factory.core;

public interface SingletonBeanRegistry {

    void registrySingleton(String beanName, Object bean);

}
