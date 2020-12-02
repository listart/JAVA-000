package io.listart.database.bigimporter;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.listart.database.bigimporter.entity.Order;
import io.listart.database.bigimporter.generator.OrderDataGenerator;
import org.apache.ibatis.jdbc.ScriptRunner;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class BigImporter {
    private static final String SCHEMA_SQL_FILE = "/META-INF/schema.sql";

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/";

    private static final String DB_USERNAME = "root";

    private static final String DB_PASSWORD = "123456";

    private static final String SQL_INSERT = "INSERT INTO ec.order(sn, status, pay_status, shipping_status, user_id, consignee, address, mobile, pay_id, pay_name, pay_fee, shipping_id, shipping_name, shipping_fee, product_amount, paid_money, amount, add_time, confirm_time, pay_time, shipping_time, discount, integral, integral_money) VALUES";
    private static final String SQL_VALUES = "('%s', %d, %d, %d, %d, '%s', '%s', '%s', %d, '%s', %.2f, %d, '%s', %.2f, %.2f, %.2f, %.2f, UNIX_TIMESTAMP(now()), UNIX_TIMESTAMP(now()), UNIX_TIMESTAMP(now()), UNIX_TIMESTAMP(now()), %.2f, %.2f, %.2f)";
    private static final String SQL_PREPARE_VALUES = "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, UNIX_TIMESTAMP(now()), UNIX_TIMESTAMP(now()), UNIX_TIMESTAMP(now()), UNIX_TIMESTAMP(now()), ?, ?, ?)";

    public static void main(String[] args) throws SQLException {
        OrderDataGenerator generator = new OrderDataGenerator(1000000L);
        System.out.println("generating samples...");
        List<Order> samples = generator.gen();
        System.out.println("generated samples.size() = " + samples.size());

        DataSource ds = createDataSource();

//        insertOneByOne(ds, samples);

//        preparedInsertOneByOne(ds, samples);

        insertMultipleValues(ds, samples);

//        preparedInsertBatch(ds, samples);
    }

    public static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(DB_USERNAME);
        config.setPassword(DB_PASSWORD);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }

    public static void prepareDatabase(DataSource ds) throws SQLException {
        Connection con = ds.getConnection();

        ScriptRunner scriptRunner = new ScriptRunner(con);
        scriptRunner.setLogWriter(null);

        Reader reader = new BufferedReader(new InputStreamReader(BigImporter.class.getResourceAsStream(SCHEMA_SQL_FILE)));
        scriptRunner.runScript(reader);
    }

    public static void insertOneByOne(DataSource ds, List<Order> samples) throws SQLException {
        System.out.print("BigImporter.insertOneByOne ");

        prepareDatabase(ds);

        long startTime = System.currentTimeMillis();
        int effectRows = 0;

        try (Connection con = ds.getConnection()) {
            for (Order order : samples) {
                String sql = String.format(SQL_INSERT + SQL_VALUES,
                        order.getSn(), order.getStatus(), order.getPayStatus(), order.getShippingStatus(),
                        order.getUserId(), order.getConsignee(), order.getAddress(), order.getMobile(),
                        order.getPayId(), order.getPayName(), order.getPayFee(),
                        order.getShippingId(), order.getShippingName(), order.getShippingFee(),
                        order.getProductAmount(), order.getPaidMoney(), order.getAmount(),
                        order.getDiscount(), order.getIntegral(), order.getIntegralMoney());

                try (Statement stmt = con.createStatement()) {
                    effectRows += stmt.executeUpdate(sql);
                }
            }

        }

        System.out.printf("inserted %d rows in %d ms.\n", effectRows, System.currentTimeMillis() - startTime);
    }

    public static void preparedInsertOneByOne(DataSource ds, List<Order> samples) throws SQLException {
        System.out.print("BigImporter.preparedInsertOneByOne ");

        prepareDatabase(ds);

        long startTime = System.currentTimeMillis();
        int effectRows = 0;

        String sql = SQL_INSERT + SQL_PREPARE_VALUES;

        try (Connection con = ds.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                for (Order order : samples) {
                    setParameters(stmt, order);
                    effectRows += stmt.executeUpdate();
                }
            }

        }

        System.out.printf("inserted %d rows in %d ms.\n", effectRows, System.currentTimeMillis() - startTime);
    }

    private static void setParameters(PreparedStatement stmt, Order order) throws SQLException {
        stmt.setString(1, order.getSn());
        stmt.setInt(2, order.getStatus());
        stmt.setInt(3, order.getPayStatus());
        stmt.setInt(4, order.getShippingStatus());
        stmt.setLong(5, order.getUserId());
        stmt.setString(6, order.getConsignee());
        stmt.setString(7, order.getAddress());
        stmt.setString(8, order.getMobile());
        stmt.setLong(9, order.getPayId());
        stmt.setString(10, order.getPayName());
        stmt.setDouble(11, order.getPayFee());
        stmt.setLong(12, order.getShippingId());
        stmt.setString(13, order.getShippingName());
        stmt.setDouble(14, order.getShippingFee());
        stmt.setDouble(15, order.getProductAmount());
        stmt.setDouble(16, order.getPaidMoney());
        stmt.setDouble(17, order.getAmount());
        stmt.setDouble(18, order.getDiscount());
        stmt.setDouble(19, order.getIntegral());
        stmt.setDouble(20, order.getIntegralMoney());
    }

    public static void insertMultipleValues(DataSource ds, List<Order> samples) throws SQLException {
        System.out.print("BigImporter.insertMultipleValues ");

        prepareDatabase(ds);

        long startTime = System.currentTimeMillis();
        int effectRows = 0, i = 0, size = samples.size();
        StringBuilder sqlBuilder = new StringBuilder();

        try (Connection con = ds.getConnection()) {
            for (Order order : samples) {
                i++;

                if (i % 1000 == 1) {
                    sqlBuilder = new StringBuilder();
                    sqlBuilder.append(SQL_INSERT);
                } else
                    sqlBuilder.append(',');

                sqlBuilder.append(String.format(SQL_VALUES,
                        order.getSn(), order.getStatus(), order.getPayStatus(), order.getShippingStatus(),
                        order.getUserId(), order.getConsignee(), order.getAddress(), order.getMobile(),
                        order.getPayId(), order.getPayName(), order.getPayFee(),
                        order.getShippingId(), order.getShippingName(), order.getShippingFee(),
                        order.getProductAmount(), order.getPaidMoney(), order.getAmount(),
                        order.getDiscount(), order.getIntegral(), order.getIntegralMoney()));

                if (i % 1000 == 0 || i == size) {
                    try (Statement stmt = con.createStatement()) {
                        effectRows += stmt.executeUpdate(sqlBuilder.toString());
                    }
                }
            }

        }

        System.out.printf("inserted %d rows in %d ms.\n", effectRows, System.currentTimeMillis() - startTime);
    }

    public static void preparedInsertBatch(DataSource ds, List<Order> samples) throws SQLException {
        System.out.print("BigImporter.preparedInsertBatch ");

        prepareDatabase(ds);

        long startTime = System.currentTimeMillis();
        int effectRows = 0, i = 0, size = samples.size();

        String sql = SQL_INSERT + SQL_PREPARE_VALUES;

        try (Connection con = ds.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                for (Order order : samples) {
                    i++;
                    setParameters(stmt, order);
                    stmt.addBatch();

                    if (i % 1000 == 0 || i == size) {
                        effectRows += Arrays.stream(stmt.executeBatch()).sum();
                    }
                }
            }

        }

        System.out.printf("inserted %d rows in %d ms.\n", effectRows, System.currentTimeMillis() - startTime);
    }

}