package qh.springframework.aop.realize;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import qh.springframework.aop.core.MethodAroundAdvice;

public class MethodAroundAdviceInterceptor implements MethodInterceptor {

    private MethodAroundAdvice advice;

    public MethodAroundAdviceInterceptor() {
    }

    public MethodAroundAdviceInterceptor(MethodAroundAdvice advice) {
        this.advice = advice;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        advice.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
        Object proceed = methodInvocation.proceed();
        advice.after(methodInvocation.getThis(), methodInvocation.getMethod(), methodInvocation.getArguments(), proceed);
        return proceed;
    }
}
