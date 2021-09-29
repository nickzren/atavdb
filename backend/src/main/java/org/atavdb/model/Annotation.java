package org.atavdb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.atavdb.global.Data;
import java.sql.ResultSet;
import org.atavdb.util.FormatManager;
import org.atavdb.util.MathManager;

/**
 *
 * @author nick
 */
public class Annotation {

    private String impact;
    private String effect;
    private String geneName;
    private int stableId;
    private String HGVS_c;
    private String HGVS_p;
    private float polyphenHumdiv = Data.FLOAT_NA;

    public static final int TRANSCRIPT_LENGTH = 15;

    public Annotation(ResultSet rset) throws Exception {
        stableId = rset.getInt("transcript_stable_id");

        if (stableId < 0) {
            stableId = Data.INTEGER_NA;
        }

        int effectID = rset.getInt("effect_id");
        impact = EffectManager.getImpactById(effectID);
        effect = EffectManager.getEffectById(effectID).replace("_variant", "");
        HGVS_c = FormatManager.getString(rset.getString("HGVS_c"));
        HGVS_p = FormatManager.getString(rset.getString("HGVS_p"));
        geneName = FormatManager.getString(rset.getString("gene"));

        polyphenHumdiv = MathManager.devide(FormatManager.getInt(rset, "polyphen_humdiv"), 1000);
    }

    public String getEffect() {
        return effect;
    }
    
    public String getImpact() {
        return impact;
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

    public String getPolyphen() {
        return getPrediction(polyphenHumdiv, effect);
    }
    
    @JsonIgnore
    public float getPolyphenHumdiv() {
        return polyphenHumdiv;
    }

    private String getPrediction(float score, String effect) {
        if (score == Data.FLOAT_NA) {
            if (effect.startsWith("missense_variant")
                    || effect.equals("splice_region_variant")) {
                return "unknown";
            } else {
                return Data.STRING_NA;
            }
        }

        if (score < 0.4335) {
            return "benign";
        }

        if (score < 0.9035) {
            return "possibly";
        }

        return "probably";
    }
}
