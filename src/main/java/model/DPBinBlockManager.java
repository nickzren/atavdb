package model;

import global.Data;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import util.DBManager;

/**
 * @author nick
 */
public class DPBinBlockManager {

    public static final int DP_BIN_BLOCK_SIZE = 1000;

    private static HashMap<Character, Short> dpBin = new HashMap<>();

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
            FilterManager filter,
            HttpSession session) {
        int posIndex = var.getStartPosition() % DP_BIN_BLOCK_SIZE;

        Integer currentBlockId = (Integer) session.getAttribute("currentNonCarrierBlockId");

        ArrayList<SampleDPBin> currentBlockList = (ArrayList<SampleDPBin>) session.getAttribute("currentBlockList");

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
                    NonCarrier noncarrier = new NonCarrier(sampleDPBin.getSampleId(),
                            sampleDPBin.getDPBin(posIndex));

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
            
            session.setAttribute("currentNonCarrierBlockId", currentBlockId);
            session.setAttribute("currentBlockList", currentBlockList);
        }
    }

    public static void initBlockDPBin(
            HashMap<Integer, Carrier> carrierMap,
            HashMap<Integer, NonCarrier> noncarrierMap,
            Variant var,
            int posIndex,
            int blockId,
            FilterManager filter,
            ArrayList<SampleDPBin> currentBlockList) {
        try {
            String sql = "SELECT d.sample_id, DP_string FROM DP_bins_chr" + var.getChrStr() + " d, sample s"
                    + " WHERE block_id = " + blockId + " AND d.sample_id = s.sample_id"
                    + " AND sample_finished=1"
                    + " AND sample_failure=0"
                    + " AND sample_type!='custom_capture'"
                    + filter.getPhenotypeSQL();

            ResultSet rs = DBManager.executeQuery(sql);
            while (rs.next()) {
                NonCarrier noncarrier = new NonCarrier(rs.getInt("sample_id"), rs.getString("DP_string"), posIndex, currentBlockList);

                Carrier carrier = carrierMap.get(noncarrier.getSampleId());

                if (carrier != null) {
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
        } catch (Exception e) {
        }
    }

    public static short getCoverageByBin(Character bin) {
        return dpBin.get(bin);
    }

    public static HashMap<Character, Short> getCoverageBin() {
        return dpBin;
    }
}
