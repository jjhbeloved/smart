package org.smart4j.framework.helper;

import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/29.
 */
public final class ControllerHelper {

    private static final Map<Request, Handler> ACTION_MAP = Maps.newLinkedHashMap();

    static {
        Set<Class<?>> classes = ClassHelper.getControllerClassSet();
        for (Class<?> cls : classes) {
            Method[] methods = cls.getMethods();
            if (ArrayUtils.isEmpty(methods)) {
                continue;
            }
            for (Method method : methods) {
                if (method.isAnnotationPresent(Action.class)) {
                    Action action = method.getAnnotation(Action.class);
                    String mapping = action.value();
                    if (!mapping.matches("\\w+:/\\w*")) {
                        continue;
                    }
                    String[] values = mapping.split(":");
                    if (ArrayUtils.isNotEmpty(values) && values.length == 2) {
                        Request request = new Request(values[0], values[1]);
                        Handler handler = new Handler(cls, method);
                        ACTION_MAP.put(request, handler);
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
