package io.listart.jdbc;

import io.listart.domain.User;

import java.sql.*;

public class RawJDBCDemo {
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306";
    public static final String SCHEMA = "sample_db";
    public static final String USER = "root";
    public static final String PASSWORD = "123456";

    public static void main(String[] args) throws SQLException {
        // 准备数据库环境
        Connection connection = getConnection();

        assert connection != null;
        // 原始插入用户并查询
        int newUserId = insertUser(connection);
        queryUser(connection, newUserId);

        // 更新用户并查询
        updateUser(connection, newUserId);
        queryUser(connection, newUserId);

        // 删除并查询
        deleteUser(connection, newUserId);
        queryUser(connection, newUserId);
    }

    private static void deleteUser(Connection connection, final int userId) throws SQLException {
        String sql = "delete from user where id=" + userId;

        System.out.println("RawJDBCDemo.deleteUser:" + sql);

        try (Statement stmt = connection.createStatement()) {
            int effectRows = stmt.executeUpdate(sql);

            System.out.println("effectRows = " + effectRows);
        }
    }

    private static void updateUser(Connection connection, final int userId) throws SQLException {
        String sql = "update user set password='passwordChanged' where id=" + userId;

        System.out.println("RawJDBCDemo.updateUser:" + sql);

        try (Statement stmt = connection.createStatement()) {
            int effectRows = stmt.executeUpdate(sql);

            System.out.println("effectRows = " + effectRows);
        }
    }

    private static void queryUser(Connection connection, final int userId) throws SQLException {
        String sql = "select * from user where id=" + userId;

        System.out.println("RawJDBCDemo.queryUser:" + sql);

        try (Statement stmt = connection.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    String name = rs.getString("name");
                    String password = rs.getString("password");

                    User user = new User(id, name, password);

                    System.out.println("user = " + user);
                }
            }
        }
    }

    protected static Integer insertUser(final Connection connection) throws SQLException {
        String sql = "INSERT INTO user (name,password) VALUES (\"name\",\"password\")";

        System.out.println("RawJDBCDemo.insertUser:" + sql);

        try (Statement stmt = connection.createStatement()) {
            int effectRows = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();

                Integer userId = rs.getInt(1);

                System.out.println("effectRows = " + effectRows);
                System.out.println("userId = " + userId);

                return userId;
            }
        }
    }

    protected static void executeSql(final Connection connection, final String sql) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
        stmt.close();
    }

    protected static void createDefaultSchema(final Connection connection) throws SQLException {
        executeSql(connection, "create database if not exists " + SCHEMA + " character set = utf8");
        executeSql(connection, "use " + SCHEMA);
    }

    protected static void createDefaultTable(final Connection connection) throws SQLException {
        executeSql(connection, "CREATE TABLE IF NOT EXISTS user (\n" +
                "    id BIGINT(20) NOT NULL AUTO_INCREMENT,\n" +
                "    name VARCHAR(50) NOT NULL,\n" +
                "    password VARCHAR(50) DEFAULT NULL,\n" +
                "    PRIMARY KEY (id)\n" +
                ")");
    }

    protected static Connection getConnection() {
        try {
            // 尝试获取jdbc驱动类
            Class.forName(DRIVER);

            // 获取连接
            Connection cnn = DriverManager.getConnection(URL, USER, PASSWORD);

            // 准备缺省数据库
            createDefaultSchema(cnn);

            // 准备缺省user表
            createDefaultTable(cnn);

            return cnn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
