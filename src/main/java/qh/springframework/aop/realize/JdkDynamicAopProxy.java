package qh.springframework.aop.realize;



import qh.springframework.aop.core.AdvisedSupport;
import qh.springframework.aop.core.AopProxy;
import org.aopalliance.intercept.MethodInterceptor;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advisedSupport;

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(advisedSupport.getMethodMatcher().
                matches(method, advisedSupport.getTargetSource().getTarget().getClass())){
            MethodInterceptor methodInterceptor = advisedSupport.getMethodInterceptor();
            return methodInterceptor.invoke(new
                    ReflectiveMethodInvocation(advisedSupport.getTargetSource().getTarget(), method, args));
        }
        return method.invoke(advisedSupport.getTargetSource().getTarget(), args);
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                advisedSupport.getTargetSource().getTargetInterface(), this);
    }
}
