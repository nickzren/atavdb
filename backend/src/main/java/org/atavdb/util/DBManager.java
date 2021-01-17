package org.atavdb.util;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.atavdb.exception.DatabaseException;

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

    static {
        try {
            initDataSource();

            initConnection();
        } catch (Exception ex) {
            throw new DatabaseException(ex);
        }
    }

    private static void initDataFromSystemConfig() {
        // server config from tomcat $CATALINA_HOME/bin/setenv.sh
        dbUrl = System.getenv("DB_URL");
        dbUser = System.getenv("DB_USER");
        dbPassword = System.getenv("DB_PASSWORD");

        // local config without tomcat
//        dbUrl = "jdbc:mysql://127.0.0.1:3306/atavdb?serverTimezone=UTC";
//        dbUser = "atavdb";
//        dbPassword = "atavdb";
    }

    private static void initDataSource() throws ClassNotFoundException {
        if (dataSource == null) {
            Class.forName(dbDriver);

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

            dataSource = new PoolingDataSource<>(connectionPool);
        }
    }

    private static void initConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = dataSource.getConnection();
        }
    }

    public static  Connection getConnection() throws SQLException {
        initConnection();

        return connection;
    }
}
