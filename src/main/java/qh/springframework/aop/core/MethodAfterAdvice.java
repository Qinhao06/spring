package qh.springframework.aop.core;

import java.lang.reflect.Method;

public interface MethodAfterAdvice extends AfterAdvice{

    void after(Object target, Method method, Object[] args, Object result);

}
