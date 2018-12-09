package org.smart4j.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/28.
 */
public final class PropsUtil {

    public static Properties loadProps(String file) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
        Properties properties = new Properties();
        try {
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
