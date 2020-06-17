package model;

import global.Data;
import util.FormatManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import util.MathManager;

/**
 *
 * @author nick
 */
public class Variant extends Region {

    public int variantId;
    public String variantIdStr;
    public String alt;
    public String ref;
    public int rsNumber;
    //Indel attributes
    private boolean isIndel;

    // annotation
    private String effect = "";
    private String HGVS_c = "";
    private String HGVS_p = "";
    private float polyphenHumdiv = Data.FLOAT_NA;
    private float polyphenHumvar = Data.FLOAT_NA;
    private String geneName = "";
    private List<String> geneList = new ArrayList<>();
    private List<Annotation> annotationList = new ArrayList<>();

    // external af
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
    private int[] ethnicityCount = new int[7]; // African, Caucasian, EastAsian, Hispanic, MiddleEastern, SouthAsian, NA
    private int ac; // allele count
    private int an; // allele number
    public float af; // allele frequency

    public boolean isValid = true;

    public Variant(String chr, ResultSet rset, FilterManager filter, HttpServletRequest request) throws Exception {
        variantId = rset.getInt("variant_id");

        int pos = rset.getInt("POS");
        ref = rset.getString("REF");
        alt = rset.getString("ALT");
        rsNumber = FormatManager.getInt(rset, "rs_number");

        isIndel = ref.length() != alt.length();

        initRegion(chr, pos, pos);

        variantIdStr = chrStr + "-" + pos + "-" + ref + "-" + alt;

        initExternalAF(filter);

        initCarrier(filter, request);
    }

    private void initExternalAF(FilterManager filter) {
        exac = ExternalDataManager.getExAC(chrStr, startPosition, ref, alt);
        genomeAsia = ExternalDataManager.getGenomeAsia(chrStr, startPosition, ref, alt);
        gnomadExome = ExternalDataManager.getGenoADExome(chrStr, startPosition, ref, alt);
        gnomadGenome = ExternalDataManager.getGenoADGenome(chrStr, startPosition, ref, alt);
        gme = ExternalDataManager.getGME(chrStr, startPosition, ref, alt);
        iranome = ExternalDataManager.getIRANOME(chrStr, startPosition, ref, alt);
        topmed = ExternalDataManager.getTOPMED(chrStr, startPosition, ref, alt);
    }

    public boolean isExternalAFValid(FilterManager filter) {
        return filter.isExternalAFValid(exac)
                && filter.isExternalAFValid(genomeAsia)
                && filter.isExternalAFValid(gnomadExome)
                && filter.isExternalAFValid(gnomadGenome)
                && filter.isExternalAFValid(gme)
                && filter.isExternalAFValid(iranome)
                && filter.isExternalAFValid(topmed);
    }

    // init carrier and non-carrier data, calculate af and count genotype
    private void initCarrier(FilterManager filter, HttpServletRequest request) throws Exception {
        if (isValid
                && initCarrierData(filter, request)) {
            DPBinBlockManager.initCarrierAndNonCarrierByDPBin(this, carrierMap, noncarrierMap, filter, request);

            initGTCount(filter);

            calculateAF();

            isValid = FilterManager.isMinVarPresentValid(carrierMap.size());

            // if not valid
            // if gene / region search then free carriers
            // if variant search and when af > MAX AF then free carriers
            if (!isValid
                    || !filter.getQueryType().equals((Data.QUERT_TYPE[1]))
                    || af > FilterManager.MAX_AF_TO_DISPLAY_CARRIER) {
                carrierMap = null;
            }
        }
    }

    public boolean isMaxAFValid(FilterManager filter) {
        return filter.isMaxAFValid(af);
    }

    public int getVariantId() {
        return variantId;
    }

    // chr-pos-ref-alt
    public String getVariantIdStr() {
        return variantIdStr;
    }

    // chr:g.posref>alt
    public String getVariantIdStr2() {
        return "chr" + chrStr + ":g." + getStartPosition() + ref + ">" + alt;
    }

    public String getAlt() {
        return alt;
    }

    public String getRef() {
        return ref;
    }

    public int getRsNumber() {
        return rsNumber;
    }

    public boolean isSnv() {
        return !isIndel;
    }

    public void update(Annotation annotation) {
        if (isValid) {
            if (effect.isEmpty()) { // init most damaging effect annotations
                effect = annotation.effect;
                HGVS_c = annotation.HGVS_c;
                HGVS_p = annotation.HGVS_p;
                geneName = annotation.geneName;
            }

            annotationList.add(annotation);

            polyphenHumdiv = MathManager.max(polyphenHumdiv, annotation.polyphenHumdiv);
            polyphenHumvar = MathManager.max(polyphenHumvar, annotation.polyphenHumvar);

            if (!geneList.contains(annotation.geneName)) {
                geneList.add(annotation.geneName);
            }
        }
    }

    public List<Annotation> getAllAnnotation() {
        return annotationList;
    }

    public String getEffect() {
        return effect;
    }

    public String getHGVS_c() {
        return HGVS_c;
    }

    public String getHGVS_p() {
        return HGVS_p;
    }

    public String getGeneName() {
        return geneName;
    }

    public List<String> getGeneList() {
        return geneList;
    }

    public boolean isValid() {
        return isValid;
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

    public String getIranme() {
        return FormatManager.getFloat(iranome);
    }

    public String getTopMed() {
        return FormatManager.getFloat(topmed);
    }

    private boolean initCarrierData(FilterManager filter, HttpServletRequest request) {
        if (filter.getQueryType().equals(Data.QUERT_TYPE[1])) { // variant search
            // single variant carriers data process
            CarrierBlockManager.initCarrierMap(carrierMap, this, filter);

            if (!FilterManager.isMinVarPresentValid(carrierMap.size())) {
                isValid = false;
            }
        } else {
            // block variants carriers data process
            CarrierBlockManager.init(this, filter, request);

            carrierMap = CarrierBlockManager.getVarCarrierMap(variantId, request);

            if (carrierMap == null) {
                carrierMap = new HashMap<>();
                isValid = false;
            } else if (!FilterManager.isMinVarPresentValid(carrierMap.size())) {
                isValid = false;
            }
        }

        return isValid;
    }

    private void initGTCount(FilterManager filter) {
        SampleManager.getList(filter).forEach((sample) -> {
            Carrier carrier = carrierMap.get(sample.getId());
            NonCarrier noncarrier = noncarrierMap.get(sample.getId());

            if (carrier != null) {
                if (!filter.isMinDpBinValid(carrier.getDPBin())) {
                    carrier.setGT(global.Enum.GT.NA.value());
                    carrier.setDPBin(Data.SHORT_NA);
                }

                if (carrier.getGT() == global.Enum.GT.NA.value()) {
                    // have to remove it for init Non-carrier map
                    carrierMap.remove(sample.getId());
                } else {
                    countGeno(carrier.getGT());
                    countGenderEthnicity(carrier.getGT(), sample);
                    carrier.setSample(sample);
                }
            } else if (noncarrier != null) {
                if (!filter.isMinDpBinValid(noncarrier.getDPBin())) {
                    noncarrier.setGT(global.Enum.GT.NA.value());
                    noncarrier.setDPBin(Data.SHORT_NA);
                }

                countGeno(noncarrier.getGT());
            }
        });

        noncarrierMap = null; // free memory
    }

    public void countGeno(byte geno) {
        if (geno != global.Enum.GT.NA.value()) {
            genoCount[geno]++;
        }
    }

    public void countGenderEthnicity(byte geno, Sample sample) {
        if (geno == global.Enum.GT.HOM.value() || geno == global.Enum.GT.HET.value()) {
            genderCount[sample.getGender().getIndex()]++;
            ethnicityCount[sample.getEthnicity().getIndex()]++;
        }
    }

    private void calculateAF() {
        ac = 2 * genoCount[global.Enum.GT.HOM.value()] + genoCount[global.Enum.GT.HET.value()];

        an = ac + genoCount[global.Enum.GT.HET.value()] + 2 * genoCount[global.Enum.GT.REF.value()];

        af = MathManager.devide(ac, an);
    }

    public Collection<Carrier> getCarriers() {
        return carrierMap == null ? null : carrierMap.values();
    }

    // NS = Number of Samples With Data
    public int getNS() {
        return genoCount[global.Enum.GT.HOM.value()] + genoCount[global.Enum.GT.HET.value()] + genoCount[global.Enum.GT.REF.value()];
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
        return genoCount[global.Enum.GT.HOM.value()];
    }

    public int[] getGenderCount() {
        return genderCount;
    }

    public int[] getEthnicityCount() {
        return ethnicityCount;
    }
}
