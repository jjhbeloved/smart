package org.smart4j.framework;

import com.google.common.collect.Lists;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.helper.AopHelper;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.helper.IocHelper;
import org.smart4j.framework.util.ClassUtil;

import java.util.List;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/29.
 */
public class HelperLoader {

    /**
     * 初始化工具
     */
    public static void init() {
        List<Class<?>> classes = Lists.newArrayList(
                // 先加载 class 再加载 bean
                ClassHelper.class,
                BeanHelper.class,
                // bean 在 aop 之前会被 aop 覆盖, aop 在 ioc 之前, 先生成增强类再注入
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        );
        classes.forEach(clazz -> ClassUtil.loadClass(clazz.getName()));

    }
}
