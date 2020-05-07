package model;

import global.Data;
import global.Index;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import util.DBManager;

/**
 * @author nick, qwang
 */
public class DPBinBlockManager {

    public static final int DP_BIN_BLOCK_SIZE = 1000;

    private static ArrayList<SampleDPBin> currentBlockList = new ArrayList<>();
    private static int currentBlockId = Data.INTEGER_NA;

    private static HashMap<Character, Short> dpBin = new HashMap<>();
    private static HashMap<Short, Byte> dpBinIndex = new HashMap<>();

    public static void init() {
        dpBin.put('b', Data.SHORT_NA);
        dpBin.put('c', (short) 10);
        dpBin.put('d', (short) 20);
        dpBin.put('e', (short) 30);
        dpBin.put('f', (short) 50);
        dpBin.put('g', (short) 200);

        dpBinIndex.put(Data.SHORT_NA, Data.BYTE_NA);
        dpBinIndex.put((short) 10, Index.DP_BIN_10);
        dpBinIndex.put((short) 20, Index.DP_BIN_20);
        dpBinIndex.put((short) 30, Index.DP_BIN_30);
        dpBinIndex.put((short) 50, Index.DP_BIN_50);
        dpBinIndex.put((short) 200, Index.DP_BIN_200);
    }

    public static void add(SampleDPBin sampleDPBin) {
        currentBlockList.add(sampleDPBin);
    }

    public static void initCarrierAndNonCarrierByDPBin(Variant var,
            HashMap<Integer, Carrier> carrierMap,
            HashMap<Integer, NonCarrier> noncarrierMap,
            Filter filter) {
        int posIndex = var.getStartPosition() % DP_BIN_BLOCK_SIZE;

        int blockId = Math.floorDiv(var.getStartPosition(), DP_BIN_BLOCK_SIZE);

        if (blockId == currentBlockId) {
            for (SampleDPBin sampleDPBin : currentBlockList) {
                Carrier carrier = carrierMap.get(sampleDPBin.getSampleId());

                if (carrier != null) {
                    if (carrier.isValid()) {
                        carrier.setDPBin(sampleDPBin.getDPBin(posIndex));
                    }
                } else {
                    NonCarrier noncarrier = new NonCarrier(sampleDPBin.getSampleId(),
                            sampleDPBin.getDPBin(posIndex));

                    if (noncarrier.isValid()) {
                        noncarrierMap.put(noncarrier.getSampleId(), noncarrier);
                    }
                }
            }
        } else {
            currentBlockId = blockId;
            currentBlockList.clear();

            initBlockDPBin(carrierMap, noncarrierMap, var, posIndex, blockId, filter);
        }
    }

    public static void initBlockDPBin(
            HashMap<Integer, Carrier> carrierMap,
            HashMap<Integer, NonCarrier> noncarrierMap,
            Variant var,
            int posIndex,
            int blockId,
            Filter filter) {
        try {
            String sql = "SELECT d.sample_id, DP_string FROM DP_bins_chr" + var.getChrStr() + " d, sample s"
                    + " WHERE block_id = " + blockId + " AND d.sample_id = s.sample_id"
                    + " AND sample_finished=1"
                    + " AND sample_failure=0"
                    + filter.getPhenotypeSQL();

            ResultSet rs = DBManager.executeQuery(sql);
            while (rs.next()) {
                NonCarrier noncarrier = new NonCarrier(rs.getInt("sample_id"), rs.getString("DP_string"), posIndex);

                Carrier carrier = carrierMap.get(noncarrier.getSampleId());

                if (carrier != null) {
                    if (carrier.isValid()) {
                        carrier.setDPBin(noncarrier.getDPBin());
                    }
                } else {
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

    public static byte getCoverageByBinIndex(short bin) {
        return dpBinIndex.get(bin);
    }

    public static HashMap<Character, Short> getCoverageBin() {
        return dpBin;
    }
}
