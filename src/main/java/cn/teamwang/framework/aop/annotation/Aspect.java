package cn.teamwang.framework.aop.annotation;

import java.lang.annotation.*;

/**
 * @author Jacky
 * Date: 2020/12/13 1:54
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    Class<? extends Annotation> value();
}
