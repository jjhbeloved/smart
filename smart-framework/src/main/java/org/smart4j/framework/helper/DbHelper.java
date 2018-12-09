package org.smart4j.framework.helper;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author: YANGXUAN223
 * @date: 2018/12/7.
 */
public class DbHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbHelper.class);

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://devdb-m1.db.pajkdc.com:3306/jkshop?autoReconnect=true&amp;useUnicode=true&amp;characterset=utf-8";
    private static final String USERNAME = "jkshop";
    private static final String PASSWORD = "jkshop";

    private static final BasicDataSource DS;
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    static {
        CONNECTION_HOLDER = new ThreadLocal<>();
        DS = new BasicDataSource();
        DS.setDriverClassName(DRIVER);
        DS.setUrl(URL);
        DS.setUsername(USERNAME);
        DS.setPassword(PASSWORD);
    }

    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
        if (conn == null) {
            try {
                conn = DS.getConnection();
                CONNECTION_HOLDER.set(conn);
            } catch (SQLException e) {
                LOGGER.error("get connection failure.exception:", e);
                throw new RuntimeException(e);
            }
        }
        return conn;
    }

    public static void close() {
        if (DS.isClosed()) {
            return;
        }
        try {
            DS.close();
        } catch (SQLException e) {
            LOGGER.error("close db pool failure.exception:", e);
        } finally {
            CONNECTION_HOLDER.remove();
        }
    }

    public static void beginTransaction() {
        Connection connection = getConnection();
        if (connection == null) {
            return;
        }
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.error("begin transaction failure", e);
            throw new RuntimeException(e);
        }
    }

    public static void commitTransaction() {
        Connection connection = getConnection();
        if (connection == null) {
            return;
        }
        try {
            connection.commit();
        } catch (SQLException e) {
            LOGGER.error("commit transaction failure", e);
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

    public static void rollbackTransaction() {
        Connection connection = getConnection();
        if (connection == null) {
            return;
        }
        try {
            connection.rollback();
        } catch (SQLException e) {
            LOGGER.error("rollback transaction failure", e);
            throw new RuntimeException(e);
        } finally {
            close();
        }
    }

}
