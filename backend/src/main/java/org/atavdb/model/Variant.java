package org.atavdb.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.atavdb.global.Data;
import org.atavdb.util.FormatManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.atavdb.global.Enum.GT;
import org.springframework.web.servlet.ModelAndView;
import org.atavdb.util.MathManager;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 *
 * @author nick
 */
@JsonAutoDetect(fieldVisibility = Visibility.PUBLIC_ONLY)
@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"variantIdStr", "variantIdStr2", "chr", "pos", "ref", "alt",
    "consequence", "impact", "effect", "geneName", "ac", "an", "af", "ns", "nhom", "maxEAF",
    "allAnnotation",
    "exAC", "genomeAsia", "gnomADExome", "gnomADGenome", "gme", "iranome", "topMed",
    "genderCount", "ancestryCount", "phenotypeCount", "carriers"})
public class Variant {

    private int variantId;
    private String variantIdStr;
    private String chr;
    private int pos;
    private String ref;
    private String alt;
    private int rsNumber;
    //Indel attributes
    private boolean isIndel;

    // annotation
    private String impact = "";
    private String effect = "";
    private String HGVS_c = "";
    private String HGVS_p = "";
    private float polyphenHumdiv = Data.FLOAT_NA;
    private String geneName = "";
    private List<Annotation> annotationList = new ArrayList<>();

    // external af
    private float maxExternalAF;
    private float exac;
    private float genomeAsia;
    private float gnomadExome;
    private float gnomadGenome;
    private float gme;
    private float iranome;
    private float topmed;

    // carrier & non-carrier
    private HashMap<Integer, Carrier> carrierMap = new HashMap<>();
    private HashMap<Integer, NonCarrier> noncarrierMap = new HashMap<>();

    private int[] genoCount = new int[3]; // REF, HET, HOM
    private int[] genderCount = new int[4]; // Male, Female, Ambiguous, NA
    private int[] ancestryCount = new int[7]; // African, Caucasian, EastAsian, Hispanic, MiddleEastern, SouthAsian, NA
    private int[] phenotypeCount = new int[40];
    private int ac; // allele count
    private int an; // allele number
    private float af; // allele frequency
    private int ns;
    private int nHom;

    private boolean isValid = true;

    public Variant(String chr, ResultSet rset, SearchFilter filter, ModelAndView mv) throws Exception {
        this.variantId = rset.getInt("variant_id");

        this.chr = chr;
        this.pos = rset.getInt("POS");
        this.ref = rset.getString("REF");
        this.alt = rset.getString("ALT");
        this.rsNumber = FormatManager.getInt(rset, "rs_number");

        this.isIndel = ref.length() != alt.length();

        this.variantIdStr = chr + "-" + pos + "-" + ref + "-" + alt;

        initExternalAF();

        if ((filter.isQueryGene() || filter.isQueryRegion())
                && filter.getPhenotype().isEmpty()
                && !ExternalDataManager.isIGMAFEmpty(filter)
                && !filter.isQueryByExperimentId()) {
            // gene/region search on all/public samples
            ExternalDataManager.setIGMAF(this, filter);
        } else {
            initCarrier(filter, mv);
        }

        // if not valid
        // if gene or region search and not query by experiment id then free non-display data
        if (!isValid
                || ((filter.isQueryGene() || filter.isQueryRegion()) && !filter.isQueryByExperimentId())) {
            carrierMap = null;
            genderCount = null;
            ancestryCount = null;
            annotationList = null;
        }

        // if variant search and when af > MAX AF & not query by experiment id then not display carrier data
        if (filter.isQueryVariant()
                && af > SearchFilter.MAX_AF_TO_DISPLAY_CARRIER
                && !filter.isQueryByExperimentId()) {
            carrierMap = null;
        }
    }

    private void initExternalAF() {
        exac = ExternalDataManager.getExAC(chr, pos, ref, alt);
        genomeAsia = ExternalDataManager.getGenomeAsia(chr, pos, ref, alt);
        gnomadExome = ExternalDataManager.getGenoADExome(chr, pos, ref, alt);
        gnomadGenome = ExternalDataManager.getGenoADGenome(chr, pos, ref, alt);
        gme = ExternalDataManager.getGME(chr, pos, ref, alt);
        iranome = ExternalDataManager.getIRANOME(chr, pos, ref, alt);
        topmed = ExternalDataManager.getTOPMED(chr, pos, ref, alt);

        maxExternalAF = Collections.max(Arrays.asList(exac, genomeAsia, gnomadExome,
                gnomadGenome, gme, iranome, topmed));
    }

    public boolean isExternalAFValid(SearchFilter filter) {
        return filter.isExternalMAFValid(exac)
                && filter.isExternalMAFValid(genomeAsia)
                && filter.isExternalMAFValid(gnomadExome)
                && filter.isExternalMAFValid(gnomadGenome)
                && filter.isExternalMAFValid(gme)
                && filter.isExternalMAFValid(iranome)
                && filter.isExternalMAFValid(topmed);
    }

    // init carrier and non-carrier data, calculate af and count genotype
    private void initCarrier(SearchFilter filter, ModelAndView mv) throws Exception {
        if (isValid
                && initCarrierData(filter, mv)) {
            DPBinBlockManager.initCarrierAndNonCarrierByDPBin(this, carrierMap, noncarrierMap, filter, mv);

            initGTCount(filter);

            calculateAF();

            isValid = SearchFilter.isMinVarPresentValid(carrierMap.size());
        }
    }

    public boolean isMAFValid(SearchFilter filter) {
        return filter.isMAFValid(af);
    }

    @JsonIgnore
    public int getVariantId() {
        return variantId;
    }

    // chr-pos-ref-alt
    public String getVariantIdStr() {
        return variantIdStr;
    }

    // chr:g.posref>alt
    @JsonProperty("variantIdStr2")
    public String getVariantIdStr2() {
        return "chr" + chr + ":g." + pos + ref + ">" + alt;
    }

    public String getChr() {
        return chr;
    }

    public int getPos() {
        return pos;
    }

    public String getAlt() {
        return alt;
    }

    public String getRef() {
        return ref;
    }

    @JsonIgnore
    public int getRsNumber() {
        return rsNumber;
    }

    @JsonIgnore
    public boolean isSnv() {
        return !isIndel;
    }

    public void update(Annotation annotation) {
        if (isValid) {
            if (effect.isEmpty()) { // init most damaging effect annotations
                impact = annotation.getImpact();
                effect = annotation.getEffect();
                HGVS_c = annotation.getHGVS_c();
                HGVS_p = annotation.getHGVS_p();
                geneName = annotation.getGeneName();
            }

            if (annotationList != null) {
                annotationList.add(annotation);
            }

            polyphenHumdiv = MathManager.max(polyphenHumdiv, annotation.getPolyphenHumdiv());
        }
    }

    public List<Annotation> getAllAnnotation() {
        return annotationList;
    }

    public String getImpact() {
        return impact;
    }

    public String getEffect() {
        return effect;
    }

    public String getConsequence() {
        return HGVS_p.equals(Data.STRING_NA) ? HGVS_c : HGVS_p;
    }

    public String getGeneName() {
        return geneName;
    }

    @JsonIgnore
    public boolean isValid() {
        return isValid;
    }

    // Maximum External Allele Frequency
    public String getMaxEAF() {
        return FormatManager.getFloat(maxExternalAF);
    }

    public String getExAC() {
        return FormatManager.getFloat(exac);
    }

    public String getGenomeAsia() {
        return FormatManager.getFloat(genomeAsia);
    }

    public String getGnomADExome() {
        return FormatManager.getFloat(gnomadExome);
    }

    public String getGnomADGenome() {
        return FormatManager.getFloat(gnomadGenome);
    }

    public String getGME() {
        return FormatManager.getFloat(gme);
    }

    public String getIranome() {
        return FormatManager.getFloat(iranome);
    }

    public String getTopMed() {
        return FormatManager.getFloat(topmed);
    }

    private boolean initCarrierData(SearchFilter filter, ModelAndView mv) throws Exception {
        if (filter.isQueryVariant()) { // variant search
            // single variant carriers data process
            CarrierBlockManager.initCarrierMap(carrierMap, this, filter);

            if (!SearchFilter.isMinVarPresentValid(carrierMap.size())) {
                isValid = false;
            }
        } else {
            // block variants carriers data process
            CarrierBlockManager.init(this, filter, mv);

            carrierMap = CarrierBlockManager.getVarCarrierMap(variantId, mv);

            if (carrierMap == null) {
                carrierMap = new HashMap<>();
                isValid = false;
            } else if (!SearchFilter.isMinVarPresentValid(carrierMap.size())) {
                isValid = false;
            }
        }

        return isValid;
    }

    private void initGTCount(SearchFilter filter) {
        SampleManager.getList(filter).forEach((sample) -> {
            if (filter.isExperimentIdValid(sample.getExperimentId())) {
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
                        countGenderAncestry(carrier.getGT(), sample);

                        // hack here - require code refactoring
                        if (!filter.isIsAuthorized()) {
                            carrier.setExperimentID(Data.INTEGER_NA);
                        } else {
                            carrier.setExperimentID(sample.getExperimentId());
                        }

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
        });

        noncarrierMap = null; // free memory
    }

    public void countGeno(byte geno) {
        if (geno != GT.NA.value()) {
            genoCount[geno]++;
        }
    }

    public void countGenderAncestry(byte geno, Sample sample) {
        if (geno == GT.HOM.value() || geno == GT.HET.value()) {
            genderCount[sample.getGender().getIndex()]++;
            ancestryCount[sample.getAncestry().getIndex()]++;
            phenotypeCount[sample.getPhenotype().getIndex()]++;
        }
    }

    private void calculateAF() {
        ac = 2 * genoCount[GT.HOM.value()] + genoCount[GT.HET.value()];

        an = ac + genoCount[GT.HET.value()] + 2 * genoCount[GT.REF.value()];

        af = MathManager.devide(ac, an);

        ns = genoCount[GT.HOM.value()] + genoCount[GT.HET.value()] + genoCount[GT.REF.value()];

        nHom = genoCount[GT.HOM.value()];
    }

    public Collection<Carrier> getCarriers() {
        return carrierMap == null ? null : carrierMap.values();
    }

    // NS = Number of Samples With Data
    public int getNS() {
        return ns;
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

    // NHOM = Number of homozygotes
    public int getNHOM() {
        return nHom;
    }

    public int[] getGenderCount() {
        return genderCount;
    }

    public int[] getAncestryCount() {
        return ancestryCount;
    }

    public int[] getPhenotypeCount() {
        return phenotypeCount;
    }

    public void setAC(int ac) {
        this.ac = ac;
    }

    public void setAN(int an) {
        this.an = an;
    }

    public void setAF(float af) {
        this.af = af;
    }

    public void setNS(int ns) {
        this.ns = ns;
    }

    public void setNHOM(int nHom) {
        this.nHom = nHom;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }
}
