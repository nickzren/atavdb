package model;

import global.Data;
import global.Index;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import util.FormatManager;
import util.MathManager;

/**
 *
 * @author nick
 */
public class CalledVariant extends AnnotatedVariant {

    private HashMap<Integer, Carrier> carrierMap = new HashMap<>();
    private HashMap<Integer, NonCarrier> noncarrierMap = new HashMap<>();

    public int[] genoCount = new int[5];
    private int ac;
    private int an;
    public float af;

    private int coveredSample; // 10x covered sample count

    public CalledVariant(String chr, ResultSet rset, FilterManager filter) throws Exception {
        super(chr, rset);

        init(filter);
    }

    private void init(FilterManager filter) throws Exception {
        CarrierBlockManager.initCarrierMap(carrierMap, this, filter);

        if (isValid
                && initCarrierData(filter)) {
            DPBinBlockManager.initCarrierAndNonCarrierByDPBin(this, carrierMap, noncarrierMap, filter);

            initGenoCovArray(filter);

            if (checkGenoCountValid()) {
                calculateAF();

                isValid = filter.isMaxAFValid(af);
            }
        }
    }

    private boolean initCarrierData(FilterManager filter) {
        if (filter.getQueryType().equals(Data.QUERT_TYPE[1])) { // variant search
            // single variant carriers data process
            CarrierBlockManager.initCarrierMap(carrierMap, this, filter);

            if (!FilterManager.isMinVarPresentValid(carrierMap.size())) {
                isValid = false;
            }
        } else {
            // block variants carriers data process
            CarrierBlockManager.init(this, filter);

            carrierMap = CarrierBlockManager.getVarCarrierMap(variantId);

            if (carrierMap == null) {
                carrierMap = new HashMap<>();
                isValid = false;
            } else if (!FilterManager.isMinVarPresentValid(carrierMap.size())) {
                isValid = false;
            }
        }

        return isValid;
    }

    private boolean checkGenoCountValid() {
        isValid = FilterManager.isMinVarPresentValid(carrierMap.size());

        return isValid;
    }

    // initialize genotype & dpBin array for better compute performance use
    private void initGenoCovArray(FilterManager filter) {
        for (Sample sample : SampleManager.getList()) {
            Carrier carrier = carrierMap.get(sample.getId());
            NonCarrier noncarrier = noncarrierMap.get(sample.getId());

            if (carrier != null) {
                if (carrier.is10xCovered()) {
                    coveredSample++;
                }

                if (!filter.isMinDpBinValid(carrier.getDPBin())) {
                    carrier.setGT(Data.BYTE_NA);
                    carrier.setDPBin(Data.SHORT_NA);
                }

                addSampleGeno(carrier.getGT(), sample);

                if (carrier.getGT() == Data.BYTE_NA) {
                    // have to remove it for init Non-carrier map
                    carrierMap.remove(sample.getId());
                }
            } else if (noncarrier != null) {
                if (noncarrier.is10xCovered()) {
                    coveredSample++;
                }

                if (!filter.isMinDpBinValid(noncarrier.getDPBin())) {
                    noncarrier.setGT(Data.BYTE_NA);
                    noncarrier.setDPBin(Data.SHORT_NA);
                }

                addSampleGeno(noncarrier.getGT(), sample);
            } 
        }

        noncarrierMap = null; // free memory
    }

    public void addSampleGeno(byte geno, Sample sample) {
        if (geno != Data.BYTE_NA) {
            genoCount[geno]++;
        }
    }

    public void deleteSampleGeno(byte geno, Sample sample) {
        if (geno != Data.BYTE_NA) {
            genoCount[geno]--;
        }
    }


    private void calculateAF() {
        ac = 2 * genoCount[Index.HOM] + genoCount[Index.HET];

        an = ac + genoCount[Index.HET] + 2 * genoCount[Index.REF];

        af = MathManager.devide(ac, an);
    }

    public Carrier getCarrier(int sampleId) {
        return carrierMap.get(sampleId);
    }

    public Collection<Carrier> getCarriers() {
        return carrierMap.values();
    }

    // NS = Number of Samples With Data
    public int getNS() {
        return genoCount[Index.HOM] + genoCount[Index.HET] + genoCount[Index.REF];
    }

    // AC = Allele Count
    public int getAC() {
        return ac;
    }

    // AN = Allele Number
    public int getAN() {
        return an;
    }

    // AF = Allele Frequency
    public String getAF() {
        return FormatManager.getFloat(af);
    }

    // NH = Number of homozygotes
    public int getNH() {
        return genoCount[Index.HOM];
    }

    // Number of samples are over 10x coverage
    public int get10xSample() {
        return coveredSample;
    }
}
