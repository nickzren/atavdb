package org.atavdb.model;

import org.atavdb.global.Enum.Ancestry;
import org.atavdb.global.Enum.Gender;
import org.atavdb.global.Enum.Phenotype;
import org.atavdb.util.FormatManager;

/**
 *
 * @author nick
 */
public class Sample {

    private int id;
    private Gender gender;
    private int experimentId;
    private Ancestry ancestry;
    private byte availableControlUse;
    private Phenotype phenotype;

    public Sample(
            int sampled_id,
            Gender gender,
            int experimentId,
            Ancestry ancestry,
            byte availableControlUse,
            Phenotype phenotype) {
        id = sampled_id;
        this.gender = gender;
        this.experimentId = experimentId;
        this.ancestry = ancestry;
        this.availableControlUse = availableControlUse;
        this.phenotype = phenotype;
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

    public void setExperimentId(int experimentId) {
        this.experimentId = experimentId;
    }

    public Gender getGender() {
        return gender;
    }
    
    public Ancestry getAncestry() {
        return ancestry;
    }
    
    public String getAvailableControlUse() {
        return availableControlUse == 1 ? "Yes" : "No";
    }
    
    public Phenotype getPhenotype() {
        return phenotype;
    }
}
