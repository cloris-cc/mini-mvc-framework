package cn.teamwang.framework.helper;

import cn.teamwang.framework.annotation.RequestMapping;
import cn.teamwang.framework.bean.Handler;
import cn.teamwang.framework.bean.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 映射 Request 请求和 Handler 处理器的关系
 *
 * @author Jacky
 * Date: 2020/12/11 4:12
 */
public class ControllerHelper {
    public static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

    /*
     * 1. 遍历所有Controller类
     * 2. 遍历Controller类的所有方法，获取带有@RequestMapping的方法
     */
    static {
        ClassHelper.getControllerClassSet().forEach(controllerClass -> {
            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    String value = method.getAnnotation(RequestMapping.class).value();
                    // 例如 GET:/users/...
                    if (value.matches("\\w+:/\\w*")) {
                        String[] array = value.split(":");

                        Request request = new Request(array[0], array[1]);
                        Handler handler = new Handler(controllerClass, method);
                        ACTION_MAP.put(request, handler);
                    }
                }
            }
        });

    }

    /**
     * 获取 Handler
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
