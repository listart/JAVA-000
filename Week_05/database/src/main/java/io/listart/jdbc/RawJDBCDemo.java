package io.listart.jdbc;

import java.sql.*;

public class RawJDBCDemo {
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/sample_db";
    public static final String SCHEMA = "sample_db";
    public static final String USER = "root";
    public static final String PASSWORD = "123456";

    protected static Integer userId = null;

    public static void main(String[] args) throws SQLException {
        Connection connection = getConnection();

        assert connection != null;
        insertUser(connection);
//        queryUser(connection);
//        updateUser(connection);
//        deleteUser(connection);
    }

    private static void deleteUser(Connection connection) {
    }

    private static void updateUser(Connection connection) {
    }

    private static void queryUser(Connection connection) {
    }

    protected static void insertUser(final Connection connection) throws SQLException {
        String sql = "INSERT INTO user (name,password) VALUES (\"name\",\"password\")";
        Statement stmt = connection.createStatement();
        int effectRows = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        userId = rs.getInt(1);

        System.out.println("RawJDBCDemo.insertUser " + sql);
        System.out.println("effectRows = " + effectRows);
        System.out.println("userId = " + userId);

        rs.close();
        stmt.close();
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
            Class.forName(DRIVER);

            Connection cnn = DriverManager.getConnection(URL, USER, PASSWORD);

            createDefaultSchema(cnn);

            createDefaultTable(cnn);

            return cnn;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
