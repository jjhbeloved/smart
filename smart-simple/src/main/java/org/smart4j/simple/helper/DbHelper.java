package org.smart4j.simple.helper;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author: YANGXUAN223
 * @date: 2018/11/28.
 */
public class DbHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbHelper.class);

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://devdb-m1.db.pajkdc.com:3306/jkshop?autoReconnect=true&amp;useUnicode=true&amp;characterset=utf-8";
    private static final String USERNAME = "jkshop";
    private static final String PASSWORD = "jkshop";

    private static final BasicDataSource DS;
    private static final ThreadLocal<Connection> CONNECTION_HOLDER;
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

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

    public static <T> T queryEntity(Class<T> clazz, String sql, Object... params) {
        Connection conn = getConnection();
        try {
            return QUERY_RUNNER.query(conn, sql, new BeanHandler<>(clazz), params);
        } catch (SQLException e) {
            LOGGER.error("query entity failure,sql->{},exception:", sql, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> queryEntityList(Class<T> clazz, String sql) {
        Connection conn = getConnection();
        try {
            return QUERY_RUNNER.query(conn, sql, new BeanListHandler<>(clazz));
        } catch (SQLException e) {
            LOGGER.error("query entity list failure,sql->{},exception:", sql, e);
            throw new RuntimeException(e);
        }
    }

    public static <T> boolean insertEntity(Class<T> clazz, Map<String, Object> fields) {
        if (CollectionUtils.sizeIsEmpty(fields)) {
            LOGGER.error("insert entity list failure, fields is empty.");
        }
        StringBuilder cols = new StringBuilder("(");
        StringBuilder vals = new StringBuilder("(");
        fields.forEach((col, val) -> {
            cols.append(col).append(", ");
            vals.append("?, ");
        });
        cols.replace(cols.lastIndexOf(", "), cols.length(), ")");
        vals.replace(vals.lastIndexOf(", "), vals.length(), ")");
        Object[] params = fields.values().toArray();
        String sql = "INSERT INTO " + getTableName(clazz) +
                cols +
                " VALUES " +
                vals;
        return executeUpdate(sql, params) == 1;
    }

    public static <T> boolean updateEntity(Class<T> clazz, long id, Map<String, Object> fields) {
        if (CollectionUtils.sizeIsEmpty(fields)) {
            LOGGER.error("update entity list failure, fields is empty.");
        }
        StringBuilder filedNames = new StringBuilder();
        fields.forEach((col, val) -> filedNames.append(col).append("=?, "));
        filedNames.replace(filedNames.lastIndexOf(", "), filedNames.length(), "");
        List<Object> params = Lists.newArrayList(fields.values());
        params.add(id);
        String sql = "UPDATE " + getTableName(clazz) +
                " SET " +
                filedNames +
                " WHERE id=?";
        return executeUpdate(sql, params.toArray()) == 1;
    }

    public static <T> boolean deleteEntity(Class<T> clazz, long id) {
        String sql = "DELETE FROM " + getTableName(clazz) +
                " WHERE id=?";
        return executeUpdate(sql, id) == 1;
    }

    public static void executeSqlFile(String file) {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            String sql;
            while ((sql = br.readLine()) != null) {
                executeUpdate(sql);
            }
        } catch (IOException e) {
            LOGGER.error("execute sql file failure,exception:", e);
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * update/insert/delete
     */
    private static int executeUpdate(String sql, Object... params) {
        Connection conn = getConnection();
        try {
            if (CollectionUtils.sizeIsEmpty(params)) {
                return QUERY_RUNNER.update(conn, sql);
            } else {
                return QUERY_RUNNER.update(conn, sql, params);
            }
        } catch (SQLException e) {
            LOGGER.error("update entity list failure,sql->{},exception:", sql, e);
            throw new RuntimeException(e);
        }
    }

    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }
}
