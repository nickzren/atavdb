package model;

import global.Enum.Ethnicity;
import global.Enum.Gender;
import util.FormatManager;

/**
 *
 * @author nick
 */
public class Sample {

    private int id;
    private Gender gender;
    private int experimentId;
    private String broadPhenotype;
    private Ethnicity ethnicity;

    public Sample(
            int sampled_id,
            Gender gender,
            int experimentId,
            String broadPhenotype,
            Ethnicity ethnicity) {
        id = sampled_id;
        this.gender = gender;
        this.experimentId = experimentId;
        this.broadPhenotype = broadPhenotype;
        this.ethnicity = ethnicity;
    }

    public int getId() {
        return id;
    }

    public boolean isMale() {
        return gender == Gender.M;
    }

    public boolean isFemale() {
        return !isMale();
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
    
    public Ethnicity getEthnicity() {
        return ethnicity;
    }
}
