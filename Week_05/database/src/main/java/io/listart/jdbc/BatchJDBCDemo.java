package io.listart.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

public class BatchJDBCDemo extends RawJDBCDemo {

    public static void main(String[] args) throws SQLException {
        // 准备数据库环境
        Connection connection = getConnection();

        if (connection == null) {
            System.out.println("获取连接失败！");
            return;
        }

        // 批量插入100个用户
        create100User(connection);
    }

    private static void create100User(final Connection connection) throws SQLException {
        String sql = "insert into user(name, password) values(?,?)";

        try {
            connection.setAutoCommit(false);

            try (PreparedStatement create = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                for (int i = 0; i < 100; i++) {
                    create.setString(1, "batch user " + i);
                    create.setString(2, "the same password");

                    System.out.println(create);

                    create.addBatch();
                }

                int[] results = create.executeBatch();
                System.out.println("stmt.executeBatch() = " + Arrays.toString(results));
            }

        } finally {
            connection.setAutoCommit(true);
        }
    }

}