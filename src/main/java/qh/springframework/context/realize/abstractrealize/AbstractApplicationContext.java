package qh.springframework.context.realize.abstractrealize;

import qh.springframework.factory.convert.core.ConversionService;
import qh.springframework.factory.convert.realize.DefaultConversionService;
import qh.springframework.factory.io.DefaultResourceLoader;
import qh.springframework.factory.bean.core.BeanFactoryPostProcessor;
import qh.springframework.factory.bean.core.BeanPostProcessor;
import qh.springframework.factory.bean.core.BeansException;
import qh.springframework.factory.bean.core.ConfigurableListableBeanFactory;
import qh.springframework.context.core.ConfigurableApplicationContext;
import qh.springframework.context.event.core.ApplicationEvent;
import qh.springframework.context.event.core.ApplicationEventMulticaster;
import qh.springframework.context.event.core.ApplicationListener;
import qh.springframework.context.event.realize.ContextCloseEvent;
import qh.springframework.context.event.realize.ContextRefreshedEvent;
import qh.springframework.context.event.realize.SimpleApplicationEventMulticaster;
import qh.springframework.context.realize.ApplicationContextAwareProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {


    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;

    @Override
    public void refresh() throws BeansException {

        refreshBeanFactory();

        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        invokeBeanFactoryPostProcessors(beanFactory);

        registerBeanPostProcessors(beanFactory);

        initApplicationEventMulticaster();

        registerListeners();

        finishBeanFactoryInitialization(beanFactory);

        finishRefresh();

    }


    protected abstract void refreshBeanFactory() throws BeansException;

    protected abstract ConfigurableListableBeanFactory  getBeanFactory();

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {

        Map<String, BeanFactoryPostProcessor> map = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);

        for (BeanFactoryPostProcessor beanFactoryPostProcessor : map.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {

        Map<String, BeanPostProcessor> map = beanFactory.getBeansOfType(BeanPostProcessor.class);

        for (BeanPostProcessor beanPostProcessor : map.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object... args) throws BeansException {
        return getBeanFactory().getBean(beanName, args);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getBeanFactory().getBeansOfType(type);
    }


    @Override
    public <T> T getBean(String beanName, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(beanName, requiredType);
    }

    @Override
    public boolean containsBean(String beanName) {
        return getBeanFactory().containsBean(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registrySingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);

    }

    protected void registerListeners() {
        List<ApplicationListener> applicationListeners = new ArrayList<>(getBeansOfType(ApplicationListener.class).values());
        for (ApplicationListener listener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }


    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {

        publishEvent(new ContextCloseEvent(this));

        getBeanFactory().destroySingletons();
    }

    private void finishRefresh(){
        publishEvent(new ContextRefreshedEvent(this));
    }

    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
        if (beanFactory.containsBean("conversionService")) {
            Object conversionService = beanFactory.getBean("conversionService");
            if (conversionService instanceof ConversionService) {
                beanFactory.setConversionService((ConversionService) conversionService);
            }
        }else {
            beanFactory.setConversionService(new DefaultConversionService());
        }

        beanFactory.preInstantiate();
    }




}
