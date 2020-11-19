package io.listart.jdbc;

import io.listart.domain.User;

import java.sql.*;

public class TransactionJDBCDemo extends RawJDBCDemo {

    public static void main(String[] args) throws SQLException {
        // 准备数据库环境
        Connection connection = getConnection();

        if (connection == null) {
            System.out.println("获取连接失败！");
            return;
        }

        // 插入测试数据
        int userId = createUser(connection);
        queryUser(connection, userId);

        // 创建事务，不断double密码，直至密码长度超过50触发回滚
        infiniteDoublePassword(connection, userId);

        // 事务保障库中数据有效回滚至调用infiniteDoublePassword操作之前
        queryUser(connection, userId);
    }

    private static void infiniteDoublePassword(final Connection connection, final int userId) throws SQLException {
        System.out.println("TransactionJDBCDemo.tryDoublePassword");

        String oldPwd, newPwd;

        try {
            connection.setAutoCommit(false);

            String querySql = "select name,password from user where id=?";
            String doublePasswordSql = "update user set password = ? where id=? and password=?";

            try (PreparedStatement query = connection.prepareStatement(querySql);
                 PreparedStatement doublePassword = connection.prepareStatement(doublePasswordSql)) {
                Savepoint savepoint = connection.setSavepoint();

                query.setInt(1, userId);

                System.out.println(query);

                try (ResultSet rs = query.executeQuery()) {
                    rs.next();
                    oldPwd = rs.getString("password");
                }

                for (;;) {
                    newPwd = oldPwd + oldPwd;

                    doublePassword.setString(1, newPwd);
                    doublePassword.setInt(2, userId);
                    doublePassword.setString(3, oldPwd);

                    System.out.println(doublePassword);

                    if (newPwd.length() >= 50) {
                        System.out.println("new password is too long, roll back to savepoint!");
                        connection.rollback(savepoint);
                        break;
                    } else
                        doublePassword.executeUpdate();

                    oldPwd = newPwd;
                }

                connection.commit();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw ex;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    private static int createUser(final Connection connection) throws SQLException {
        String sql = "insert into user(name, password) values(?,?)";

        try (PreparedStatement create = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            create.setString(1, "for transaction");
            create.setString(2, "root password");

            System.out.println(create);

            create.executeUpdate();

            try (ResultSet rs = create.getGeneratedKeys()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private static void queryUser(final Connection connection, final int userId) throws SQLException {
        String sql = "select * from user where id=?";

        try (PreparedStatement query = connection.prepareStatement(sql)) {
            query.setInt(1, userId);

            System.out.println(query);

            try (ResultSet rs = query.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String password = rs.getString("password");

                    User user = new User(id, name, password);

                    System.out.println(user);
                }
            }
        }
    }

}