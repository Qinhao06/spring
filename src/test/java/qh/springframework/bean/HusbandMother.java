package qh.springframework.bean;

import qh.springframework.beans.factory.bean.core.BeansException;
import qh.springframework.beans.factory.bean.core.FactoryBean;

import java.lang.reflect.Proxy;

public class HusbandMother implements FactoryBean<IMother> {

    @Override
    public IMother getObject() throws BeansException {
        return (IMother) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{IMother.class}, (proxy, method, args) -> "婚后媳妇妈妈的职责被婆婆代理了！" + method.getName());
    }

    @Override
    public Class<?> getObjectType() {
        return IMother.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}