package model;

import global.Enum.Gender;
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
    private Gender gender;
    private String type;
    private String captureKit;
    private int experimentId;
    private String broadPhenotype;

    // covariate
    private ArrayList<Double> covariateList = new ArrayList<>();
    // sample file order
    private int index;

    public Sample(int sampled_id, String family_id, String child_id,
            String paternal_id, String maternal_id, Gender gender,
            String sample_type, String captureKit, int experimentId, String broadPhenotype) {
        id = sampled_id;
        type = sample_type;
        this.captureKit = captureKit;

        familyId = family_id;
        name = child_id;
        paternalId = paternal_id;
        maternalId = maternal_id;
        this.gender = gender;
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

    public boolean isMale() {
        return gender == Gender.M;
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

    public Gender getGender() {
        return gender;
    }
    
    public String getBroadPhenotype() {
        return FormatManager.getString(broadPhenotype);
    }
}
