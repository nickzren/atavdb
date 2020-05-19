package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final String dbDriver = "com.mysql.cj.jdbc.Driver";
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    public static void init() throws Exception {
        initDataSource();

        initConnection();
    }

    private static void initDataFromSystemConfig() {
        // server config from enviroment JAVA_HOME
        dbUrl = System.getenv("DB_URL");
        dbUser = System.getenv("DB_USER");
        dbPassword = System.getenv("DB_PASSWORD");

        // local config
//            dbUrl = "jdbc:mysql://localhost:3306/WalDB?serverTimezone=UTC";
//            dbUser = "test";
//            dbPassword = "test";
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
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
