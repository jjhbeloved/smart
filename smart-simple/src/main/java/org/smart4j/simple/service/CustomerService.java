package org.smart4j.simple.service;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.simple.helper.DbHelper;
import org.smart4j.simple.model.Customer;

import javax.naming.Name;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/28.
 */
public class CustomerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    private static final String QUERY_CUSTOMER = "select * from customer";
    private static final String QUERY_ALL_CUSTOMERS = "select * from customer";

    public List<Customer> getAll() {
        return DbHelper.queryEntityList(Customer.class, QUERY_ALL_CUSTOMERS);
    }

    public boolean create(Customer customer) {
        Map<String, Object> fields = Maps.newLinkedHashMap();
        fields.put("name", customer.getName());
        fields.put("telephone", customer.getTelephone());
        fields.put("email", customer.getEmail());
        return DbHelper.insertEntity(Customer.class, fields);
    }

    public boolean update(Customer customer) {
        Map<String, Object> fields = Maps.newLinkedHashMap();
        Optional.ofNullable(customer.getName()).ifPresent(name -> fields.put("name", name));
        Optional.ofNullable(customer.getTelephone()).ifPresent(telephone -> fields.put("telephone", telephone));
        Optional.ofNullable(customer.getEmail()).ifPresent(email -> fields.put("email", email));
        Optional.ofNullable(customer.getContact()).ifPresent(contact -> fields.put("contact", contact));
        Optional.ofNullable(customer.getRemark()).ifPresent(remark -> fields.put("remark", remark));
        return DbHelper.updateEntity(Customer.class, customer.getId(), fields);
    }

    public boolean delete(long id) {
        return DbHelper.deleteEntity(Customer.class, id);
    }
}
