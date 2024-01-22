package qh.springframework.factory.convert.realize;

import qh.springframework.factory.convert.core.Converter;
import qh.springframework.factory.convert.core.ConverterFactory;
import qh.springframework.utils.NumberUtils;

public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {

    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToIntegerConverter<>(targetType);
    }

    private final static class StringToIntegerConverter<T extends Number> implements Converter<String, T> {

        private final Class<T> targetType;

        public StringToIntegerConverter(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if(source.isEmpty()){
                return null;
            }
            return NumberUtils.parseNumber(source, this.targetType);
        }
    }

}

