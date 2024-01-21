package qh.springframework.beans.factory.core;

public interface AutowireCapableBeanFactory extends BeanFactory{

    Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) throws BeansException;


    Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) throws BeansException;


}
