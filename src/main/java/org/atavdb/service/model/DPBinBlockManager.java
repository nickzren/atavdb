package org.atavdb.service.model;

import org.atavdb.service.util.DBManager;
import org.atavdb.model.SearchFilter;
import org.atavdb.global.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import org.atavdb.model.Carrier;
import org.atavdb.model.NonCarrier;
import org.atavdb.model.SampleDPBin;
import org.atavdb.model.Variant;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author nick
 */
public class DPBinBlockManager {

    public static final int DP_BIN_BLOCK_SIZE = 1000;
    
    private static final HashMap<Character, Short> dpBin = new HashMap<>();

    static {
        dpBin.put('b', Data.SHORT_NA);
        dpBin.put('c', (short) 10);
        dpBin.put('d', (short) 20);
        dpBin.put('e', (short) 30);
        dpBin.put('f', (short) 50);
        dpBin.put('g', (short) 200);
    }

    public static void initCarrierAndNonCarrierByDPBin(
            Variant var,
            HashMap<Integer, Carrier> carrierMap,
            HashMap<Integer, NonCarrier> noncarrierMap,
            SearchFilter filter,
            ModelAndView mv) throws Exception {
        int posIndex = var.getStartPosition() % DP_BIN_BLOCK_SIZE;

        Integer currentBlockId = (Integer) mv.getModel().get("currentNonCarrierBlockId");

        ArrayList<SampleDPBin> currentBlockList = (ArrayList<SampleDPBin>) mv.getModel().get("currentBlockList");

        int blockId = Math.floorDiv(var.getStartPosition(), DP_BIN_BLOCK_SIZE);

        if (currentBlockId != null && blockId == currentBlockId) {
            for (SampleDPBin sampleDPBin : currentBlockList) {
                Carrier carrier = carrierMap.get(sampleDPBin.getSampleId());

                if (carrier != null) {
                    carrier.applyQualityFilter(filter, var.isSnv());

                    if (carrier.isValid()) {
                        carrier.setDPBin(sampleDPBin.getDPBin(posIndex));

                        carrier.applyCoverageFilter(filter);
                    }
                } else {
                    NonCarrier noncarrier = new NonCarrier(sampleDPBin.getSampleId(), sampleDPBin.getDPBin(posIndex));

                    noncarrier.applyCoverageFilter(filter);

                    if (noncarrier.isValid()) {
                        noncarrierMap.put(noncarrier.getSampleId(), noncarrier);
                    }
                }
            }
        } else {
            currentBlockId = blockId;

            currentBlockList = new ArrayList<>();

            initBlockDPBin(carrierMap, noncarrierMap, var, posIndex, blockId, filter, currentBlockList);

            mv.addObject("currentNonCarrierBlockId", currentBlockId);
            mv.addObject("currentBlockList", currentBlockList);
        }
    }

    public static void initBlockDPBin(
            HashMap<Integer, Carrier> carrierMap,
            HashMap<Integer, NonCarrier> noncarrierMap,
            Variant var,
            int posIndex,
            int blockId,
            SearchFilter filter,
            ArrayList<SampleDPBin> currentBlockList) throws Exception {
        StringBuilder sqlSB = new StringBuilder();

        sqlSB.append("SELECT d.sample_id, DP_string FROM DP_bins_chr" + var.getChrStr() + " d, sample s ");
        sqlSB.append("WHERE block_id=? AND d.sample_id=s.sample_id AND");
        sqlSB.append(filter.getSampleSQL());
        sqlSB.append(filter.getPhenotypeSQL());
        sqlSB.append(filter.getAvailableControlUseSQL());

        Connection connection = DBManager.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlSB.toString());
        preparedStatement.setInt(1, blockId);
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            NonCarrier noncarrier = new NonCarrier(
                    rs.getInt("sample_id"),
                    rs.getString("DP_string"),
                    posIndex,
                    currentBlockList);

            Carrier carrier = carrierMap.get(noncarrier.getSampleId());

            if (carrier != null) {
                carrier.applyQualityFilter(filter, var.isSnv());
                
                if (carrier.isValid()) {
                    carrier.setDPBin(noncarrier.getDPBin());

                    carrier.applyCoverageFilter(filter);
                }
            } else {
                noncarrier.applyCoverageFilter(filter);

                if (noncarrier.isValid()) {
                    noncarrierMap.put(noncarrier.getSampleId(), noncarrier);
                }
            }
        }
        rs.close();
        preparedStatement.close();
    }

    public static short getCoverageByBin(Character bin) {
        return dpBin.get(bin);
    }

    public static HashMap<Character, Short> getCoverageBin() {
        return dpBin;
    }
}
