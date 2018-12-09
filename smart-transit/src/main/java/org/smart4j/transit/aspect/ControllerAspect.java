package org.smart4j.transit.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.proxy.AbstractAspectProxy;

import java.lang.reflect.Method;

/**
 * @author: YANGXUAN223
 * @date: 2018/12/6.
 */
@Aspect(value = Controller.class)
public class ControllerAspect extends AbstractAspectProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAspectProxy.class);

    private long begin;

    @Override
    public void before(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOGGER.info("-------- begin --------");
        LOGGER.info(String.format("class: %s", cls.getName()));
        LOGGER.info(String.format("method: %s", method.getName()));
        begin = System.currentTimeMillis();
    }

    @Override
    public void after(Class<?> cls, Method method, Object[] params) throws Throwable {
        LOGGER.info(String.format("time: %dms", System.currentTimeMillis() - begin));
        LOGGER.info("-------- end --------");
    }

    @Override
    public void error(Class<?> cls, Method method, Object[] params, Throwable throwable) {

    }
}
