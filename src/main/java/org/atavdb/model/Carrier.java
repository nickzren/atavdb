package org.atavdb.model;

import org.atavdb.service.FilterManager;
import org.atavdb.global.Data;
import org.atavdb.global.Enum.FILTER;
import org.atavdb.global.Enum.GT;
import java.sql.ResultSet;
import org.atavdb.service.FormatManager;

/**
 *
 * @author nick
 */
public class Carrier extends NonCarrier {

    Sample sample;
    private short dp;
    private short adRef;
    private short adAlt;
    private byte gq;
    private float vqslod;
    private float sor;
    private float fs;
    private byte mq;
    private byte qd;
    private int qual;
    private float readPosRankSum;
    private float mqRankSum;
    private byte filterValue;

    public Carrier(ResultSet rs) throws Exception {
        sampleId = rs.getInt("sample_id");
        gt = rs.getByte("GT");
        dp = rs.getShort("DP");
        dpBin = Data.SHORT_NA;
        adRef = rs.getShort("AD_REF");
        adAlt = rs.getShort("AD_ALT");
        gq = FormatManager.getByte(rs, "GQ");
        vqslod = FormatManager.getFloat(rs, "VQSLOD");
        sor = FormatManager.getFloat(rs, "SOR");
        fs = FormatManager.getFloat(rs, "FS");
        mq = FormatManager.getByte(rs, "MQ");
        qd = FormatManager.getByte(rs, "QD");
        qual = FormatManager.getInt(rs, "QUAL");
        readPosRankSum = FormatManager.getFloat(rs, "ReadPosRankSum");
        mqRankSum = FormatManager.getFloat(rs, "MQRankSum");
        filterValue = FormatManager.getByte(rs, "FILTER+0");
    }
    
    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public short getDP() {
        return dp;
    }

    public short getADRef() {
        return adRef;
    }

    public short getADAlt() {
        return adAlt;
    }

    public byte getGQ() {
        return gq;
    }

    public float getVQSLOD() {
        return vqslod;
    }

    public float getSOR() {
        return sor;
    }

    public float getFS() {
        return fs;
    }

    public byte getMQ() {
        return mq;
    }

    public byte getQD() {
        return qd;
    }

    public int getQual() {
        return qual;
    }

    public float getReadPosRankSum() {
        return readPosRankSum;
    }

    public float getMQRankSum() {
        return mqRankSum;
    }

    public String getFILTER() {
        return FILTER.valueOf(filterValue);
    }

    public String getGTStr() {
        return GT.valueOf(gt);        
    }

    public int getExperimentId() {
        return sample.getExperimentId();
    }

    public String getGender() {
        return sample.getGender().name();
    }

    public String getPhenotype() {
        return sample.getBroadPhenotype();
    }
    
    public String getEthnicity() {
        return sample.getEthnicity().name();
    }
    
    public String getAvailableControlUse() {
        return sample.getAvailableControlUse();
    }

    public void applyQualityFilter(FilterManager filter, boolean isSnv) {
        if (gt != GT.NA.value()) {
            if (!filter.isFilterValid(filterValue)
                    || !filter.isMinGqValid(gq)
                    || !filter.isMaxSorValid(sor, isSnv)
                    || !filter.isMaxFsValid(fs, isSnv)
                    || !filter.isMinMqValid(mq)
                    || !filter.isMinQdValid(qd)
                    || !filter.isMinQualValid(qual)
                    || !filter.isMinRprsValid(readPosRankSum)
                    || !filter.isMinMqrsValid(mqRankSum)) {
                gt = GT.NA.value();
            }
        }
    }
    
    /*
     * Outside of pseudoautosomal regions:
     *
     * ChrX: females are treated normally
     *
     * ChrX: males have het removed
     *
     * ChrY: females are set to missing
     *
     * ChrY: males have het removed
     *
     * Inside of pseudoautosomal region which are treated like autosomes.
     */
    protected void checkValidOnXY(Region r) {
        if (gt != GT.NA.value()) {
            boolean isValid = true;

            if (sample.isMale()) {
                if (gt == GT.HET.value() // male het chr x or y & outside PARs
                        && !r.isInsideAutosomalOrPseudoautosomalRegions()) {
                    isValid = false;
                }
            } else if (r.getChrNum() == 24 // female chy & outside PARs
                    && !r.isInsideYPseudoautosomalRegions()) {
                isValid = false;
            }

            if (!isValid) {
                gt = Data.BYTE_NA;
            }
        }
    }
}
