package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 *
 * @author nick
 */
public class DBManager {

    private static PoolingDataSource<PoolableConnection> dataSource;
    private static Connection connection;
    private static Statement statement;

    // this is just for collecting annotation data
    private static Connection concurReadOnlyConn;
    private static Statement concurReadOnlyStmt;

    private static final String dbDriver = "com.mysql.cj.jdbc.Driver";
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    public static void init() throws Exception {
        initDataSource();

        initConnection();
    }

    private static void initDataFromSystemConfig() {
        try {
            // server config from enviroment
//            dbUrl = System.getProperty("DB_URL");
//            dbUser = System.getProperty("DB_USER");
//            dbPassword = System.getProperty("DB_PASSWORD");

            // local config
            dbUrl = "jdbc:mysql://localhost:3306/WalDB?serverTimezone=UTC";
            dbUser = "test";
            dbPassword = "test";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initDataSource() {
        if (dataSource == null) {
            try {
                Class.forName(dbDriver);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            initDataFromSystemConfig();

            ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                    dbUrl, dbUser, dbPassword);
            PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
                    connectionFactory, null);

            GenericObjectPoolConfig<PoolableConnection> config = new GenericObjectPoolConfig<>();
            config.setMaxTotal(100);
            config.setMaxIdle(100);
            config.setMinIdle(10);

            ObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(
                    poolableConnectionFactory, config);
            poolableConnectionFactory.setPool(connectionPool);

            dataSource = new PoolingDataSource<>(
                    connectionPool);
        }
    }

    private static void initConnection() throws Exception {
        if (connection == null || connection.isClosed()) {

            connection = dataSource.getConnection();
            statement = connection.createStatement();

            concurReadOnlyConn = dataSource.getConnection();
            concurReadOnlyStmt = concurReadOnlyConn.createStatement(
                    java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY); // The ResultSet object cannot be updated using the ResultSet interface.
            concurReadOnlyStmt.setFetchSize(Integer.MIN_VALUE);
        } else if (statement.isClosed()) {
            statement = connection.createStatement();
        } else if (concurReadOnlyStmt.isClosed()) {
            concurReadOnlyStmt = concurReadOnlyConn.createStatement(
                    java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY);
            concurReadOnlyStmt.setFetchSize(Integer.MIN_VALUE);
        }
    }

    public static ResultSet executeQuery(String sqlQuery) throws SQLException {
        return statement.executeQuery(sqlQuery);
    }

    public static PreparedStatement prepareStatement(String sqlQuery) throws SQLException {
        return connection.prepareStatement(sqlQuery);
    }

    public static void executeUpdate(String sqlQuery) throws SQLException {
        statement.executeUpdate(sqlQuery);
    }

    public static ResultSet executeConcurReadOnlyQuery(String sqlQuery) throws SQLException {
        return concurReadOnlyStmt.executeQuery(sqlQuery);
    }

    public static void close() throws SQLException {
        statement.close();
        connection.close();

        concurReadOnlyStmt.close();
        concurReadOnlyConn.close();
    }
}
