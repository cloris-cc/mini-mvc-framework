package cn.teamwang.framework.aop.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Jacky
 * Date: 2020/12/14 0:37
 */
public class ProxyUtil {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(final Class<?> targetClass, final List<ProxyInterceptor> proxyInterceptorList) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetClass, obj, method, methodProxy, args, proxyInterceptorList)
                        .invoke();
            }
        });
    }

}
