package qh.springframework.aop.proxy.realize;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import qh.springframework.aop.advice.core.AdvisedSupport;
import qh.springframework.aop.advice.core.Advisor;
import qh.springframework.aop.advice.core.Pointcut;
import qh.springframework.aop.advice.core.TargetSource;
import qh.springframework.aop.advice.realize.AspectJExpressionPointcutAdvisor;
import qh.springframework.aop.proxy.core.ClassFilter;
import qh.springframework.aop.proxy.core.InstantiationAwareBeanPostProcessor;
import qh.springframework.beans.factory.bean.core.BeanFactory;
import qh.springframework.beans.factory.bean.core.BeanFactoryAware;
import qh.springframework.beans.factory.bean.core.BeansException;
import qh.springframework.beans.factory.bean.core.PropertyValues;
import qh.springframework.beans.factory.bean.realize.DefaultListableBeanFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;


    private final Set<Object> earlyProxyReferences = Collections.synchronizedSet(new HashSet<Object>());

    public DefaultAdvisorAutoProxyCreator() {
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(!earlyProxyReferences.contains(beanName)){
            return wrapIfNecessary(bean,beanName);
        }
        return bean;

    }

    protected Object wrapIfNecessary(Object bean, String beanName) throws BeansException {
        if(isInfrastructureClass(bean.getClass())) {
            return bean;
        }

        Collection<AspectJExpressionPointcutAdvisor> values = beanFactory.
                getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();

        for (AspectJExpressionPointcutAdvisor advisor : values) {
            ClassFilter classFilter = advisor.getPointcut().getClassFilter();
            if(!classFilter.matches(bean.getClass())) continue;

            AdvisedSupport advisedSupport = new AdvisedSupport();

            advisedSupport.setTargetSource(new TargetSource(bean));
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());
            advisedSupport.setMethodMatcher(advisor.getPointcut().getMethodMatcher());
            advisedSupport.setProxyTargetClass(false);
            return new ProxyFactory(advisedSupport).getProxy();

        }
        return bean;
    }



    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;

    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }
}
