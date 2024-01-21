package qh.springframework.beans.utils;


public class ClassUtils {
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(cl == null){
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null) && (isCglibProxyClassName(clazz.getName()));
    }

    public static boolean isCglibProxyClassName(String name) {
        return (name != null) && name.contains("$$");
    }
}
