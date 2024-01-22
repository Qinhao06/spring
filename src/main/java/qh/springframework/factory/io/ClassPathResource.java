package qh.springframework.factory.io;

import cn.hutool.core.lang.Assert;

import qh.springframework.utils.ClassUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ClassPathResource implements Resource{

    private final String path;

    private ClassLoader classLoader;

    public ClassPathResource(String path) {
        this(path, (ClassLoader) null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "Path can not be null");
        this.path = path;
        this.classLoader = classLoader == null ? ClassUtils.getDefaultClassLoader() : classLoader;
    }

    @Override
    public InputStream getInputStream() throws IOException {

        InputStream is = classLoader.getResourceAsStream(path);

        if(is == null){
            throw new FileNotFoundException(path + " not found, due to it does not exist");
        }

        return is;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
