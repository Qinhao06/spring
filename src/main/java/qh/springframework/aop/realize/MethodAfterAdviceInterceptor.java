package qh.springframework.aop.realize;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import qh.springframework.aop.core.MethodAfterAdvice;

public class MethodAfterAdviceInterceptor implements MethodInterceptor {

    private MethodAfterAdvice advice;

    public MethodAfterAdviceInterceptor() {
    }

    public MethodAfterAdviceInterceptor(MethodAfterAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        Object proceed = methodInvocation.proceed();
        advice.after(methodInvocation.getThis(), methodInvocation.getMethod(), methodInvocation.getArguments(), proceed);
        return proceed;
    }
}
