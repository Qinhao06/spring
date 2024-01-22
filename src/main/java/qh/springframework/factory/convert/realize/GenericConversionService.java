package qh.springframework.factory.convert.realize;

import org.jetbrains.annotations.Nullable;
import qh.springframework.factory.convert.core.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class GenericConversionService implements ConversionService, ConverterRegistry {

    private final Map<GenericConverter.ConvertiblePair, GenericConverter> convertersMap = new HashMap<>();


    @Override
    public void addConverter(Converter<?, ?> converter) {
        GenericConverter.ConvertiblePair requireTypeInfo = getRequireTypeInfo(converter);
        ConverterAdapter converterAdapter = new ConverterAdapter(requireTypeInfo, converter);
        for (GenericConverter.ConvertiblePair convertibleType : converterAdapter.getConvertibleTypes()) {
            convertersMap.put(convertibleType, converterAdapter);
        }
    }

    @Override
    public void addConverter(GenericConverter converter) {
        for (GenericConverter.ConvertiblePair convertibleType : converter.getConvertibleTypes()) {
            convertersMap.put(convertibleType, converter);
        }
    }

    @Override
    public void addConverterFactory(ConverterFactory<?, ?> converterFactory) {
        GenericConverter.ConvertiblePair requireTypeInfo = getRequireTypeInfo(converterFactory);
        ConverterFactoryAdapter converterFactoryAdapter = new ConverterFactoryAdapter(requireTypeInfo, converterFactory);
        for (GenericConverter.ConvertiblePair convertibleType : converterFactoryAdapter.getConvertibleTypes()) {
            convertersMap.put(convertibleType, converterFactoryAdapter);
        }
    }

    @Override
    public boolean canConvert(@Nullable Class<?> sourceType, Class<?> targetType) {
        GenericConverter converter = getConverter(sourceType, targetType);
        return converter != null;
    }

    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        Class<?> sourceType = source.getClass();
        GenericConverter converter = getConverter(sourceType, targetType);
        return (T) converter.convert(source, targetType);
    }

    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType){
        List<Class<?>> sourceTypeHierarchy = getClassHierarchy(sourceType);
        List<Class<?>> targetTypeHierarchy = getClassHierarchy(targetType);
        for (Class<?> aClass : sourceTypeHierarchy) {
            for (Class<?> bClass : targetTypeHierarchy) {
                GenericConverter.ConvertiblePair convertiblePair = new GenericConverter.ConvertiblePair(aClass, bClass);
                GenericConverter genericConverter = convertersMap.get(convertiblePair);
                if(genericConverter != null){
                    return genericConverter;
                }
            }
        }
        return null;

    }

    private List<Class<?>> getClassHierarchy(Class<?> clazz) {
        List<Class<?>> classes = new ArrayList<>();
        while (clazz != null) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return classes;
    }

    protected GenericConverter.ConvertiblePair getRequireTypeInfo(Object object){
        Type genericInterface = object.getClass().getGenericInterfaces()[0];

        ParameterizedType pType = (ParameterizedType) genericInterface;
        Type[] actualTypeArguments = pType.getActualTypeArguments();
        Class sourceType = (Class) actualTypeArguments[0];
        Class targetType = (Class) actualTypeArguments[1];

        return new GenericConverter.ConvertiblePair(sourceType, targetType);

    }




    private final class ConverterAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final Converter<Object, Object> convert;

        private ConverterAdapter(ConvertiblePair typeInfo, Converter<?, ?> convert) {
            this.typeInfo = typeInfo;
            this.convert = (Converter<Object, Object>) convert;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class<?> targetType) {
            return convert.convert(source);
        }
    }

    private final class ConverterFactoryAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final ConverterFactory<Object, Object> converterFactory;

        private ConverterFactoryAdapter(ConvertiblePair typeInfo, ConverterFactory<?, ?> convert) {
            this.typeInfo = typeInfo;
            this.converterFactory = (ConverterFactory<Object, Object>) convert;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class<?> targetType) {
            return converterFactory.getConverter(targetType).convert(source);
        }

    }

}
