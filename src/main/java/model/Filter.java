package model;

import global.Data;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nick
 */
public class Filter {

    private String query;
    private String queryType;
    private float maxAF;
    private String phenotype;
    private boolean isHighQualityVariants;
    private final static int minVarPresent = 1;

    public Filter() {
    }

    public Filter(HttpServletRequest request) throws Exception {
        query = request.getParameter("query");
        queryType = getQueryType(query);
        String maxAFStr = request.getParameter("maxAF");
        String phenotypeStr = request.getParameter("phenotype");
        String isHighQualityVariantsStr = request.getParameter("isHighQualityVariants");

        request.setAttribute("query", query);
        request.setAttribute("queryType", queryType);
        request.setAttribute("maxAF", maxAFStr);
        request.setAttribute("phenotype", phenotypeStr);
        request.setAttribute("isHighQualityVariants", isHighQualityVariantsStr);

        maxAF = getFloat(maxAFStr);
        phenotype = phenotypeStr;

        isHighQualityVariants = false;
        if (isHighQualityVariantsStr != null
                && isHighQualityVariantsStr == "On") {
            isHighQualityVariants = true;
        }
    }

    public String getPhenotypeSQL() {
        if (phenotype != null && !phenotype.equals("Not apply")) {
            return " AND broad_phenotype='" + phenotype + "'";
        }

        return "";
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
        if (isQueryValid()) {
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
        return query;
    }

    public String getQueryType() {
        return queryType;
    }

    public boolean isQueryValid() {
        return query != null && !query.isEmpty();
    }
    
    public boolean isMaxAFValid(double value) {
        if (maxAF == Data.NO_FILTER) {
            return true;
        }

        return value <= maxAF;
    }
}
