package model;

import java.sql.PreparedStatement;
import object.Variant;
import util.DBManager;
import java.sql.ResultSet;

/**
 *
 * @author Nick
 */
public class Output {

    public static Variant variant;
    public static String errorMsg;

    public static void init(String query) throws Exception {
        variant = null;
        errorMsg = "";

        initVariant(query);
    }

    public static void initVariant(String query) throws Exception {
        String[] tmp = query.split("-");

        String sql = "SELECT * "
                + "FROM variant_v3 "
                + "WHERE chr= ? AND pos= ? AND ref= ? AND alt= ?";

        PreparedStatement stmt = DBManager.prepareStatement(sql);
        stmt.setString(1, tmp[0]);
        stmt.setInt(2, Integer.valueOf(tmp[1]));
        stmt.setString(3, tmp[2]);
        stmt.setString(4, tmp[3]);
        ResultSet rset = stmt.executeQuery();

        if (rset.next()) {
            variant = new Variant(rset);
        }
    }
}
