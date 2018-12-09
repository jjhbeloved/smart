package org.smart4j.simple;

import org.junit.After;
import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.smart4j.simple.helper.DbHelper;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/28.
 */
public abstract class BaseUT {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
        DbHelper.close();
    }
}
