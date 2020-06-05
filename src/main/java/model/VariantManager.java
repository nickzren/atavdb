package model;

import global.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import util.DBManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Nick
 */
public class VariantManager {

    public static ArrayList<Variant> getVariantList(FilterManager filter, HttpServletRequest request) throws Exception {
        ArrayList<Variant> list = new ArrayList<>();

        String chr = "";
        String joinSQL = "";// codingandsplice_effect
        String whereSQL = "";

        String query = filter.getQuery();
        String queryType = filter.getQueryType();
        if (queryType.equals(Data.QUERT_TYPE[1])) { // variant chr-pos-ref-alt
            String[] tmp = query.split("-");
            chr = tmp[0];
            whereSQL = "WHERE POS=? AND REF=? AND ALT =? ";
        } else if (queryType.equals(Data.QUERT_TYPE[2])) { // gene
            chr = GeneManager.getChr(query);
            // it's important to force using gene key here for better performance
            joinSQL = "FORCE INDEX (gene_idx),codingandsplice_effect e ";
            whereSQL = "WHERE gene=? AND v.effect_id=e.id ";
        } else if (queryType.equals(Data.QUERT_TYPE[3])) { // region chr:start-end
            String[] tmp = query.split(":");
            chr = tmp[0];
            tmp = tmp[1].split("-");
            joinSQL = ",codingandsplice_effect e ";
            whereSQL = "WHERE POS BETWEEN ? AND ? AND v.effect_id = e.id ";
        } else {
            return list;
        }

        if (filter.isHighQualityVariant()) {
            whereSQL += "AND has_high_quality_call=1 ";
        }

        String sql = "SELECT variant_id, POS, REF, ALT, rs_number, transcript_stable_id, "
                + "effect_id, HGVS_c, HGVS_p, polyphen_humdiv, polyphen_humvar, gene "
                + "FROM variant_chr" + chr + " v "
                + joinSQL
                + whereSQL
                + "ORDER BY POS,variant_id,effect_id,transcript_stable_id;";

        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        if (queryType.equals(Data.QUERT_TYPE[1])) { // variant chr-pos-ref-alt
            String[] tmp = query.split("-");
            preparedStatement.setInt(1, Integer.valueOf(tmp[1]));
            preparedStatement.setString(2, tmp[2]);
            preparedStatement.setString(3, tmp[3]);
        } else if (queryType.equals(Data.QUERT_TYPE[2])) { // gene
            preparedStatement.setString(1, query);
        } else if (queryType.equals(Data.QUERT_TYPE[3])) { // region chr:start-end
            String[] tmp = query.split(":");
            tmp = tmp[1].split("-");
            preparedStatement.setInt(1, Integer.valueOf(tmp[0]));
            preparedStatement.setInt(2, Integer.valueOf(tmp[1]));
        }

        ResultSet rs = preparedStatement.executeQuery();

        Variant variant = null;
        int currentVariantId = Data.INTEGER_NA;
        while (rs.next()) {
            Annotation annotation = new Annotation(chr, rs);

            if (currentVariantId != rs.getInt("variant_id")) {
                currentVariantId = rs.getInt("variant_id");

                variant = new Variant(chr, rs, filter, request);

                if (variant.isValid()) {
                    list.add(variant);
                }
            }

            variant.update(annotation);
        }

        rs.close();
        preparedStatement.close();

        return list;
    }
}
