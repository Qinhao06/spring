package qh.springframework.factory.convert.core;

public interface ConverterFactory <S, R>{

    <T extends R> Converter<S, T> getConverter(Class<T> targetType);

}
