package model;

import global.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import util.DBManager;

/**
 *
 * @author nick
 */
public class GeneManager {

    public static String getChr(String gene) throws Exception {
        String sql = "SELECT chrom FROM hgnc WHERE gene = '" + gene + "'";

        Connection connection = DBManager.getConnection();
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);

        if (rs.next()) {
            return rs.getString("chrom");
        }
        
        rs.close();
        statement.close();

        return Data.STRING_NA;
    }
    
    public static boolean isValid(String gene) throws Exception {
        if(getChr(gene).equals(Data.STRING_NA)) {
            return false;
        }
        
        return true;
    }
}
