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

    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    public static void init() throws Exception {
        if (connection == null || connection.isClosed()) {
            initDataFromSystemConfig();
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                    dbUrl + "?"
                    + "user=" + dbUser
                    + "&password=" + dbPassword
                    + "&serverTimezone=UTC");
            statement = connection.createStatement();
        } else if (statement.isClosed()) {
            statement = connection.createStatement();
        }
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
        System.out.println(sqlQuery);
        return statement.executeQuery(sqlQuery);
    }

    public static PreparedStatement prepareStatement(String sqlQuery) throws SQLException {
        return connection.prepareStatement(sqlQuery);
    }
    
    public static void executeUpdate(String sqlQuery) throws SQLException {
        statement.executeUpdate(sqlQuery);
    }
}
