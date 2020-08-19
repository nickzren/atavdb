package org.atavdb.service.model;

import org.atavdb.service.util.DBManager;
import org.atavdb.model.SearchFilter;
import org.atavdb.global.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import org.atavdb.model.Annotation;
import org.atavdb.model.Variant;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Nick
 */
@Service
@ComponentScan("org.atavdb.service")
@ComponentScan("org.atavdb.model")
public class VariantManager implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    DBManager dbManager;

    @Autowired
    GeneManager geneManager;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // cached data when no new samples data loaded
    private HashMap<String, ArrayList<Variant>> cachedVariant4AllSampleMap = new HashMap<>();
    private HashMap<String, ArrayList<Variant>> cachedVariant4PublicAvailableSampleMap = new HashMap<>();

    private HashMap<String, ArrayList<Variant>> getMap(SearchFilter filter) {
        return filter.isAvailableControlUseOnly() ? cachedVariant4PublicAvailableSampleMap : cachedVariant4AllSampleMap;
    }

    public ArrayList<Variant> getVariantList(SearchFilter filter, ModelAndView mv) throws Exception {
        ArrayList<Variant> list = new ArrayList<>();

        String queryIdentifier = filter.getQueryIdentifier();
        list = getMap(filter).getOrDefault(queryIdentifier, list);
        if (list.isEmpty()) {
            getMap(filter).put(queryIdentifier, list);
        } else {
            // return cached data , apply max af or ultra variant filter
            return applyFilter(filter, list);
        }

        String chr = "";
        String joinSQL = ""; // codingandsplice_effect
        String whereSQL = "";

        String query = filter.getQuery();
        if (filter.isQueryVariant()) {
            String[] tmp = query.split("-");
            chr = tmp[0];
            whereSQL = "WHERE POS=? AND REF=? AND ALT =? ";
        } else if (filter.isQueryGene()) {
            chr = geneManager.getChr(query);
            // it's important to force using gene key here for better performance
            joinSQL = "FORCE INDEX (gene_idx),codingandsplice_effect e ";
            whereSQL = "WHERE gene=? AND v.effect_id=e.id ";
        } else if (filter.isQueryRegion()) {
            String[] tmp = query.split(":");
            chr = tmp[0];
            tmp = tmp[1].split("-");
            joinSQL = ",codingandsplice_effect e ";
            whereSQL = "WHERE POS BETWEEN ? AND ? AND v.effect_id = e.id ";
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

        Connection connection = dbManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        if (filter.isQueryVariant()) { // variant chr-pos-ref-alt
            String[] tmp = query.split("-");
            preparedStatement.setInt(1, Integer.valueOf(tmp[1]));
            preparedStatement.setString(2, tmp[2]);
            preparedStatement.setString(3, tmp[3]);
        } else if (filter.isQueryGene()) { // gene
            preparedStatement.setString(1, query);
        } else if (filter.isQueryRegion()) { // region chr:start-end
            String[] tmp = query.split(":");
            tmp = tmp[1].split("-");
            preparedStatement.setInt(1, Integer.valueOf(tmp[0]));
            preparedStatement.setInt(2, Integer.valueOf(tmp[1]));
        }

        ResultSet rs = preparedStatement.executeQuery();

        Variant variant = null;
        int currentVariantId = Data.INTEGER_NA;
        while (rs.next()) {
            Annotation annotation = applicationContext.getBean(Annotation.class);
            annotation.init(rs);

            if (currentVariantId != rs.getInt("variant_id")) {
                currentVariantId = rs.getInt("variant_id");

                variant = applicationContext.getBean(Variant.class);
                variant.init(chr, rs, filter, mv);

                if (variant.isValid()) {
                    list.add(variant);
                }
            }

            variant.update(annotation);
        }

        rs.close();
        preparedStatement.close();

        // cached data does not apply max af and ultra filters
        return applyFilter(filter, list);
    }

    private ArrayList<Variant> applyFilter(SearchFilter filter, ArrayList<Variant> list) {
        if (!filter.isUltraRareVariant() && filter.getMAF() == Data.NO_FILTER) {
            return list;
        }

        ArrayList<Variant> newList = new ArrayList<>();
        for (Variant variant : list) {
            if (filter.isUltraRareVariant()) {
                if (!variant.isExternalAFValid(filter)) {
                    continue;
                }
            }

            if (filter.getMAF() != Data.NO_FILTER) {
                if (!variant.isMAFValid(filter)) {
                    continue;
                }
            }

            newList.add(variant);
        }

        return newList;
    }

    public void clearCachedData(SearchFilter filter) {
        getMap(filter).clear();
    }
}
