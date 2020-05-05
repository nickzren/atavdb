package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 *
 * @author nick
 */
public class DBManager {

    private static Connection connection;
    private static Statement statement;

    // this is just for collecting annotation data
    private static Connection concurReadOnlyConn;
    private static Statement concurReadOnlyStmt;

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    public static void init() throws Exception {
        if (connection == null || connection.isClosed()) {
            initDataFromSystemConfig();
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = getConnection();
            statement = connection.createStatement();

            concurReadOnlyConn = getConnection();
            concurReadOnlyStmt = concurReadOnlyConn.createStatement(
                    java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY); // The ResultSet object cannot be updated using the ResultSet interface.
            concurReadOnlyStmt.setFetchSize(Integer.MIN_VALUE);
        } else if (statement.isClosed()) {
            statement = connection.createStatement();
        } else if(concurReadOnlyStmt.isClosed()) {
            concurReadOnlyStmt = concurReadOnlyConn.createStatement(
                    java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_READ_ONLY); 
            concurReadOnlyStmt.setFetchSize(Integer.MIN_VALUE);
        }
    }

    private static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                dbUrl + "?"
                + "user=" + dbUser
                + "&password=" + dbPassword
                + "&serverTimezone=UTC");
    }

    private static void initDataFromSystemConfig() {
        try {
            // server config from enviroment
//            dbUrl = System.getProperty("DB_URL");
//            dbUser = System.getProperty("DB_USER");
//            dbPassword = System.getProperty("DB_PASSWORD");

            // local config
            dbUrl = "jdbc:mysql://localhost:3306/WalDB";
            dbUser = "test";
            dbPassword = "test";
        } catch (Exception e) {
            e.printStackTrace();
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
}
