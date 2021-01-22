package cn.teamwang.framework.aop.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Jacky
 * Date: 2020/12/13 2:23
 */
public class ProxyChain {
    // target: class, obj, method, args...
    // proxy: proxyList, methodProxy...
    private final Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] methodParams;
    private final List<ProxyInterceptor> proxyInterceptorList;

    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<ProxyInterceptor> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyInterceptorList = proxyList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Object invoke() throws Throwable {
        Object methodResult;

        if (proxyIndex < proxyInterceptorList.size()) {
            methodResult = proxyInterceptorList.get(proxyIndex++).intercept(this);
        } else {
            // 执行完用户实现的所有 intercept() 后，最终调用目标对象的业务逻辑方法。
            methodResult = methodProxy.invokeSuper(targetObject, methodParams);
        }

        return methodResult;
    }
}
