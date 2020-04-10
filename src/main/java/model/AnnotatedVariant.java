package model;

import global.Data;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import static model.Annotation.TRANSCRIPT_LENGTH;
import util.FormatManager;
import util.MathManager;

/**
 *
 * @author nick
 */
public class AnnotatedVariant extends Variant {

    // annotations / most damaging effect annotations
    private int stableId;
    private String effect = "";
    private int effectID;
    private String HGVS_c = "";
    private String HGVS_p = "";
    private float polyphenHumdiv = Data.FLOAT_NA;
    private float polyphenHumvar = Data.FLOAT_NA;
    private boolean hasCCDS = false;
    private String geneName = "";

    private List<String> geneList = new ArrayList<>();
    private StringJoiner allAnnotationSJ = new StringJoiner(",");

    public AnnotatedVariant(String chr, ResultSet rset) throws Exception {
        super(chr, rset);
    }

    public void update(Annotation annotation) {
        if (effect.isEmpty()) { // init most damaging effect annotations
            stableId = annotation.stableId;
            effect = annotation.effect;
            effectID = annotation.effectID;
            HGVS_c = annotation.HGVS_c;
            HGVS_p = annotation.HGVS_p;
            geneName = annotation.geneName;
        }

        StringJoiner annotationSJ = new StringJoiner("|");
        annotationSJ.add(annotation.effect);
        annotationSJ.add(annotation.geneName);
        annotationSJ.add(getStableId(annotation.stableId));
        annotationSJ.add(annotation.HGVS_c);
        annotationSJ.add(annotation.HGVS_p);
        annotationSJ.add(FormatManager.getFloat(annotation.polyphenHumdiv));
        annotationSJ.add(FormatManager.getFloat(annotation.polyphenHumvar));

        allAnnotationSJ.add(annotationSJ.toString());

        polyphenHumdiv = MathManager.max(polyphenHumdiv, annotation.polyphenHumdiv);
        polyphenHumvar = MathManager.max(polyphenHumvar, annotation.polyphenHumvar);

        if (!geneList.contains(annotation.geneName)) {
            geneList.add(annotation.geneName);
        }
    }

    public String getAllAnnotation() {
        return allAnnotationSJ.toString();
    }
    
    public void getAnnotationData(StringJoiner sj) {
        sj.add(getStableId(stableId));
        sj.add(Boolean.toString(hasCCDS));
        sj.add(effect);
        sj.add(HGVS_c);
        sj.add(HGVS_p);
        sj.add(FormatManager.getFloat(polyphenHumdiv));
        sj.add(getPrediction(polyphenHumdiv, effect));
        sj.add(FormatManager.getFloat(polyphenHumvar));
        sj.add(getPrediction(polyphenHumvar, effect));
        sj.add(geneName);
        sj.add(FormatManager.appendDoubleQuote(getAllAnnotation()));
    }
    
    private static String getPrediction(float score, String effect) {        
        if (score == Data.FLOAT_NA) {
            if (effect.startsWith("missense_variant")
                    || effect.equals("splice_region_variant")) {
                return "unknown";
            } else {
                return Data.STRING_NA;
            }
        }

        if (score < 0.4335) { //based on Liz's comment
            return "benign";
        }

        if (score < 0.9035) { //based on Liz's comment
            return "possibly";
        }

        return "probably";
    }

    private String getStableId(int stableId) {
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

    public boolean hasCCDS() {
        return hasCCDS;
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
}
