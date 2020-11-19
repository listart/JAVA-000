package io.listart.jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class HikariCPDemo extends RawJDBCDemo {

    public static void main(String[] args) throws SQLException {
        // 配置HikariCP
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(RawJDBCDemo.DRIVER);
        config.setJdbcUrl(RawJDBCDemo.URL);
        config.setUsername(RawJDBCDemo.USER);
        config.setPassword(RawJDBCDemo.PASSWORD);

        // 创建Hikari连接池
        HikariDataSource ds = new HikariDataSource(config);

        // 获取连接
        Connection connection = ds.getConnection();

        // 使用sample_db库
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("use sample_db");
        }

        // 批量插入100个用户
        BatchJDBCDemo.create100User(connection);
    }

}