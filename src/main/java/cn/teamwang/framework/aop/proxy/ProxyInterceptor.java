package cn.teamwang.framework.aop.proxy;

/**
 * @author Jacky
 * Date: 2020/12/13 2:16
 */
public interface ProxyInterceptor {
    /**
     * 由用户来实现的切面方法。
     * <p>
     * 按顺序执行链式代理
     */
    Object intercept(ProxyChain proxyChain) throws Throwable;
}
