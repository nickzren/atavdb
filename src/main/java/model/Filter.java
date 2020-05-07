package model;

import global.Data;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author nick
 */
public class Filter {

    private float maxAF;
    private String phenotype;
    private boolean isHighQualityVariants;
    
    public Filter() {
    }

    public Filter(HttpServletRequest request) throws Exception {
        String maxAFStr = request.getParameter("maxAF");
        String phenotypeStr = request.getParameter("phenotype");
        String isHighQualityVariantsStr = request.getParameter("isHighQualityVariants");
        
        request.setAttribute("maxAF", maxAFStr);
        request.setAttribute("phenotype", phenotypeStr);
        request.setAttribute("isHighQualityVariants", isHighQualityVariantsStr);
        
        maxAF = getFloat(maxAFStr);
        phenotype = phenotypeStr;
        
        isHighQualityVariants = false;
        if(isHighQualityVariantsStr != null &&
                isHighQualityVariantsStr == "On") {
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
}
