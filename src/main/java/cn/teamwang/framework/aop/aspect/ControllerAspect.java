package cn.teamwang.framework.aop.aspect;

import cn.teamwang.framework.annotation.Controller;
import cn.teamwang.framework.aop.annotation.Aspect;
import cn.teamwang.framework.aop.proxy.AspectProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author Jacky
 * Date: 2020/12/14 3:30
 */
@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {
    public static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);
    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] args) {
//        super.before(cls, method, args);
        LOGGER.debug("ControllerAspect 的 before 方法...");
        LOGGER.debug("class: {}", cls.getName());
        LOGGER.debug("method: {}", method.getName());
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] args, Object result) throws Throwable {
//        super.after(cls, method, args, result);
        LOGGER.debug("ControllerAspect 的 after 方法...");
        LOGGER.debug("time: {}", System.currentTimeMillis() - begin);
    }
}
