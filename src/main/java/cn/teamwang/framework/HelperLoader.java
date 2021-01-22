package cn.teamwang.framework;

import cn.teamwang.framework.helper.*;
import cn.teamwang.framework.util.ClassUtil;

/**
 * 其实可以不用这个类
 *
 * @author Jacky
 * Date: 2020/12/11 5:54
 */
public class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AOPHelper.class,
                IOCHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}
