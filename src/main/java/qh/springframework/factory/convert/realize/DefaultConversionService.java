package qh.springframework.factory.convert.realize;

import qh.springframework.factory.convert.core.ConverterRegistry;

public class DefaultConversionService extends GenericConversionService{

    public DefaultConversionService() {
        addDefaultConverter(this);
    }

    public static void addDefaultConverter(ConverterRegistry converterRegistry){
        converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
        converterRegistry.addConverter(new StringToLocalDateConverter("yyyy-MM-dd"));
    }

}
