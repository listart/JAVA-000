package io.listart.jdbc;

import io.listart.domain.User;

import java.sql.*;

public class PreparedJDBCDemo extends RawJDBCDemo {

    public static void main(String[] args) throws SQLException {
        // 准备数据库环境
        Connection connection = getConnection();

        // 禁用事务
        assert connection != null;
        connection.setAutoCommit(false);

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
        String sql = "delete from user where id=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            System.out.println(stmt);

            int effectRows = stmt.executeUpdate();

            System.out.println("effectRows = " + effectRows);
        }
    }

    private static void updateUser(Connection connection, final int userId) throws SQLException {
        String sql = "update user set password='passwordChanged' where id=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            System.out.println(stmt);

            int effectRows = stmt.executeUpdate();

            System.out.println("effectRows = " + effectRows);
        }
    }

    private static void queryUser(Connection connection, final int userId) throws SQLException {
        String sql = "select * from user where id=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            System.out.println(stmt);

            try (ResultSet rs = stmt.executeQuery()) {
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
        String sql = "INSERT INTO user (name,password) VALUES (?,?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, "name");
            stmt.setString(2, "password");

            System.out.println(stmt);

            int effectRows = stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                rs.next();

                Integer userId = rs.getInt(1);

                System.out.println("effectRows = " + effectRows);
                System.out.println("userId = " + userId);

                return userId;
            }
        }
    }
}
