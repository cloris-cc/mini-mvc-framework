package cn.teamwang.framework.aop.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;


/**
 * @author Jacky
 * Date: 2020/12/14 2:10
 */
public abstract class AspectProxy implements ProxyInterceptor {

    public static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);

    @Override
    public Object intercept(ProxyChain proxyChain) throws Throwable {
        Object result;
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] args = proxyChain.getMethodParams();

        begin();
        try {
            if (validate()) {
                before(cls, method, args);
                result = proxyChain.invoke();
                after(cls, method, args, result);
            } else {
                result = proxyChain.invoke();
            }
        } catch (Exception e) {
            LOGGER.error("proxy failure: ", e);
            error(cls, method, args, e);
            throw e;
        } finally {
            end();
        }

        return result;
    }

    public boolean validate() {
        return true;
    }

    public void begin() {

    }

    public void before(Class<?> cls, Method method, Object[] args) {

    }

    public void after(Class<?> cls, Method method, Object[] args, Object result) throws Throwable {

    }

    public void error(Class<?> cls, Method method, Object[] args, Throwable e) {

    }

    public void end() {

    }


}
