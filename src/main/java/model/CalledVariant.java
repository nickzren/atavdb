package model;

import global.Data;
import global.Enum.GT;
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

    private int[] genoCount = new int[3];
    private int[] genderCount = new int[4];
    private int ac;
    private int an;
    public float af;

    public CalledVariant(String chr, ResultSet rset, FilterManager filter) throws Exception {
        super(chr, rset);

        init(filter);
    }

    private void init(FilterManager filter) throws Exception {
        if (isValid
                && initCarrierData(filter)) {
            DPBinBlockManager.initCarrierAndNonCarrierByDPBin(this, carrierMap, noncarrierMap, filter);

            initGenoCovArray(filter);

            calculateAF();

            isValid = FilterManager.isMinVarPresentValid(carrierMap.size())
                    && filter.isMaxAFValid(af);

            // if gene / region search then free carriers
            // if variant search when af > 0.01 then free carriers
            if (!filter.getQueryType().equals((Data.QUERT_TYPE[1])) || af > 0.01) {
                carrierMap = null;
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

    // initialize genotype & dpBin array for better compute performance use
    private void initGenoCovArray(FilterManager filter) {
        for (Sample sample : SampleManager.getList(filter)) {
            Carrier carrier = carrierMap.get(sample.getId());
            NonCarrier noncarrier = noncarrierMap.get(sample.getId());

            if (carrier != null) {
                if (!filter.isMinDpBinValid(carrier.getDPBin())) {
                    carrier.setGT(GT.NA.value());
                    carrier.setDPBin(Data.SHORT_NA);
                }

                if (carrier.getGT() == GT.NA.value()) {
                    // have to remove it for init Non-carrier map
                    carrierMap.remove(sample.getId());
                } else {
                    countGeno(carrier.getGT());
                    countGender(carrier.getGT(), sample);
                    carrier.setSample(sample);
                }
            } else if (noncarrier != null) {
                if (!filter.isMinDpBinValid(noncarrier.getDPBin())) {
                    noncarrier.setGT(GT.NA.value());
                    noncarrier.setDPBin(Data.SHORT_NA);
                }

                countGeno(noncarrier.getGT());
            }
        }

        noncarrierMap = null; // free memory
    }

    public void countGeno(byte geno) {
        if (geno != GT.NA.value()) {
            genoCount[geno]++;
        }
    }

    public void countGender(byte geno, Sample sample) {
        if (geno == GT.HOM.value() || geno == GT.HET.value()) {
            genderCount[sample.getGender().getIndex()]++;
        }
    }

    private void calculateAF() {
        ac = 2 * genoCount[GT.HOM.value()] + genoCount[GT.HET.value()];

        an = ac + genoCount[GT.HET.value()] + 2 * genoCount[GT.REF.value()];

        af = MathManager.devide(ac, an);
    }

    public Carrier getCarrier(int sampleId) {
        return carrierMap.get(sampleId);
    }

    public Collection<Carrier> getCarriers() {
        return carrierMap == null ? null : carrierMap.values();
    }

    // NS = Number of Samples With Data
    public int getNS() {
        return genoCount[GT.HOM.value()] + genoCount[GT.HET.value()] + genoCount[GT.REF.value()];
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
        return genoCount[GT.HOM.value()];
    }

    public int[] getGenderCount() {
        return genderCount;
    }
}
