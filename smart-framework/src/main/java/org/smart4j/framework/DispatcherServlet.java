package org.smart4j.framework;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.Request;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.util.CodeUtil;
import org.smart4j.framework.util.JsonUtil;
import org.smart4j.framework.util.ReflectionUtil;
import org.smart4j.framework.util.StreamUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

/**
 * MVC 核心方法
 *
 * @author: YANGXUAN223
 * @date: 2018/11/29.
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 初始化helper
        HelperLoader.init();
        // 用于注册 servlet
        ServletContext servletContext = config.getServletContext();
        // 获取处理 JSP 的 servlet 加入定义的 jsp 地址
        servletContext.getServletRegistration("jsp").addMapping(ConfigHelper.getAppJspPath() + "*");
        // 获取处理 default 的 servlet 加入定义的 css 地址
        servletContext.getServletRegistration("default").addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod = req.getMethod().toLowerCase();
        String path = req.getPathInfo();
        Handler handler = ControllerHelper.getHandler(requestMethod, path);
        if (handler == null) {
            return;
        }
        Class<?> controllerClass = handler.getControllerClass();
        Object controllerBean = BeanHelper.getBean(controllerClass);
        Map<String, Object> params = Maps.newLinkedHashMap();
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = req.getParameter(paramName);
            params.put(paramName, paramValue);
        }
        // servlet 请求存在 转码问题
        String body = CodeUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
        // TODO 这里有一个对body的解析
        Param param = new Param(params);
        Method method = handler.getMethod();
        Object result;
        if (param.isEmpty()) {
            result = ReflectionUtil.invokeMethod(controllerBean, method);
        } else {
            result = ReflectionUtil.invokeMethod(controllerBean, method, param);
        }
        if (result instanceof View) {
            View view = (View) result;
            String destPath = view.getPath();
            if (StringUtils.isBlank(destPath)) {
                return;
            }
            if (destPath.startsWith("/")) {
                String contextPath = req.getContextPath();
                // sendRedirect 发送302状态码给客户端, 重定向, 客户端会改变地址
                resp.sendRedirect(contextPath + destPath);
            } else {
                view.getModels().forEach(req::setAttribute);
                req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path)
                        // forward 客户端地址不会改变, 只是服务端请求资源
                        .forward(req, resp);
            }
            return;
        }
        if (result instanceof Data) {
            Data data = (Data) result;
            Object model = data.getModel();
            if (model == null) {
                return;
            }
            resp.setContentType(ConfigConstant.JSON_CONTENT_TYPE);
            resp.setCharacterEncoding(ConfigConstant.ENC_UTF_8);
            try (PrintWriter writer = resp.getWriter()) {
                writer.write(JsonUtil.toJson(model));
                writer.flush();
            }
        }
    }
}
