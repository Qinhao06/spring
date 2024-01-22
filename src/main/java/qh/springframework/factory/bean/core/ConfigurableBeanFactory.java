package qh.springframework.factory.bean.core;

import org.jetbrains.annotations.Nullable;
import qh.springframework.factory.convert.core.ConversionService;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";

    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void destroySingletons();

    void addEmbeddedValueResolver(StringValueResolver resolver);

    String resolveEmbeddedValue(String value);

    void setConversionService(ConversionService conversionService);

    @Nullable
    ConversionService getConversionService();


}
