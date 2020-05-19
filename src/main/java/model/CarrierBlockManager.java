package model;

import global.Data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import util.DBManager;

/**
 *
 * @author nick
 */
public class CarrierBlockManager {

    public static final int CARRIER_BLOCK_SIZE = 1000;

    public static void init(Variant var, FilterManager filter, HttpServletRequest request) {
        int blockId = Math.floorDiv(var.getStartPosition(), CARRIER_BLOCK_SIZE);

        Integer currentBlockId = (Integer) request.getAttribute("currentCarrierBlockId");

        if (currentBlockId == null || currentBlockId != blockId) {
            currentBlockId = blockId;

            HashMap<Integer, HashMap<Integer, Carrier>> blockCarrierMap = new HashMap<>();

            initBlockCarrierMap(var, filter, blockId, blockCarrierMap);

            request.setAttribute("currentCarrierBlockId", currentBlockId);
            request.setAttribute("blockCarrierMap", blockCarrierMap);
        }
    }

    private static void initBlockCarrierMap(
            Variant var,
            FilterManager filter,
            int currentBlockId,
            HashMap<Integer, HashMap<Integer, Carrier>> blockCarrierMap) {
        StringBuilder sqlSB = new StringBuilder();

        sqlSB.append("SELECT c.sample_id,variant_id,block_id,GT,DP,AD_REF,AD_ALT,GQ,VQSLOD,SOR,FS,MQ,QD,QUAL,ReadPosRankSum,MQRankSum,FILTER+0 ");
        sqlSB.append("FROM called_variant_chr").append(var.getChrStr()).append(" c,");

        if (filter.getQueryType().equals(Data.QUERT_TYPE[1])) // variant 
        {
            sqlSB.append("full_impact,");
        } else { // gene or region
            sqlSB.append("low_impact,");
        }

        sqlSB.append("sample s ");
        sqlSB.append("WHERE block_id=").append(currentBlockId);
        sqlSB.append(" AND highest_impact=input_impact");
        sqlSB.append(" AND c.sample_id=s.sample_id ");
        sqlSB.append(" AND sample_finished=1");
        sqlSB.append(" AND sample_failure=0");
        sqlSB.append(" AND sample_type!='custom_capture'");
        sqlSB.append(filter.getPhenotypeSQL());

        try {
            HashMap<Integer, Integer> validVariantCarrierCount = new HashMap<>();

            Connection connection = DBManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlSB.toString());

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
            statement.close();

            // removed no qualified carriers variant
            validVariantCarrierCount.entrySet().parallelStream().filter((entry) -> (entry.getValue() == 0)).forEachOrdered((entry) -> {
                blockCarrierMap.remove(entry.getKey());
            });
        } catch (Exception e) {
        }
    }

    public static void initCarrierMap(HashMap<Integer, Carrier> carrierMap, Variant var, FilterManager filter) {
        int blockId = Math.floorDiv(var.getStartPosition(), CARRIER_BLOCK_SIZE);

        StringBuilder sqlSB = new StringBuilder();

        sqlSB.append("SELECT c.sample_id,variant_id,block_id,GT,DP,AD_REF,AD_ALT,GQ,VQSLOD,SOR,FS,MQ,QD,QUAL,ReadPosRankSum,MQRankSum,FILTER+0 ");
        sqlSB.append("FROM called_variant_chr").append(var.getChrStr()).append(" c,");

        if (filter.getQueryType().equals(Data.QUERT_TYPE[1])) // variant 
        {
            sqlSB.append("full_impact,");
        } else { // gene or region
            sqlSB.append("low_impact,");
        }

        sqlSB.append("sample s ");
        sqlSB.append("WHERE block_id=").append(blockId);
        sqlSB.append(" AND highest_impact=input_impact");
        sqlSB.append(" AND c.sample_id=s.sample_id ");
        sqlSB.append(" AND variant_id=").append(var.getVariantId());
        sqlSB.append(" AND sample_finished=1");
        sqlSB.append(" AND sample_failure=0");
        sqlSB.append(" AND sample_type!='custom_capture'");
        sqlSB.append(filter.getPhenotypeSQL());

        try {
            Connection connection = DBManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sqlSB.toString());

            while (rs.next()) {
                Carrier carrier = new Carrier(rs);

                carrier.applyQualityFilter(filter, var.isSnv());

                carrierMap.put(carrier.getSampleId(), carrier);
            }
            
            rs.close();
            statement.close();
        } catch (Exception e) {
        }
    }

    public static HashMap<Integer, Carrier> getVarCarrierMap(int variantId, HttpServletRequest request) {
        HashMap<Integer, HashMap<Integer, Carrier>> blockCarrierMap
                = (HashMap<Integer, HashMap<Integer, Carrier>>) request.getAttribute("blockCarrierMap");

        return blockCarrierMap == null ? null : blockCarrierMap.get(variantId);
    }
}
