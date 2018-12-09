package org.smart4j.transit;

import org.junit.Test;
import org.smart4j.framework.HelperLoader;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.proxy.ProxyManager;
import org.smart4j.transit.controller.CustomerController;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/28.
 */
public class ATest {

    @Test
    public void getClassSet() {
        HelperLoader.init();
        CustomerController cc = BeanHelper.getBean(CustomerController.class);
        cc.cout();
    }

}
