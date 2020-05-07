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
            Filter filter = new Filter();
            
            DBManager.init();

            SampleManager.init(filter);

            getVariantList(filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<CalledVariant> getVariantList(Filter filter) throws Exception {
        ArrayList<CalledVariant> list = new ArrayList<>();

        String chr = "";
        String whereSQL = "";
        
        String query = filter.getQuery();
        String queryType = filter.getQueryType();
        if (queryType.equals(Data.QUERT_TYPE[1])) { // variant chr-pos-ref-alt
            String[] tmp = query.split("-");
            chr = tmp[0];
            whereSQL = "WHERE POS = " + tmp[1] + " AND REF = '" + tmp[2] + "' AND ALT = '" + tmp[3] + "' ";
        } else if (queryType.equals(Data.QUERT_TYPE[2])) { // gene
            chr = GeneManager.getChr(query);
            whereSQL = "WHERE gene = '" + query + "' ";
        } else if(queryType.equals(Data.QUERT_TYPE[3])) { // region chr:start-end
            String[] tmp = query.split(":");
            chr = tmp[0];
            tmp = tmp[1].split("-");
            whereSQL = "WHERE POS BETWEEN " + tmp[0] + " AND " + tmp[1] + " ";
        } else {
            return list;
        }

        String sql = "SELECT variant_id, POS, REF, ALT, rs_number, transcript_stable_id, "
                + "effect_id, HGVS_c, HGVS_p, polyphen_humdiv, polyphen_humvar, gene "
                + "FROM variant_chr" + chr + " "
                + whereSQL
                + "ORDER BY POS,variant_id,effect_id,transcript_stable_id;";

        ResultSet rset = DBManager.executeConcurReadOnlyQuery(sql);

        CalledVariant calledVar = null;
        int currentVariantId = Data.INTEGER_NA;
        while (rset.next()) {
            Annotation annotation = new Annotation(chr, rset);

            if (currentVariantId != rset.getInt("variant_id")) {
                currentVariantId = rset.getInt("variant_id");

                calledVar = new CalledVariant(chr, rset, filter);
                
                if(calledVar.isValid()) {
                    list.add(calledVar);
                } 
            }

            calledVar.update(annotation);
        }
        
        rset.close();

        return list;
    }
}
