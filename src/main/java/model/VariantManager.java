package model;

import util.DBManager;
import java.sql.ResultSet;

/**
 *
 * @author Nick
 */
public class VariantManager {

    public static void main(String[] args) {
        try {
            DBManager.init();

            SampleManager.init();

            getVariant("1-13273-G-C");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CalledVariant getVariant(String query) throws Exception {
        String[] tmp = query.split("-");

        String chr = tmp[0];
        String pos = tmp[1];
        String ref = tmp[2];
        String alt = tmp[3];
        
        String sql = "SELECT variant_id, POS, REF, ALT, rs_number, transcript_stable_id, "
                + "effect_id, HGVS_c, HGVS_p, polyphen_humdiv, polyphen_humvar, gene "
                + "FROM variant_chr" + chr + " "
                + "WHERE POS = " + pos + " AND REF = '" + ref + "' AND ALT = '" + alt + "' "
                + "ORDER BY POS,variant_id,effect_id,transcript_stable_id;";

         ResultSet rset = DBManager.executeQuery(sql);

        if (rset.next()) {
            return new CalledVariant(chr, rset);
        }

        return null;
    }
}
