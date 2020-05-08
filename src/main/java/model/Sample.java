package model;

import global.Data;
import global.Index;
import java.util.ArrayList;
import util.FormatManager;

/**
 *
 * @author nick
 */
public class Sample {

    // database info
    private int id;
    // sample info
    private String familyId; // sample or family name
    private String name; // sample name
    private String paternalId;
    private String maternalId;
    private byte sex; // male 1, female 2
    private float quantitativeTrait;
    private String type;
    private String captureKit;
    private int experimentId;
    private String broadPhenotype;

    // covariate
    private ArrayList<Double> covariateList = new ArrayList<>();
    // sample file order
    private int index;

    public Sample(int sampled_id, String family_id, String child_id,
            String paternal_id, String maternal_id, byte _sex, byte _pheno,
            String sample_type, String captureKit, int experimentId, String broadPhenotype) {
        id = sampled_id;
        type = sample_type;
        this.captureKit = captureKit;

        familyId = family_id;
        name = child_id;
        paternalId = paternal_id;
        maternalId = maternal_id;
        sex = _sex;
        quantitativeTrait = Data.FLOAT_NA;
        this.experimentId = experimentId;
        this.broadPhenotype = broadPhenotype;
    }

    public int getId() {
        return id;
    }

    public void setIndex(int value) {
        index = value;
    }

    public int getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public void setType(String value) {
        type = value;
    }

    public String getCaptureKit() {
        return captureKit;
    }

    public void setCaptureKit(String value) {
        captureKit = value;
    }

    public String getFamilyId() {
        return familyId;
    }

    public String getName() {
        return name;
    }

    public String getPaternalId() {
        return paternalId;
    }

    public String getMaternalId() {
        return maternalId;
    }

    public int getSex() {
        return sex;
    }

    public void setQuantitativeTrait(float value) {
        quantitativeTrait = value;
    }

    public float getQuantitativeTrait() {
        return quantitativeTrait;
    }

    public boolean isMale() {
        return sex == 1;
    }

    public boolean isFemale() {
        return !isMale();
    }

    public boolean isFamily() {
        return !name.equals(familyId);
    }

    public void initCovariate(String[] values) {
        int i = 0;

        for (String value : values) {
            if (i == 0 || i == 1) { // ignore FID and IID columns
                i++;
                continue;
            }

            covariateList.add(Double.valueOf(value));
        }
    }

    public ArrayList<Double> getCovariateList() {
        return covariateList;
    }

    public int getExperimentId() {
        return experimentId;
    }

    public String getGender() {
        if (sex == 2) {
            return "F";
        }

        return "M";
    }
    
    public String getBroadPhenotype() {
        return FormatManager.getString(broadPhenotype);
    }
}
