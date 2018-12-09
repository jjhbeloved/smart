package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.ConfigConstant;
import org.smart4j.framework.util.PropsUtil;

import java.util.Properties;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/28.
 */
public final class ConfigHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHelper.class);

    public static String getJdbcDriver() {
        return CONFIG_PROPS.getProperty(ConfigConstant.JDBC_DRIVER, "com.mysql.jdbc.Driver");
    }

    public static String getJdbcUrl() {
        return CONFIG_PROPS.getProperty(ConfigConstant.JDBC_URL, "jdbc:mysql://127.0.0.1:3306/smart");
    }

    public static String getJdbcUsername() {
        return CONFIG_PROPS.getProperty(ConfigConstant.JDBC_USERNAME, "admin");
    }

    public static String getJdbcPassword() {
        return CONFIG_PROPS.getProperty(ConfigConstant.JDBC_PASSWORD, "admin");
    }

    public static String getAppBasePackage() {
        return CONFIG_PROPS.getProperty(ConfigConstant.APP_BASE_PACKAGE, "org.smart4j");
    }

    public static String getAppJspPath() {
        return CONFIG_PROPS.getProperty(ConfigConstant.APP_JSP_PATH, "/WEB-INF/view/");
    }

    public static String getAppAssetPath() {
        return CONFIG_PROPS.getProperty(ConfigConstant.APP_ASSET_PATH, "/asset/");
    }

    private static final Properties CONFIG_PROPS = PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);

}
