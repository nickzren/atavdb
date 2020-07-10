package org.atavdb.model;

import org.atavdb.service.FilterManager;
import org.atavdb.global.Data;
import org.atavdb.global.Enum.GT;
import java.util.ArrayList;

/**
 *
 * @author nick
 */
public class NonCarrier {

    protected int sampleId;
    protected byte gt;
    protected short dpBin;

    public NonCarrier() {
    }

    public NonCarrier(int sampleId, short dpBin) {
        this.sampleId = sampleId;
        this.dpBin = dpBin;

        initGenotype();
    }

    public NonCarrier(int sampleId, String minDPBin, int posIndex, ArrayList<SampleDPBin> currentBlockList) throws Exception {
        this.sampleId = sampleId;
        SampleDPBin sampleDPBin = new SampleDPBin(sampleId, minDPBin);
        currentBlockList.add(sampleDPBin);
        dpBin = sampleDPBin.getDPBin(posIndex);

        initGenotype();
    }

    private void initGenotype() {
        if (dpBin == Data.SHORT_NA) {
            gt = GT.NA.value();
        } else {
            gt = GT.REF.value();
        }
    }

    public int getSampleId() {
        return sampleId;
    }

    public void setGT(byte value) {
        gt = value;
    }

    public byte getGT() {
        return gt;
    }

    public void setDPBin(short value) {
        dpBin = value;
    }

    public short getDPBin() {
        return dpBin;
    }

    public void applyCoverageFilter(FilterManager filter) {
        if (!filter.isMinDpBinValid(dpBin)) {
            gt = Data.BYTE_NA;
        }
    }
    
    public boolean isValid() {
        return gt != GT.NA.value();
    }
}

