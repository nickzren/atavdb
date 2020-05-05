package model;

import global.Data;
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
        String[] tmp = query.split("-"); // chr-pos-ref-alt
        if (tmp.length == 4) {
            String chr = tmp[0];
            String pos = tmp[1];
            String ref = tmp[2];
            String alt = tmp[3];

            ArrayList<CalledVariant> list = new ArrayList<>();
            CalledVariant calledVar = getVariant(chr, pos, ref, alt);
            if (calledVar != null) {
                list.add(calledVar);
            }

            return list;
        } else { // gene search
            return getVariantListByGene(query);
        }
    }

    public static CalledVariant getVariant(String chr, String pos, String ref, String alt) throws Exception {
        CalledVariant calledVar = null;
        String sql = "SELECT variant_id, POS, REF, ALT, rs_number, transcript_stable_id, "
                + "effect_id, HGVS_c, HGVS_p, polyphen_humdiv, polyphen_humvar, gene "
                + "FROM variant_chr" + chr + " "
                + "WHERE POS = " + pos + " AND REF = '" + ref + "' AND ALT = '" + alt + "' "
                + "ORDER BY POS,variant_id,effect_id,transcript_stable_id;";

        ResultSet rset = DBManager.executeConcurReadOnlyQuery(sql);

        while (rset.next()) {
            Annotation annotation = new Annotation(chr, rset);
            
            if (calledVar == null) {
                calledVar = new CalledVariant(chr, rset);
            }
            
            calledVar.update(annotation);
        }
        
        rset.close();

        return calledVar;
    }

    public static ArrayList<CalledVariant> getVariantListByGene(String gene) throws Exception {
        ArrayList<CalledVariant> list = new ArrayList<>();

        String chr = getChr(gene);

        if (chr.equals(Data.STRING_NA)) {
            return list;
        }

        String sql = "SELECT variant_id, POS, REF, ALT, rs_number, transcript_stable_id, "
                + "effect_id, HGVS_c, HGVS_p, polyphen_humdiv, polyphen_humvar, gene "
                + "FROM variant_chr" + chr + " "
                + "WHERE gene = '" + gene + "' "
                + "ORDER BY POS,variant_id,effect_id,transcript_stable_id;";

        ResultSet rset = DBManager.executeConcurReadOnlyQuery(sql);

        CalledVariant calledVar = null;
        int currentVariantId = Data.INTEGER_NA;
        while (rset.next()) {
            Annotation annotation = new Annotation(chr, rset);

            if (currentVariantId != rset.getInt("variant_id")) {
                currentVariantId = rset.getInt("variant_id");

                calledVar = new CalledVariant(chr, rset);
                list.add(calledVar);
            }

            calledVar.update(annotation);
        }
        rset.close();

        return list;
    }

    private static String getChr(String gene) throws Exception {
        String sql = "SELECT chrom FROM hgnc WHERE gene = '" + gene + "'";

        ResultSet rset = DBManager.executeQuery(sql);

        if (rset.next()) {
            return rset.getString("chrom");
        }

        rset.close();

        return Data.STRING_NA;
    }
}
