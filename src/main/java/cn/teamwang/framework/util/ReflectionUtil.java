package cn.teamwang.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类。
 * 用于实例化 Bean。
 *
 * @author Jacky
 * Date: 2020/12/9 21:19
 */
public class ReflectionUtil {

    public static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建实例
     */
    public static Object newInstance(Class<?> cls) {
        try {
            return cls.newInstance();
        } catch (Exception e) {
            logger.error("{} new instance failure: ", cls, e);
        }
        return null;
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Object... args) {
        method.setAccessible(true);
        try {
            return method.invoke(obj, args);
        } catch (Exception e) {
            logger.error("{} invoke method failure: ", obj, e);
        }
        return null;
    }

    /**
     * 设置成员变量的值??
     */
    public static void setField(Object obj, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(obj, value);
        } catch (Exception e) {
            logger.error("{} set field failure: ", obj, e);
        }
    }


}
