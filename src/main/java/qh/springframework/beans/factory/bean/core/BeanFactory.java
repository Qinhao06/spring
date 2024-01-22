package qh.springframework.beans.factory.bean.core;

public interface BeanFactory {

    Object getBean(String beanName) throws BeansException;

    Object getBean(String beanName, Object ...args) throws BeansException;

    <T> T getBean(String beanName, Class<T> requiredType) throws BeansException;

    <T> T getBean(Class<T> requiredType) throws BeansException;

}
