package cn.teamwang.framework.helper;

import cn.teamwang.framework.annotation.Autowired;
import cn.teamwang.framework.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * 为被 @Autowired 修饰的实例变量赋值
 *
 * @author Jacky
 * Date: 2020/12/11 2:14
 */
public class IOCHelper {

    public static final Logger logger = LoggerFactory.getLogger(IOCHelper.class);

    /*
     * 1. 遍历所有 Bean 实例
     * 2. 遍历 Bean 的 Field，并给被 @Autowired 修饰的 Field 成员变量赋值
     */
    static {
        BeanHelper.getBeanMap().forEach((beanClass, beanInstance) -> {
            for (Field field : beanClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    ReflectionUtil.setField(field.getType(), field, BeanHelper.getBean(field.getType()));
                }
            }
        });

    }
}
