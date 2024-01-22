package qh.springframework.beans.factory.annotation.core;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Qualifier {

    String value();
}
