package model;

import util.DBManager;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author Nick
 */
public class VariantManager {

    public static void main(String[] args) {
        try {
            DBManager.init();

            SampleManager.init();

            getVariantList("1-13273-G-C");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<CalledVariant> getVariantList(String query) throws Exception {
        ArrayList<CalledVariant> list = new ArrayList<>();

        for (String variantIDStr : query.split(",")) {
            String[] tmp = variantIDStr.split("-"); // chr-pos-ref-alt
            if (tmp.length == 4) {
                String chr = tmp[0];
                String pos = tmp[1];
                String ref = tmp[2];
                String alt = tmp[3];

                CalledVariant calledVar = getVariant(chr, pos, ref, alt);
                if (calledVar != null) {
                    list.add(calledVar);
                }
            }
        }

        return list;
    }

    public static CalledVariant getVariant(String chr, String pos, String ref, String alt) throws Exception {
        CalledVariant calledVar = null;
        String sql = "SELECT variant_id, POS, REF, ALT, rs_number, transcript_stable_id, "
                + "effect_id, HGVS_c, HGVS_p, polyphen_humdiv, polyphen_humvar, gene "
                + "FROM variant_chr" + chr + " "
                + "WHERE POS = " + pos + " AND REF = '" + ref + "' AND ALT = '" + alt + "' "
                + "ORDER BY POS,variant_id,effect_id,transcript_stable_id;";

        ResultSet rset = DBManager.executeQuery(sql);

        while (rset.next()) {
            if (calledVar == null) {
                calledVar = new CalledVariant(chr, rset);
            }

            Annotation annotation = new Annotation(chr, rset);
            calledVar.update(annotation);
        }

        if (calledVar != null) {
            calledVar.init();
        }

        return calledVar;
    }
}
