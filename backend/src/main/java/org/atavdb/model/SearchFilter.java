package org.atavdb.model;

import org.atavdb.global.Enum;
import org.atavdb.global.Data;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import org.atavdb.exception.RegionMaxLimitException;

/**
 *
 * @author nick
 */

public class SearchFilter {

    // user input
    private String query;
    private String queryType;
    private float maf;
    private String phenotype;
    private boolean isHighQualityVariant;
    private boolean isUltraRareVariant;
    private boolean isAvailableControlUseOnly;

    // system default
    private final static int minVarPresent = 1;
    private final static boolean isQcMissingIncluded = true;
    private final static int minDpBin = 10;
    private final static int minGQ = 20;
    private final static float maxSnvSOR = 3;
    private final static float maxIndelSOR = 10;
    private final static float maxSnvFS = 60;
    private final static float maxIndelFS = 200;
    private final static int minMQ = 40;
    private final static int minQD = 5;
    private final static int minQual = 50;
    private final static float minRPRS = -3;
    private final static float minMQRS = -10;
    private final static byte[] validFILTER = {
        Enum.FILTER.PASS.getValue(),
        Enum.FILTER.LIKELY.getValue(),
        Enum.FILTER.INTERMEDIATE.getValue()};

    public final static float MAX_AF_TO_DISPLAY_CARRIER = 0.01f;

    public final static String[] QUERT_TYPE = {"Invalid", "Variant", "Gene", "Region"};

    public final static String[] MAF_LIST = {"", "0.01", "0.005", "0.001"};

    public final static String[] PHENOTYPE_LIST = {"", "amyotrophic lateral sclerosis",
        "autoimmune disease", "bone disease", "brain malformation", "cancer", "cardiovascular disease",
        "congenital disorder", "control", "control mild neuropsychiatric disease", "covid-19",
        "dementia", "dermatological disease", "diseases that affect the ear",
        "endocrine disorder", "epilepsy", "febrile seizures", "fetal ultrasound anomaly",
        "gastrointestinal disease", "healthy family member", "hematological disease",
        "infectious disease", "intellectual disability", "kidney and urological disease",
        "liver disease", "metabolic disease", "neurodegenerative", "nonhuman", "obsessive compulsive disorder",
        "ophthalmic disease", "other", "other neurodevelopmental disease", "other neurological disease",
        "other neuropsychiatric disease", "primary immune deficiency", "pulmonary disease",
        "schizophrenia", "sudden death", "alzheimers disease", "cerebral palsy"};

    public SearchFilter() {}
    
    public SearchFilter(
            String query, 
            String phenotype, 
            String maf, 
            String isHighQualityVariant, 
            String isUltraRareVariant, 
            String isPublicAvailable) throws Exception {
        this.query = query;
        queryType = getQueryType(query);
        this.phenotype = phenotype == null ? "" : phenotype;
        this.maf = getFloat(maf);
        this.isHighQualityVariant = isHighQualityVariant != null && isHighQualityVariant.equals("true");
        this.isUltraRareVariant = isUltraRareVariant != null && isUltraRareVariant.equals("true");
        this.isAvailableControlUseOnly = isPublicAvailable != null && isPublicAvailable.equals("true");
    }

    public void setPhenotype(String phenotype) {
        this.phenotype = phenotype;
    }

    public String getPhenotype() {
        return phenotype;
    }

    public String getPhenotypeSQL() {
        return phenotype.isEmpty() ? "" : " AND broad_phenotype='" + phenotype + "'";
    }

    private void appendPhenotype4Identifer(StringJoiner sj) {
        if (!phenotype.isEmpty()) {
            sj.add(phenotype);
        }
    }

    public void setIsAvailableControlUseOnly(boolean isAvailableControlUseOnly) {
        this.isAvailableControlUseOnly = isAvailableControlUseOnly;
    }

    public String getMAFStr() {
        return maf == Data.NO_FILTER ? null : String.valueOf(maf);
    }

    public float getMAF() {
        return maf;
    }

    private void appendHighQualityVariant4Identifer(StringJoiner sj) {
        if (isHighQualityVariant) {
            sj.add("high");
        }
    }

    public String getQueryIdentifier() {
        StringJoiner sj = new StringJoiner("-");
        sj.add(query);
        appendPhenotype4Identifer(sj);
        appendHighQualityVariant4Identifer(sj);

        return sj.toString();
    }

    private static float getFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            return Data.NO_FILTER;
        }
    }

    public static boolean isMinVarPresentValid(int value) {
        return value >= minVarPresent;
    }

    private String getQueryType(String query) throws Exception {
        if (query != null && !query.isEmpty()
                // only allow Alphanumeric, ":" and "-" 
                && Pattern.matches("^[a-zA-Z0-9\\:\\-]+$", query)) {
            if (query.split("-").length == 4) {
                String[] tmp = query.split("-");
                if (RegionManager.isChrValid(tmp[0]) // check valid chr 
                        && tmp[1].matches("^[1-9]\\d*$") // check valid positive integer
                        && tmp[2].matches("^[ATCG]+$") // valid if only contains ATCG 
                        && tmp[3].matches("^[ATCG]+$") // valid if only contains ATCG 
                        ) {
                    return QUERT_TYPE[1]; // Variant
                }
            } else if (query.contains(":")) {
                String[] tmp = query.split(":");
                if (tmp.length == 2
                        && RegionManager.isChrValid(tmp[0])) { // check valid chr 
                    tmp = tmp[1].split("-");
                    if (tmp.length == 2
                            && tmp[0].matches("^[1-9]\\d*$") // check valid positive integer
                            && tmp[1].matches("^[1-9]\\d*$") // check valid positive integer
                            ) {
                        int end = Integer.valueOf(tmp[1]);
                        int start = Integer.valueOf(tmp[0]);

                        if (end >= start
                                && (end - start) <= RegionManager.MAX_SEARCH_LIMIT) {
                            return QUERT_TYPE[3]; // Region
                        } else {
                            throw new RegionMaxLimitException();
                        }
                    }
                }
            } else if (GeneManager.isValid(query)) {
                return QUERT_TYPE[2]; // Gene
            }
        }

        return QUERT_TYPE[0]; // Invalid
    }

    public String getQuery() {
        return query.isEmpty() ? null : query;
    }

    public String getQueryType() {
        return queryType;
    }

    public boolean isQueryValid() {
        return !queryType.equals(QUERT_TYPE[0]);
    }

    public boolean isQueryVariant() {
        return queryType.equals(QUERT_TYPE[1]);
    }

    public boolean isQueryGene() {
        return queryType.equals(QUERT_TYPE[2]);
    }

    public boolean isQueryRegion() {
        return queryType.equals(QUERT_TYPE[3]);
    }

    public boolean isMAFValid(float value) {
        if (maf == Data.NO_FILTER) {
            return true;
        }

        return value <= maf || value >= (1 - maf);
    }

    public boolean isFilterValid(byte value) {
        if (!isHighQualityVariant) {
            return true;
        }

        for (byte tmp : validFILTER) {
            if (value == tmp) {
                return true;
            }
        }

        return false;
    }

    public boolean isMinGqValid(byte value) {
        if (!isHighQualityVariant) {
            return true;
        }

        if (value == Data.BYTE_NA) {
            if (isQcMissingIncluded) {
                return true;
            }
        } else if (value >= minGQ) {
            return true;
        }

        return false;
    }

    public boolean isMaxSorValid(float value, boolean isSnv) {
        if (isSnv) {
            return isMaxSORValid(value, maxSnvSOR);
        } else {
            return isMaxSORValid(value, maxIndelSOR);
        }
    }

    private boolean isMaxSORValid(float value, float sor) {
        if (!isHighQualityVariant) {
            return true;
        }

        if (value == Data.FLOAT_NA) {
            if (isQcMissingIncluded) {
                return true;
            }
        } else if (value <= sor) {
            return true;
        }

        return false;
    }

    public boolean isMaxFsValid(float value, boolean isSnv) {
        if (isSnv) {
            return isMaxFsValid(value, maxSnvFS);
        } else {
            return isMaxFsValid(value, maxIndelFS);
        }
    }

    private boolean isMaxFsValid(float value, float fs) {
        if (!isHighQualityVariant) {
            return true;
        }

        if (value == Data.FLOAT_NA) {
            if (isQcMissingIncluded) {
                return true;
            }
        } else if (value <= fs) {
            return true;
        }

        return false;
    }

    public boolean isMinMqValid(byte value) {
        if (!isHighQualityVariant) {
            return true;
        }

        if (value == Data.BYTE_NA) {
            if (isQcMissingIncluded) {
                return true;
            }
        } else if (value >= minMQ) {
            return true;
        }

        return false;
    }

    public boolean isMinQdValid(byte value) {
        if (!isHighQualityVariant) {
            return true;
        }

        if (value == Data.BYTE_NA) {
            if (isQcMissingIncluded) {
                return true;
            }
        } else if (value >= minQD) {
            return true;
        }

        return false;
    }

    public boolean isMinQualValid(int value) {
        if (!isHighQualityVariant) {
            return true;
        }

        if (value == Data.INTEGER_NA) {
            if (isQcMissingIncluded) {
                return true;
            }
        } else if (value >= minQual) {
            return true;
        }

        return false;
    }

    public boolean isMinRprsValid(float value) {
        if (!isHighQualityVariant) {
            return true;
        }

        if (value == Data.FLOAT_NA) {
            if (isQcMissingIncluded) {
                return true;
            }
        } else if (value >= minRPRS) {
            return true;
        }

        return false;
    }

    public boolean isMinMqrsValid(float value) {
        if (!isHighQualityVariant) {
            return true;
        }

        if (value == Data.FLOAT_NA) {
            if (isQcMissingIncluded) {
                return true;
            }
        } else if (value >= minMQRS) {
            return true;
        }

        return false;
    }

    public boolean isMinDpBinValid(short value) {
        if (!isHighQualityVariant) {
            return true;
        }

        return value >= minDpBin;
    }

    public boolean isHighQualityVariant() {
        return isHighQualityVariant;
    }

    public boolean isUltraRareVariant() {
        return isUltraRareVariant;
    }

    public boolean isExternalMAFValid(float af) {
        if (isUltraRareVariant) {
            return af == Data.FLOAT_NA || af == 0 || af == 1;
        }

        // if not looking for ultra variant always return true
        return true;
    }

    public boolean isAvailableControlUseOnly() {
        return isAvailableControlUseOnly;
    }

    public String getAvailableControlUseSQL() {
        return isAvailableControlUseOnly ? " AND available_control_use=1" : "";
    }

    public String getSampleSQL() {
        return " sample_finished=1 AND sample_failure=0 AND sample_type!='custom_capture'";
    }

    public String getIsHighQualityVariantStr() {
        return isHighQualityVariant ? "on" : null;
    }

    public String getIsUltraRareVariantStr() {
        return isUltraRareVariant ? "on" : null;
    }

    public String getIsPublicAvailableStr() {
        return isAvailableControlUseOnly ? "on" : null;
    }

    public String getFlankingRegion() {
        if (isQueryVariant()) {
            String[] tmp = query.split("-");
            String chr = tmp[0];
            int pos = Integer.valueOf(tmp[1]);
            int start = pos - 10;
            int end = pos + 10;

            return chr + ":" + start + "-" + end;
        }

        return null;
    }
}
