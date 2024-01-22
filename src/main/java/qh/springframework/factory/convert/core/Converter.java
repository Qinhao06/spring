package qh.springframework.factory.convert.core;

public interface Converter <S, T>{
    T convert(S source);
}
