package cn.teamwang.framework.bean;

import java.lang.reflect.Method;

/**
 * @author Jacky
 * Date: 2020/12/11 4:11
 */
public class Handler {

    // Controller 类
    private final Class<?> controllerClass;

    // Action 方法
    private final Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
