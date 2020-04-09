package object;

import java.sql.ResultSet;

/**
 *
 * @author Nick
 */
public class Variant {

    private String chr;
    private int pos;
    private String ref;
    private String alt;
    private String effect;
    private int homCount;
    private int hetCount;
    private int refCount;
    private float af;

    public static final String title
            = "Chr,"
            + "Pos,"
            + "Ref,"
            + "Alt,"
            + "Effect,"
            + "Allele Count,"
            + "Sample Count,"
            + "Hom Count,"
            + "Heteroz Count,"
            + "Ref Count,"
            + "AF";

    public Variant(ResultSet rset) throws Exception {
        chr = rset.getString("chr");
        pos = rset.getInt("pos");
        ref = rset.getString("ref");
        alt = rset.getString("alt");
        effect = rset.getString("effect");
        homCount = rset.getInt("hom_count");
        hetCount = rset.getInt("het_count");
        refCount = rset.getInt("ref_count");
        af = rset.getFloat("af");
    }

    public String getIdStr() {
        return chr + "-" + pos + "-" + ref + "-" + alt;
    }

    public String getChr() {
        return chr;
    }

    public int getPos() {
        return pos;
    }

    public String getRef() {
        return ref;
    }

    public String getAlt() {
        return alt;
    }
    
    public String getEffect() {
        return effect;
    }

    public int getAlleleCount() {
        return 2 * homCount + hetCount;
    }

    public int getSampleCount() {
        return refCount + hetCount + homCount;
    }
    
    public int getHomCount() {
        return homCount;
    }
    
    public int getHetCount() {
        return hetCount;
    }
    
    public int getRefCount() {
        return refCount;
    }
    
    public float getAF() {
        return af;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(chr).append(",");
        sb.append(pos).append(",");
        sb.append(ref).append(",");
        sb.append(alt).append(",");
        sb.append(effect).append(",");
        sb.append(getAlleleCount()).append(",");
        sb.append(getSampleCount()).append(",");
        sb.append(homCount).append(",");
        sb.append(hetCount).append(",");
        sb.append(refCount).append(",");
        sb.append(af).append("\n");

        return sb.toString();
    }
}
