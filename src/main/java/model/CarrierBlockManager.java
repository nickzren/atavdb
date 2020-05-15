package model;

import global.Data;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map.Entry;
import util.DBManager;

/**
 *
 * @author nick
 */
public class CarrierBlockManager {

    public static final int CARRIER_BLOCK_SIZE = 1000;

    private static int currentBlockId = Data.INTEGER_NA;

    private static HashMap<Integer, HashMap<Integer, Carrier>> blockCarrierMap = new HashMap<>(); // variantId <SampleId, CarrierMap> 

    public static void init(Variant var, FilterManager filter) {
        int blockId = Math.floorDiv(var.getStartPosition(), CARRIER_BLOCK_SIZE);

        if (currentBlockId != blockId) {
            currentBlockId = blockId;

            blockCarrierMap.clear();

            initBlockCarrierMap(var, filter);
        }
    }

    private static void initBlockCarrierMap(Variant var, FilterManager filter) {
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

            ResultSet rs = DBManager.executeQuery(sqlSB.toString());

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

            // removed no qualified carriers variant
            for (Entry<Integer, Integer> entry : validVariantCarrierCount.entrySet()) {
                if (entry.getValue() == 0) {
                    blockCarrierMap.remove(entry.getKey());
                }
            }
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
            ResultSet rs = DBManager.executeQuery(sqlSB.toString());

            while (rs.next()) {
                Carrier carrier = new Carrier(rs);

                carrier.applyQualityFilter(filter, var.isSnv());

                carrierMap.put(carrier.getSampleId(), carrier);
            }

            rs.close();
        } catch (Exception e) {
        }
    }

    public static HashMap<Integer, Carrier> getVarCarrierMap(int variantId) {
        return blockCarrierMap.get(variantId);
    }
}
