package qh.springframework.factory.bean.core;

public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);

}
