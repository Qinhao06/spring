package qh.springframework.beans.factory.realize.abstractrelize;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import qh.springframework.aop.core.InstantiationAwareBeanPostProcessor;
import qh.springframework.beans.factory.core.*;
import qh.springframework.beans.factory.realize.SimpleInstantiationStrategy;
import qh.springframework.beans.utils.ClassUtils;
import qh.springframework.common.Aware;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 该类用于实现 bean 的创建
 */
public abstract class AbstractAutowiredCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    public Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;
        try {

            bean = createBeanInstance(beanName, beanDefinition, args);

            if(beanDefinition.isSingleton()){
                Object finalBean = bean;
                addSingletonFactory(beanName, ()-> getEarlyBeanReference(beanName, beanDefinition, finalBean));
            }


            boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstantiation(beanName, bean);
            if (!continueWithPropertyPopulation) {
                return bean;
            }

            applyBeanPostProcessorsBeforeApplyingPropertyValues(beanName, bean, beanDefinition);

            applyPropertyValues(bean, beanDefinition);

            bean = initializeBean(beanName, bean, beanDefinition);
        }catch (Exception e){
            e.printStackTrace();
            throw new BeansException("create bean failed [" + beanName + "]", e);
        }

        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);


        Object exposedObject = bean;
        if(beanDefinition.isSingleton()){
            exposedObject = getSingleton(beanName);
            registrySingleton(beanName, exposedObject);
        }

        return exposedObject;
    }

    private boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
        boolean continueWithPropertyPopulation = true;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                boolean b = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessAfterInstantiation(bean, beanName);
                if(!b){
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
        return continueWithPropertyPopulation;
    }

    private void applyBeanPostProcessorsBeforeApplyingPropertyValues(String beanName, Object bean, BeanDefinition beanDefinition) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                PropertyValues propertyValues = ((InstantiationAwareBeanPostProcessor) beanPostProcessor)
                        .postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);

            }
        }
    }


    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {

        if(bean instanceof Aware){
            if(bean instanceof BeanNameAware){
                ((BeanNameAware) bean).setBeanName(beanName);
            }
            if(bean instanceof BeanClassLoaderAware){
                ((BeanClassLoaderAware) bean).setBeanClassLoader(ClassUtils.getDefaultClassLoader());
            }
            if(bean instanceof BeanFactoryAware){
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }

        }



        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        invokeInitMethods(beanName, wrappedBean, beanDefinition);

        return applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

    }


    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) throws BeansException {
        Object exposedObject = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if(beanPostProcessor instanceof InstantiationAwareBeanPostProcessor){
                exposedObject = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).getEarlyBeanReference(exposedObject, beanName);
                if(null == exposedObject) return null;
            }
        }
        return exposedObject;
    }


    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {

        if(!beanDefinition.isSingleton()){
            return;
        }

        if(bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())){
            registerDisposableBean(beanName, new DisposalBeanAdapter(bean, beanName, beanDefinition.getDestroyMethodName()) );
        }
    }


    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) throws BeansException{

        if(wrappedBean instanceof InitializingBean){
                ((InitializingBean) wrappedBean).afterPropertiesSet();
        }
        String initMethod = beanDefinition.getInitMethodName();
        Method method = null;
        if(StrUtil.isNotEmpty(initMethod)){
            try {
                method = beanDefinition.getBeanDefinitionClass().getMethod(initMethod);
                method.invoke(wrappedBean);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new BeansException("no such init-method named + " + initMethod + "in bean named " + beanName, e);
            }
        }
    }

    protected Object createBeanInstance(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
        Object bean = null;
        Constructor ctor = null;
        Class beanDefinitionClass = beanDefinition.getBeanDefinitionClass();
        Constructor[] declaredConstructors = beanDefinitionClass.getDeclaredConstructors();
        for (Constructor declaredConstructor : declaredConstructors) {
            if(null != args && args.length == declaredConstructor.getParameterTypes().length){
                ctor = declaredConstructor;
                break;
            }
        }
        return instantiationStrategy.instantiate(beanDefinition, beanName, ctor, args);
    }

    protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws BeansException {

        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            if(null != propertyValues){
                for(PropertyValue propertyValue : propertyValues.getPropertyValues()){

                    String propertyName = propertyValue.getName();
                    Object propertyValueObject = propertyValue.getValue();

                    if(propertyValueObject instanceof BeanReference){
                        BeanReference beanReference = (BeanReference) propertyValueObject;
                        propertyValueObject = getBean(beanReference.getName());
                    }

                    BeanUtil.setFieldValue(bean, propertyName, propertyValueObject);

                }
            }
        }
        catch (Exception e){
            throw new BeansException("apply property values failed [" + bean + "]", e);
        }
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object currentBean = beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
            if (currentBean == null) {
                return result;
            }
            result = currentBean;
        }
        return result;
    }


    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object currentBean = beanPostProcessor.postProcessAfterInitialization(bean, beanName);
            if (currentBean == null) {
                return result;
            }
            result = currentBean;
        }
        return result;
    }
}
