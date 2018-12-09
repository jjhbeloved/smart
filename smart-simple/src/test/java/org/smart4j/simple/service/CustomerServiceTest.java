package org.smart4j.simple.service;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.smart4j.simple.BaseUT;
import org.smart4j.simple.model.Customer;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * @author: YANGXUAN223
 * @date: 2018/11/28.
 */
public class CustomerServiceTest extends BaseUT {

    @Spy
    @InjectMocks
    private CustomerService customerService;

    @Test
    public void create() {
        Map<String, Object> fields = Maps.newHashMap();
        Customer customer = new Customer();
        customer.setName("yx");
        assertThat(customerService.create(customer)).isTrue();
        List<Customer> customers = customerService.getAll();
        assertThat(customers).isNotEmpty();
        Customer updateObject = customers.stream().findFirst().get();
        updateObject.setTelephone("123");
        assertThat(customerService.update(updateObject)).isTrue();
        assertThat(customerService.delete(updateObject.getId())).isTrue();
        assertThat(customerService.getAll()).isEmpty();
    }
}