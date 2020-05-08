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
    private byte[] gt = new byte[SampleManager.getTotalSampleNum()];
    private short[] dpBin = new short[SampleManager.getTotalSampleNum()];

    private int[] qcFailSample = new int[2];
    public int[][] genoCount = new int[5][2];
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
                if(carrier.is10xCovered()) {
                    coveredSample++;
                } 
                
                if (!filter.isMinDpBinValid(carrier.getDPBin())) {
                    carrier.setGT(Data.BYTE_NA);
                    carrier.setDPBin(Data.SHORT_NA);
                }
                
                setGenoDPBin(carrier.getGT(), carrier.getDPBin(), sample.getIndex());
                addSampleGeno(carrier.getGT(), sample);

                if (carrier.getGT() == Data.BYTE_NA) {
                    // have to remove it for init Non-carrier map
                    qcFailSample[sample.getPheno()]++;
                    carrierMap.remove(sample.getId());
                }

            } else if (noncarrier != null) {
                if(noncarrier.is10xCovered()) {
                    coveredSample++;
                }
                
                if (!filter.isMinDpBinValid(noncarrier.getDPBin())) {
                    noncarrier.setGT(Data.BYTE_NA);
                    noncarrier.setDPBin(Data.SHORT_NA);
                }
                
                setGenoDPBin(noncarrier.getGT(), noncarrier.getDPBin(), sample.getIndex());
                addSampleGeno(noncarrier.getGT(), sample);
            } else {
                setGenoDPBin(Data.BYTE_NA, Data.SHORT_NA, sample.getIndex());
            }
        }

        noncarrierMap = null; // free memory
    }

    public void addSampleGeno(byte geno, Sample sample) {
        if (geno != Data.BYTE_NA) {
            genoCount[geno][sample.getPheno()]++;
        }
    }

    public void deleteSampleGeno(byte geno, Sample sample) {
        if (geno != Data.BYTE_NA) {
            genoCount[geno][sample.getPheno()]--;
        }
    }

    private void setGenoDPBin(byte geno, short bin, int s) {
        gt[s] = geno;
        dpBin[s] = bin;
    }

    private void calculateAF() {
        ac = 2 * genoCount[Index.HOM][Index.CTRL]
                + genoCount[Index.HET][Index.CTRL];

        an = ac + genoCount[Index.HET][Index.CTRL]
                + 2 * genoCount[Index.REF][Index.CTRL];

        af = MathManager.devide(ac, an);
    }

    public byte getGT(int index) {
        if (index == Data.INTEGER_NA) {
            return Data.BYTE_NA;
        }

        return gt[index];
    }

    public String getGT4VCF(int index) {
        byte gt = getGT(index);

        switch (gt) {
            case Index.HOM:
                return "1/1";
            case Index.HET:
                return "1/0";
            case Index.REF:
                return "0/0";
            default:
                return "./.";
        }
    }

    public Carrier getCarrier(int sampleId) {
        return carrierMap.get(sampleId);
    }

    public Collection<Carrier> getCarriers() {
        return carrierMap.values();
    }

    public int getQcFailSample(byte pheno) {
        return qcFailSample[pheno];
    }

    // NS = Number of Samples With Data
    public int getNS() {
        return genoCount[Index.HOM][Index.CTRL]
                + genoCount[Index.HET][Index.CTRL]
                + genoCount[Index.REF][Index.CTRL];
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
        return genoCount[Index.HOM][Index.CTRL];
    }

    // Number of samples are over 10x coverage
    public int get10xSample() {
        return coveredSample;
    }
}
