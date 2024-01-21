package qh.springframework.beans.factory.realize;

import qh.springframework.beans.core.io.DefaultResourceLoader;
import qh.springframework.beans.core.io.Resource;
import qh.springframework.beans.factory.core.*;

import java.io.IOException;
import java.util.Properties;

public class PropertyPlaceholderConfigurer implements BeanFactoryPostProcessor {


    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;

    public void setLocation(String location) {
        this.location = location;
    }

    public PropertyPlaceholderConfigurer(String location) {
        this.location = location;
    }

    public PropertyPlaceholderConfigurer() {
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());


            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanDefinitionName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);

                PropertyValues propertyValues = beanDefinition.getPropertyValues();
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    if(!(value instanceof String))continue;

                    String newValue = resolvePlaceholder((String) value, properties);
                    propertyValue.setValue(newValue);

                }

                StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
                beanFactory.addEmbeddedValueResolver(valueResolver);

            }

        } catch (IOException e) {
            throw new BeansException("load properties failed", e);
        }
    }

    private String resolvePlaceholder(String placeholder, Properties properties) {
        int startIndex = placeholder.indexOf(DEFAULT_PLACEHOLDER_PREFIX);
        int endIndex = placeholder.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
        if (startIndex == -1 || endIndex == -1 || startIndex > endIndex) {
            return placeholder;
        }
        String key = placeholder.substring(startIndex + DEFAULT_PLACEHOLDER_PREFIX.length(), endIndex);
        String value = properties.getProperty(key);
        if (value == null) {
            return placeholder;
        }
        return value;
    }

    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        @Override
        public String resolveStringValue(String strVal) {
            return PropertyPlaceholderConfigurer.this.resolvePlaceholder(strVal, properties);
        }

    }

}
