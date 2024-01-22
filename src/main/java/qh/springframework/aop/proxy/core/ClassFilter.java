package qh.springframework.aop.proxy.core;

public interface ClassFilter {

    boolean matches(Class<?> clazz);

}
