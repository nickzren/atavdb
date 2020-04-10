package model;

import global.Data;
import java.sql.ResultSet;
import util.FormatManager;
import util.MathManager;

/**
 *
 * @author nick
 */
public class Carrier extends NonCarrier {

    public static final String[] FILTER = {"PASS", "LIKELY", "INTERMEDIATE", "FAIL"};
    
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
    private byte filterValue; // PASS(1), LIKELY(2), INTERMEDIATE(3), FAIL(4)
    private int pidVariantId;
    private int hpVariantId;

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
        filterValue = rs.getByte("FILTER+0");
        // PGT = 1 indicate variant is in phase
        if (rs.getByte("PGT") == 1) {
            pidVariantId = FormatManager.getInt(rs, "PID_variant_id");
        } else {
            pidVariantId = Data.INTEGER_NA;
        }
        // HP_GT = 1 indicate variant is in phase
        if (rs.getByte("HP_GT") == 1) {
            hpVariantId = FormatManager.getInt(rs, "HP_variant_id");
        } else {
            hpVariantId = Data.INTEGER_NA;
        }
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
        return FILTER[filterValue - 1];
    }

    public String getPercAltRead() {
        return FormatManager.getFloat(MathManager.devide(adAlt, dp));
    }

    public double getPercentAltReadBinomialP() {
        if (adAlt == Data.SHORT_NA || adRef == Data.SHORT_NA) {
            return Data.DOUBLE_NA;
        } else {
            return MathManager.getBinomialLessThan(adAlt + adRef, adAlt, 0.5f);
        }
    }

    public int getPIDVariantId() {
        return pidVariantId;
    }

    public int getHPVariantId() {
        return hpVariantId;
    }
}