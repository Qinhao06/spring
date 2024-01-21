package qh.springframework.beans.factory.core;

import qh.springframework.common.Aware;

public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);

}
