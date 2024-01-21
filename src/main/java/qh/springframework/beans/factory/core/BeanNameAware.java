package qh.springframework.beans.factory.core;

import qh.springframework.common.Aware;

public interface BeanNameAware extends Aware {

    void setBeanName(String beanName);

}
