package org.atavdb.model;

import org.atavdb.util.DBManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author nick
 */
public class CarrierBlockManager {
  
    public final static int CARRIER_BLOCK_SIZE = 1000;

    public static void init(Variant var, SearchFilter filter, ModelAndView mv) throws Exception {
        int blockId = Math.floorDiv(var.getPos(), CARRIER_BLOCK_SIZE);

        Integer currentBlockId = (Integer) mv.getModel().get("currentCarrierBlockId");

        if (currentBlockId == null || currentBlockId != blockId) {
            currentBlockId = blockId;

            HashMap<Integer, HashMap<Integer, Carrier>> blockCarrierMap = new HashMap<>();

            initBlockCarrierMap(var, filter, blockId, blockCarrierMap);

            mv.addObject("currentCarrierBlockId", currentBlockId);
            mv.addObject("blockCarrierMap", blockCarrierMap);
        }
    }

    private static void initBlockCarrierMap(
            Variant var,
            SearchFilter filter,
            int currentBlockId,
            HashMap<Integer, HashMap<Integer, Carrier>> blockCarrierMap) throws Exception {
        StringBuilder sqlSB = new StringBuilder();

        sqlSB.append("SELECT c.sample_id,variant_id,block_id,GT,DP,AD_REF,AD_ALT,GQ,VQSLOD,SOR,FS,MQ,QD,QUAL,ReadPosRankSum,MQRankSum,FILTER+0 ");
        sqlSB.append("FROM called_variant_chr").append(var.getChr()).append(" c,");

        if (filter.isQueryVariant()) // variant 
        {
            sqlSB.append("full_impact,");
        } else { // gene or region
            sqlSB.append("low_impact,");
        }

        sqlSB.append("sample s ");
        sqlSB.append("WHERE block_id=?");
        sqlSB.append(" AND highest_impact=input_impact ");
        sqlSB.append("AND c.sample_id=s.sample_id ");
        sqlSB.append("AND").append(filter.getSampleSQL());
        sqlSB.append(filter.getPhenotypeSQL());
        sqlSB.append(filter.getAvailableControlUseSQL());

        HashMap<Integer, Integer> validVariantCarrierCount = new HashMap<>();

        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSB.toString());
        preparedStatement.setInt(1, currentBlockId);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            Carrier carrier = new Carrier(rs);

            int variantId = rs.getInt("variant_id");

            HashMap<Integer, Carrier> varCarrierMap = blockCarrierMap.get(variantId);

            if (varCarrierMap == null) {
                varCarrierMap = new HashMap<>();
                blockCarrierMap.put(variantId, varCarrierMap);

                validVariantCarrierCount.put(variantId, 0);
            }

            if (carrier.isValid()) {
                validVariantCarrierCount.computeIfPresent(variantId, (k, v) -> v + 1);
            }

            varCarrierMap.put(carrier.getSampleId(), carrier);
        }

        rs.close();
        preparedStatement.close();

        // removed no qualified carriers variant
        validVariantCarrierCount.entrySet().stream().filter((entry) -> (entry.getValue() == 0)).forEachOrdered((entry) -> {
            blockCarrierMap.remove(entry.getKey());
        });
    }

    public static void initCarrierMap(HashMap<Integer, Carrier> carrierMap, Variant var, SearchFilter filter) throws Exception {
        int blockId = Math.floorDiv(var.getPos(), CARRIER_BLOCK_SIZE);

        StringBuilder sqlSB = new StringBuilder();

        sqlSB.append("SELECT c.sample_id,variant_id,block_id,GT,DP,AD_REF,AD_ALT,GQ,VQSLOD,SOR,FS,MQ,QD,QUAL,ReadPosRankSum,MQRankSum,FILTER+0 ");
        sqlSB.append("FROM called_variant_chr").append(var.getChr()).append(" c,");

        if (filter.isQueryVariant()) // variant 
        {
            sqlSB.append("full_impact,");
        } else { // gene or region
            sqlSB.append("low_impact,");
        }

        sqlSB.append("sample s ");
        sqlSB.append("WHERE block_id=?");
        sqlSB.append(" AND highest_impact=input_impact ");
        sqlSB.append("AND c.sample_id=s.sample_id ");
        sqlSB.append("AND variant_id=").append(var.getVariantId());
        sqlSB.append(" AND").append(filter.getSampleSQL());
        sqlSB.append(filter.getPhenotypeSQL());
        sqlSB.append(filter.getAvailableControlUseSQL());

        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSB.toString());
        preparedStatement.setInt(1, blockId);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            Carrier carrier = new Carrier(rs);

            carrier.applyQualityFilter(filter, var.isSnv());

            carrierMap.put(carrier.getSampleId(), carrier);
        }

        rs.close();
        preparedStatement.close();
    }

    public static HashMap<Integer, Carrier> getVarCarrierMap(int variantId, ModelAndView mv) {
        HashMap<Integer, HashMap<Integer, Carrier>> blockCarrierMap
                = (HashMap<Integer, HashMap<Integer, Carrier>>) mv.getModel().get("blockCarrierMap");

        return blockCarrierMap == null ? null : blockCarrierMap.get(variantId);
    }
}
