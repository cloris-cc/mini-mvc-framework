package cn.teamwang.framework.helper;

import cn.teamwang.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 初始化 Bean (实例)容器
 *
 * @author Jacky
 * Date: 2020/12/11 1:18
 */
public class BeanHelper {

    public static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    static {
        ClassHelper.getBeanClassSet().forEach(i -> {
            BEAN_MAP.put(i, ReflectionUtil.newInstance(i));
        });
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get bean by class: " + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }

}
