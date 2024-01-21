package qh.springframework.beans.factory.core;

import cn.hutool.core.util.StrUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DisposalBeanAdapter implements DisposableBean{

    private final Object bean;

    private final String beanName;

    private String destroyMethodName;

    public DisposalBeanAdapter(Object bean, String beanName, String destroyMethodName) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = destroyMethodName;
    }

    @Override
    public void destroy() throws BeansException {
        if(this.bean instanceof DisposableBean){
            ((DisposableBean) this.bean).destroy();
        }

        if(StrUtil.isNotEmpty(destroyMethodName) && !(bean instanceof DisposableBean)){
            Method destroyMethod = null;
            try {
               destroyMethod =  bean.getClass().getMethod(destroyMethodName);
               destroyMethod.invoke(bean);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new BeansException("Couldn't find a destroy method named '" + destroyMethodName + "' on bean with name '" + beanName + "'");
            }
        }
    }
}
