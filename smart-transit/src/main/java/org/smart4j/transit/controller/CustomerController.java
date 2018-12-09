package org.smart4j.transit.controller;

import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.transit.service.CustomerService;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/29.
 */
@Controller
public class CustomerController {

    @Inject
    private CustomerService customerService;

    public void cout() {
        System.out.println(customerService.count());
    }

    @Action("get:/hello")
    public Data hello(Param param) {
        String name = "hello";
        return new Data(name);
    }

    @Action("get:/go")
    public View go() {
        View view = new View("/hello");
        view.addModel("key", "value");
        return view;
    }
}
