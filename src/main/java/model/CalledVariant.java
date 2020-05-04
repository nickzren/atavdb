package model;

import global.Data;
import global.Index;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
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
    public float[] homFreq = new float[2];
    public float[] hetFreq = new float[2];
    public float[] af = new float[2];

    public CalledVariant(String chr, ResultSet rset) throws Exception {        
        super(chr, rset);
    }

    public void init() throws Exception {
        CarrierBlockManager.initCarrierMap(carrierMap, this);

        DPBinBlockManager.initCarrierAndNonCarrierByDPBin(this, carrierMap, noncarrierMap);

        initGenoCovArray();

        calculateAlleleFreq();

        calculateGenotypeFreq();
    }

    // initialize genotype & dpBin array for better compute performance use
    private void initGenoCovArray() {
        for (Sample sample : SampleManager.getList()) {
            Carrier carrier = carrierMap.get(sample.getId());
            NonCarrier noncarrier = noncarrierMap.get(sample.getId());

            if (carrier != null) {
                setGenoDPBin(carrier.getGT(), carrier.getDPBin(), sample.getIndex());
                addSampleGeno(carrier.getGT(), sample);

                if (carrier.getGT() == Data.BYTE_NA) {
                    // have to remove it for init Non-carrier map
                    qcFailSample[sample.getPheno()]++;
                    carrierMap.remove(sample.getId());
                }

            } else if (noncarrier != null) {
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

    public void calculate() {
        calculateAlleleFreq();

        calculateGenotypeFreq();
    }

    private void calculateAlleleFreq() {
        int caseAC = 2 * genoCount[Index.HOM][Index.CASE]
                + genoCount[Index.HET][Index.CASE];
        int caseTotalAC = caseAC + genoCount[Index.HET][Index.CASE]
                + 2 * genoCount[Index.REF][Index.CASE];

        // (2*hom + maleHom + het) / (2*hom + maleHom + 2*het + 2*ref + maleRef)
        af[Index.CASE] = MathManager.devide(caseAC, caseTotalAC);

        int ctrlAC = 2 * genoCount[Index.HOM][Index.CTRL]
                + genoCount[Index.HET][Index.CTRL];
        int ctrlTotalAC = ctrlAC + genoCount[Index.HET][Index.CTRL]
                + 2 * genoCount[Index.REF][Index.CTRL];

        af[Index.CTRL] = MathManager.devide(ctrlAC, ctrlTotalAC);
    }

    private void calculateGenotypeFreq() {
        int totalCaseGenotypeCount
                = genoCount[Index.HOM][Index.CASE]
                + genoCount[Index.HET][Index.CASE]
                + genoCount[Index.REF][Index.CASE];

        int totalCtrlGenotypeCount
                = genoCount[Index.HOM][Index.CTRL]
                + genoCount[Index.HET][Index.CTRL]
                + genoCount[Index.REF][Index.CTRL];

        // hom / (hom + het + ref)
        homFreq[Index.CASE] = MathManager.devide(
                genoCount[Index.HOM][Index.CASE], totalCaseGenotypeCount);
        homFreq[Index.CTRL] = MathManager.devide(
                genoCount[Index.HOM][Index.CTRL], totalCtrlGenotypeCount);

        hetFreq[Index.CASE] = MathManager.devide(genoCount[Index.HET][Index.CASE], totalCaseGenotypeCount);
        hetFreq[Index.CTRL] = MathManager.devide(genoCount[Index.HET][Index.CTRL], totalCtrlGenotypeCount);
    }

    public short getDPBin(int index) {
        if (index == Data.INTEGER_NA) {
            return Data.SHORT_NA;
        }

        return dpBin[index];
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
        return genoCount[Index.HOM][Index.CASE]
                + genoCount[Index.HET][Index.CASE]
                + genoCount[Index.REF][Index.CASE]
                + genoCount[Index.HOM][Index.CTRL]
                + genoCount[Index.HET][Index.CTRL]
                + genoCount[Index.REF][Index.CTRL];
    }
    
    // AC = Allele Count
    public int getAC() {
        return 2 * genoCount[Index.HOM][Index.CASE]
                + genoCount[Index.HET][Index.CASE]
                + 2 * genoCount[Index.HOM][Index.CTRL]
                + genoCount[Index.HET][Index.CTRL];
    }

    // AN = Allele Number
    public int getAN() {
        return getAC() + genoCount[Index.HET][Index.CASE]
                + 2 * genoCount[Index.REF][Index.CASE]
                + genoCount[Index.HET][Index.CTRL]
                + 2 * genoCount[Index.REF][Index.CTRL];
    }
    
    // AF = Allele Frequency
    public float getAF() {
        return MathManager.devide(getAC(), getAN());
    }
    
    // NH = Number of homozygotes
    public int getNH() {
        return genoCount[Index.HOM][Index.CASE] +
                 genoCount[Index.HOM][Index.CTRL];
    }
}
