package qh.springframework.beans.factory.bean.realize;

import cn.hutool.core.util.StrUtil;
import qh.springframework.beans.factory.annotation.core.Component;
import qh.springframework.beans.factory.annotation.core.Scope;
import qh.springframework.beans.factory.annotation.realize.AutowiredAnnotationBeanPostProcessor;
import qh.springframework.beans.factory.bean.core.BeanDefinition;
import qh.springframework.beans.factory.bean.core.BeanDefinitionRegistry;
import qh.springframework.beans.factory.bean.core.ClassPathScanningCandidateComponentProvider;

import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                String beanScope = resolveBeanScope(candidateComponent);
                if(StrUtil.isNotEmpty(beanScope)){
                    candidateComponent.setScope(beanScope);
                }
                registry.registerBeanDefinition(determineBeanName(candidateComponent), candidateComponent);
            }

        }

        registry.registerBeanDefinition("qh.springframework.beans.factory.annotation.realize.AutowiredAnnotationBeanPostProcessor",
                new BeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
    }

    private String determineBeanName(BeanDefinition candidateComponent) {
        Class<?> beanClass = candidateComponent.getBeanDefinitionClass();
        Component component = beanClass.getAnnotation(Component.class);
        String value = component.value();
        if (StrUtil.isEmpty(value)) {
            value = StrUtil.lowerFirst(beanClass.getSimpleName());
        }
        return value;
    }

    private String resolveBeanScope(BeanDefinition candidateComponent) {
        Scope annotation = candidateComponent.getClass().getAnnotation(Scope.class);
        if(null != annotation){
            return annotation.value();
        }
        return StrUtil.EMPTY;
    }
}
