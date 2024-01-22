package qh.springframework.aop.advice.core;

import qh.springframework.aop.advice.core.AfterAdvice;

import java.lang.reflect.Method;

public interface MethodAfterAdvice extends AfterAdvice {

    void after(Object target, Method method, Object[] args, Object result);

}
