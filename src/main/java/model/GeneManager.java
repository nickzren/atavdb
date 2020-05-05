package model;

import global.Data;
import java.sql.ResultSet;
import util.DBManager;

/**
 *
 * @author nick
 */
public class GeneManager {

    public static String getChr(String gene) throws Exception {
        String sql = "SELECT chrom FROM hgnc WHERE gene = '" + gene + "'";

        ResultSet rset = DBManager.executeQuery(sql);

        if (rset.next()) {
            return rset.getString("chrom");
        }

        rset.close();

        return Data.STRING_NA;
    }
    
    public static boolean isValid(String gene) throws Exception {
        if(getChr(gene).equals(Data.STRING_NA)) {
            return false;
        }
        
        return true;
    }
}
