package model;

import global.Enum.Gender;
import util.FormatManager;

/**
 *
 * @author nick
 */
public class Sample {

    // database info
    private int id;
    // sample info
    private Gender gender;
    private int experimentId;
    private String broadPhenotype;

    // sample file order
    private int index;

    public Sample(int sampled_id, Gender gender, int experimentId, String broadPhenotype) {
        id = sampled_id;
        this.gender = gender;
        this.experimentId = experimentId;
        this.broadPhenotype = broadPhenotype;
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
}
