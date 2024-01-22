package qh.springframework.aop.advice.core;

import qh.springframework.aop.proxy.core.ClassFilter;
import qh.springframework.aop.proxy.core.MethodMatcher;

public interface Pointcut {

    ClassFilter getClassFilter();

    MethodMatcher getMethodMatcher();

}
