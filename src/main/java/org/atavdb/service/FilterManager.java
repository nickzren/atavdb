package org.atavdb.service;

import org.atavdb.global.Enum;
import org.atavdb.global.Data;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import javax.servlet.http.HttpSession;

/**
 *
 * @author nick
 */
public class FilterManager {

    private String query;
    private String queryType;
    private float maxAF;
    private String phenotype;
    private boolean isHighQualityVariant;
    private boolean isUltraRareVariant;
    private boolean isAvailableControlUseOnly;
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
    private static final byte[] validFILTER = {
        Enum.FILTER.PASS.getValue(),
        Enum.FILTER.LIKELY.getValue(),
        Enum.FILTER.INTERMEDIATE.getValue()};
    private String error;

    public final static float MAX_AF_TO_DISPLAY_CARRIER = 0.01f;

    public FilterManager() {
    }

    public FilterManager(HttpSession session) {
        this.query = (String) session.getAttribute("query");
        queryType = getQueryType(query);
        session.setAttribute("queryType", queryType);

        String phenotype = (String) session.getAttribute("phenotype");
        this.maxAF = getFloat((String) session.getAttribute("maxAF"));
        String isHighQualityVariant = (String) session.getAttribute("isHighQualityVariant");
        String isUltraRareVariant = (String) session.getAttribute("isUltraRareVariant");
        String isPublicAvailable = (String) session.getAttribute("isPublicAvailable");

        if (session.getAttribute("username") == null
                && queryType.equals(Data.QUERT_TYPE[2])) {
            error = error != null ? error : "Permission denied for anonymous user.";
        }

        // default to search high quality variants only for gene or region
        if (queryType.equals(Data.QUERT_TYPE[2]) || queryType.equals(Data.QUERT_TYPE[3])) {
            isHighQualityVariant = "on";
            session.setAttribute("isHighQualityVariant", isHighQualityVariant);
        }

        // for unauthorized user & valid query, public avaiable data only
        if (query != null && !query.isEmpty()
                && session.getAttribute("sequence_authorized") == null) {
            isAvailableControlUseOnly = true;
            isPublicAvailable = "on";
            session.setAttribute("isPublicAvailable", isPublicAvailable);
        } else {
            // for ahthorized user, use Public Only checkbox as filter
            isAvailableControlUseOnly = isPublicAvailable != null;
        }

        this.phenotype = phenotype == null ? "" : phenotype;
        this.isHighQualityVariant = isHighQualityVariant != null && isHighQualityVariant.equalsIgnoreCase("on");
        this.isUltraRareVariant = isUltraRareVariant != null && isUltraRareVariant.equalsIgnoreCase("on");
    }

    public String getPhenotype() {
        return phenotype;
    }

    public String getPhenotypeStr() {
        return phenotype.isEmpty() ? null : phenotype;
    }

    public String getPhenotypeSQL() {
        return phenotype.isEmpty() ? "" : " AND broad_phenotype='" + phenotype + "'";
    }

    private void appendPhenotype4Identifer(StringJoiner sj) {
        if (!phenotype.isEmpty()) {
            sj.add(phenotype);
        }
    }

    public String getMaxAFStr() {
        return maxAF == Data.NO_FILTER ? null : String.valueOf(maxAF);
    }

    public float getMaxAF() {
        return maxAF;
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

    private String getQueryType(String query) {
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
                    return Data.QUERT_TYPE[1]; // Variant
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
                            return Data.QUERT_TYPE[3]; // Region
                        } else {
                            error = "Invalid region or exceeds maximum limit " + RegionManager.MAX_SEARCH_LIMIT + " base pair.";
                            return Data.QUERT_TYPE[0]; // Invalid
                        }
                    }
                }
            } else if (GeneManager.isValid(query)) {
                return Data.QUERT_TYPE[2]; // Gene
            }
        }

        return Data.QUERT_TYPE[0]; // Invalid
    }

    public String getQuery() {
        return query.isEmpty() ? null : query;
    }

    public String getQueryType() {
        return queryType;
    }

    public boolean isQueryValid() {
        return !queryType.equals(Data.QUERT_TYPE[0]) && error == null;
    }

    public String getError() {
        return error;
    }

    public boolean isMaxAFValid(double value) {
        if (maxAF == Data.NO_FILTER) {
            return true;
        }

        return value <= maxAF;
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

    public boolean isExternalAFValid(float af) {
        if (isUltraRareVariant) {
            return af == Data.FLOAT_NA || af == 0;
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
}
