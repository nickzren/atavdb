package model;

import global.Data;
import util.FormatManager;
import java.sql.ResultSet;
import java.util.StringJoiner;

/**
 *
 * @author nick
 */
public class Variant extends Region {

    public int variantId;
    public String variantIdStr;
    public String allele;
    public String refAllele;
    public int rsNumber;
    //Indel attributes
    private boolean isIndel;
    private boolean isMNV;

    public Variant(String chr, ResultSet rset) throws Exception {
        variantId = rset.getInt("variant_id");

        int pos = rset.getInt("POS");
        allele = rset.getString("ALT");
        refAllele = rset.getString("REF");
        rsNumber = FormatManager.getInt(rset, "rs_number");

        isIndel = refAllele.length() != allele.length();

        isMNV = refAllele.length() > 1 && allele.length() > 1
                && allele.length() == refAllele.length();

        initRegion(chr, pos, pos);

        variantIdStr = chrStr + "-" + pos + "-" + refAllele + "-" + allele;
    }

    public int getVariantId() {
        return variantId;
    }

    public String getVariantIdStr() {
        return variantIdStr;
    }

    public String getType() {
        if (isIndel) {
            return "indel";
        } else {
            return "snv";
        }
    }

    public String getAllele() {
        return allele;
    }

    public String getRefAllele() {
        return refAllele;
    }

    public int getRsNumber() {
        return rsNumber;
    }

    public String getRsNumberStr() {
        if (rsNumber == Data.INTEGER_NA) {
            return Data.STRING_NA;
        }

        return "rs" + rsNumber;
    }

    public boolean isSnv() {
        return !isIndel;
    }

    public boolean isIndel() {
        return isIndel;
    }
    
    public boolean isMNV() {
        return isMNV;
    }

    public boolean isDel() {
        return refAllele.length() > allele.length();
    }

    public String getSiteId() {
        return getChrStr() + "-" + getStartPosition();
    }

    public void getVariantData(StringJoiner sj) {
        sj.add(variantIdStr);
        sj.add(getType());
        sj.add(refAllele);
        sj.add(allele);
        sj.add(getRsNumberStr());
    }
}
