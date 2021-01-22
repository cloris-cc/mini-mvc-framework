package cn.teamwang.framework;

import cn.teamwang.framework.bean.Handler;
import cn.teamwang.framework.bean.RequestParam;
import cn.teamwang.framework.bean.ResponseData;
import cn.teamwang.framework.bean.View;
import cn.teamwang.framework.helper.BeanHelper;
import cn.teamwang.framework.helper.ConfigHelper;
import cn.teamwang.framework.helper.ControllerHelper;
import cn.teamwang.framework.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 调度器(Servlet)
 *
 * @author Jacky
 * Date: 2020/12/11 18:36
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        HelperLoader.init();
        ServletContext context = config.getServletContext();
        // 注册处理 JSP 的 Servlet
        context.getServletRegistration("jsp").addMapping(ConfigHelper.getAppJspPath() + "*");
        // 注册处理静态资源的 Servlet
        context.getServletRegistration("default").addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    /**
     * 1. (DispatcherServlet) 获取 req 的请求方法(GET/POST..)、请求路径(URL)和请求参数
     * 2. 404 (HandlerMapping + HandlerAdapter是什么) 通过 Key-“GET:/users/...”获取对应 Handler，再将上述参数传入调用 method
     * 3. (ViewResolver) 返回视图或数据
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.service(req, resp);
        String reqMethod = req.getMethod().toLowerCase();
        String reqPath = req.getPathInfo();

        // 暂不处理 404
        Handler handler = ControllerHelper.getHandler(reqMethod, reqPath);

        // 获取请求参数 params
        Map<String, Object> paramMap = new HashMap<>();
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String key = paramNames.nextElement();
            Object val = req.getParameter(key);
            paramMap.put(key, val);
        }
        // 获取请求参数 body （表单也是body的一种）
        String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
        if (StringUtil.isNotEmpty(body)) {
            String[] kvs = StringUtil.splitString(body, "&");
            if (ArrayUtil.isNotEmpty(kvs)) {
                for (String kv : kvs) {
                    String[] array = StringUtil.splitString(kv, "=");
                    if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                        String fieldName = array[0];
                        String fieldValue = array[1];
                        paramMap.put(fieldName, fieldValue);
                    }
                }
            }
        }

        // 调用方法
        Object result;
        if (paramMap.isEmpty()) {
            result = ReflectionUtil.invokeMethod(
                    BeanHelper.getBean(handler.getControllerClass()),
                    handler.getActionMethod());
        } else {
            result = ReflectionUtil.invokeMethod(
                    BeanHelper.getBean(handler.getControllerClass()),
                    handler.getActionMethod(), new RequestParam(paramMap));
        }

        // 处理返回值
        if (result instanceof View) {
            // 返回 JSP 页面
            View view = (View) result;
            String jspPath = view.getPath();
            if (StringUtil.isNotEmpty(jspPath)) {
                if (jspPath.startsWith("/")) {
                    // 页面重定向
                    resp.sendRedirect(req.getContextPath() + jspPath);
                } else {
                    // 页面转发
                    for (Map.Entry<String, Object> entry : view.getModel().entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                    req.getRequestDispatcher(ConfigHelper.getAppJspPath() + jspPath).forward(req, resp);
                }
            }

        } else if (result instanceof ResponseData) {
            // 返回 JSON 数据
            resp.setContentType("application/json");
            resp.setCharacterEncoding("utf-8");

            // 将响应的 json 数据写入 response 的 writer 中
            String jsonString = JsonUtil.toJson(((ResponseData) result).getModel());
            PrintWriter writer = resp.getWriter();
            writer.write(jsonString);
            writer.flush();
            writer.close();
        }

    }


}