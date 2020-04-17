package model;

import global.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.FormatManager;
import util.MathManager;

/**
 *
 * @author nick
 */
public class Annotation {

    private String chr;
    private int pos;
    private String ref;
    private String alt;
    public String effect;
    public int effectID;
    public String geneName;
    public int stableId;
    public String HGVS_c;
    public String HGVS_p;
    public float polyphenHumdiv = Data.FLOAT_NA;
    public float polyphenHumvar = Data.FLOAT_NA;
    private boolean isValid;
    
    public static final int TRANSCRIPT_LENGTH = 15;

    public Annotation(String chr, ResultSet rset) throws SQLException {
        this.chr = chr;
        pos = rset.getInt("POS");
        ref = rset.getString("REF");
        alt = rset.getString("ALT");
        stableId = rset.getInt("transcript_stable_id");

        if (stableId < 0) {
            stableId = Data.INTEGER_NA;
        }

        effectID = rset.getInt("effect_id");
        effect = EffectManager.getEffectById(effectID);
        HGVS_c = FormatManager.getString(rset.getString("HGVS_c"));
        HGVS_p = FormatManager.getString(rset.getString("HGVS_p"));
        geneName = FormatManager.getString(rset.getString("gene"));

        polyphenHumdiv = MathManager.devide(FormatManager.getInt(rset, "polyphen_humdiv"), 1000);
        polyphenHumvar = MathManager.devide(FormatManager.getInt(rset, "polyphen_humvar"), 1000);
    }

    public void setValid(boolean value) {
        isValid = value;
    }
    
    public boolean isValid() {
        return isValid;
    }

    public String getStableId() {
        if (stableId == Data.INTEGER_NA) {
            return Data.STRING_NA;
        }

        StringBuilder idSB = new StringBuilder(String.valueOf(stableId));

        int zeroStringLength = TRANSCRIPT_LENGTH - idSB.length() - 4;

        for (int i = 0; i < zeroStringLength; i++) {
            idSB.insert(0, 0);
        }

        idSB.insert(0, "ENST");

        return idSB.toString();
    }
}