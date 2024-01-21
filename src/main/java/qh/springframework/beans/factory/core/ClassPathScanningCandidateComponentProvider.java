package qh.springframework.beans.factory.core;

import cn.hutool.core.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

public class ClassPathScanningCandidateComponentProvider {

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {

        Set<BeanDefinition> definitionSet = new HashSet<>();
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for (Class<?> aClass : classes) {
            definitionSet.add(new BeanDefinition(aClass));
        }
        return definitionSet;
    }

}
