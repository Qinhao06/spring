package qh.springframework.factory.bean.realize;

import qh.springframework.factory.bean.core.BeansException;
import qh.springframework.factory.bean.core.FactoryBean;
import qh.springframework.factory.bean.core.InitializingBean;
import qh.springframework.factory.convert.core.*;
import qh.springframework.factory.convert.realize.DefaultConversionService;
import qh.springframework.factory.convert.realize.GenericConversionService;

import java.util.Set;

public class ConversionServiceFactoryBean implements FactoryBean<ConversionService>, InitializingBean {

    private Set<?> converters;

    private GenericConversionService genericConversionService;


    @Override
    public ConversionService getObject() throws BeansException {
        return genericConversionService;
    }

    @Override
    public Class<?> getObjectType() {
        return genericConversionService.getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws BeansException {
        this.genericConversionService = new DefaultConversionService();
        registerConverter(converters, genericConversionService);
    }

    private void registerConverter(Set<?> converters, ConverterRegistry converterRegistry) {
        if (converters != null) {
            for (Object converter : converters) {
                if (converter instanceof GenericConverter) {
                   converterRegistry.addConverter((GenericConverter) converter);
                } else if (converter instanceof ConverterFactory) {
                    converterRegistry.addConverterFactory((ConverterFactory) converter);
                } else if (converter instanceof Converter) {
                   converterRegistry.addConverter((Converter) converter);
                }else {
                    throw new IllegalArgumentException("Each converter object must implement one of the " +
                            "Converter, ConverterFactory, or GenericConverter interface");
                }
            }
        }
    }

    public void setConverters(Set<?> converters) {
        this.converters = converters;
    }
}
