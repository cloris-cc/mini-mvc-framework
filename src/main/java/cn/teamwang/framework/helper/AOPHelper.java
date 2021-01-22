package cn.teamwang.framework.helper;

import cn.teamwang.framework.annotation.Service;
import cn.teamwang.framework.aop.annotation.Aspect;
import cn.teamwang.framework.aop.proxy.AspectProxy;
import cn.teamwang.framework.aop.proxy.ProxyInterceptor;
import cn.teamwang.framework.aop.proxy.ProxyUtil;
import cn.teamwang.framework.transcation.TransactionProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 处理 目标类、切面类 & 创建代理类实例 的关系。
 * 最终以代理类实例覆盖IOC容器中的目标类实例。（另外，该类的初始化时间应在IOC容器初始化之后，才能实现覆盖）
 *
 * @author Jacky
 * Date: 2020/12/14 4:08
 */
public class AOPHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AOPHelper.class);

    static {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            // 目标类：代理
            Map<Class<?>, List<ProxyInterceptor>> targetMap = createTargetMap(proxyMap);

            for (Map.Entry<Class<?>, List<ProxyInterceptor>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<ProxyInterceptor> proxyList = targetEntry.getValue();
                // 创建代理类实例
                Object proxyInstance = ProxyUtil.getProxy(targetClass, proxyList);
                // 替换IOC容器中targetClass的实例为代理类实例
                BeanHelper.setBean(targetClass, proxyInstance);
            }
        } catch (Exception e) {
            LOGGER.error("aop failure", e);
        }
    }



    /**
     * (一个)切面代理类 : (多个)目标类
     * 例如：ControllerAspect切面代理类 和 所有的(value=)Controller目标类
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() {
        // 代理类和(多个)目标类的映射集合
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
        // proxySet: 获取所有继承了AspectProxy的切面类（即 AspectProxy 的子类）
        Set<Class<?>> proxySet = ClassHelper.getClassSetBySuper(AspectProxy.class);

        // 获取目标类
        for (Class<?> proxyClass : proxySet) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                // 获取 @Aspect 注解中的 value，再获取被 value 修饰的所有类。
                // 以 ControllerAspect 为例，目标类就是 @Aspect(Controller.class) 的 value
                proxyMap.put(proxyClass, ClassHelper.getClassSetByAnnotation(proxyClass.getAnnotation(Aspect.class).value()));
            }
        }

        addTransactionProxy(proxyMap);
        return proxyMap;
    }

    /**
     * 因为创建代理类实例需要targetClass，proxyInterceptorList参数，所以需要在proxyMap的基础上建立新的映射关系。
     */
    private static Map<Class<?>, List<ProxyInterceptor>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception {
        Map<Class<?>, List<ProxyInterceptor>> targetMap = new HashMap<>();

        for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            // 代理类
            Class<?> proxyClass = proxyEntry.getKey();
            // n 个目标类
            Set<Class<?>> targetClassSet = proxyEntry.getValue();

            for (Class<?> targetCls : targetClassSet) {
                // 创建代理实例
                ProxyInterceptor proxyInterceptor = (ProxyInterceptor) proxyClass.newInstance();
                if (targetMap.containsKey(targetCls)) {
                    targetMap.get(targetCls).add(proxyInterceptor);
                } else {
                    List<ProxyInterceptor> temp = new ArrayList<>();
                    temp.add(proxyInterceptor);
                    targetMap.put(targetCls, temp);
                }
            }
        }

        return targetMap;
    }

    private static void addTransactionProxy(Map<Class<?>, Set<Class<?>>> proxyMap) {
        Set<Class<?>> serviceClassSet = ClassHelper.getClassSetByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class, serviceClassSet);
    }

}
