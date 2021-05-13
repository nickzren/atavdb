package org.atavdb.global;

import java.util.stream.Stream;

/**
 *
 * @author nick
 */
public class Enum {

    public enum FILTER {
        NA((byte) Data.BYTE_NA),
        PASS((byte) 1),
        LIKELY((byte) 2),
        INTERMEDIATE((byte) 3),
        FAIL((byte) 4);

        private byte value;

        private static final String[] array
                = Stream.of(FILTER.values()).map(FILTER::name).toArray(String[]::new);

        private FILTER(byte value) {
            this.value = value;
        }

        public static String valueOf(byte filter) {
            if (filter == NA.value) {
                return NA.name();
            } else {
                return array[filter];
            }
        }

        public byte getValue() {
            return value;
        }
    };

    public enum Gender {
        M("Male", (byte) 0),
        F("Female", (byte) 1),
        Ambiguous("Ambiguous", (byte) 2),
        NA("NA", (byte) 3);

        private String name;
        private byte index;

        private Gender(String name, byte index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public byte getIndex() {
            return index;
        }
    }

    public enum GT {
        REF("REF", (byte) 0),
        HET("HET", (byte) 1),
        HOM("HOM", (byte) 2),
        NA(Data.STRING_NA, (byte) Data.BYTE_NA);

        private String name;
        private byte value;

        private static final String[] array
                = Stream.of(GT.values()).map(GT::name).toArray(String[]::new);

        private GT(String name, byte value) {
            this.name = name;
            this.value = value;
        }

        public static String valueOf(byte gt) {
            if (gt == NA.value) {
                return NA.name;
            } else {
                return array[gt];
            }
        }

        public byte value() {
            return value;
        }
    }

    public enum Ancestry {
        African("African", (byte) 0),
        Caucasian("Caucasian", (byte) 1),
        EastAsian("EastAsian", (byte) 2),
        Hispanic("Hispanic", (byte) 3),
        MiddleEastern("MiddleEastern", (byte) 4),
        SouthAsian("SouthAsian", (byte) 5),
        NA("NA", (byte) 6);

        private String name;
        private byte index;

        private Ancestry(String name, byte index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public byte getIndex() {
            return index;
        }
    }

    public enum Phenotype {
        AMYOTROPHIC_LATERAL_SCLEROSIS("amyotrophic lateral sclerosis", (byte) 0),
        AUTOIMMUNE_DISEASE("autoimmune disease", (byte) 1),
        BONE_DISEASE("bone disease", (byte) 2),
        BRAIN_MALFORMATION("brain malformation", (byte) 3),
        CANCER("cancer", (byte) 4),
        CARDIOVASCULAR_DISEASE("cardiovascular disease", (byte) 5),
        CONGENITAL_DISORDER("congenital disorder", (byte) 6),
        CONTROL("control", (byte) 7),
        CONTROL_MILD_NEUROPSYCHIATRIC_DISEASE("control mild neuropsychiatric disease", (byte) 8),
        COVID19("covid-19", (byte) 9),
        DEMENTIA("dementia", (byte) 10),
        DERMATOLOGICAL_DISEASE("dermatological disease", (byte) 11),
        DISEASES_THAT_AFFECT_THE_EAR("diseases that affect the ear", (byte) 12),
        ENDOCRINE_DISORDER("endocrine disorder", (byte) 13),
        EPILEPSY("epilepsy", (byte) 14),
        FEBRILE_SEIZURES("febrile seizures", (byte) 15),
        FETAL_ULTRASOUND_ANOMALY("fetal ultrasound anomaly", (byte) 16),
        GASTROINTESTINAL_DISEASE("gastrointestinal disease", (byte) 17),
        HEALTHY_FAMILY_MEMBER("healthy family member", (byte) 18),
        HEMATOLOGICAL_DISEASE("hematological disease", (byte) 19),
        INFECTIOUS_DISEASE("infectious disease", (byte) 20),
        INTELLECTUAL_DISABILITY("intellectual disability", (byte) 21),
        KIDNEY_AND_UROLOGICAL_DISEASE("kidney and urological disease", (byte) 22),
        LIVER_DISEASE("liver disease", (byte) 23),
        METABOLIC_DISEASE("metabolic disease", (byte) 24),
        NEURODEGENERATIVE("neurodegenerative", (byte) 25),
        NONHUMAN("nonhuman", (byte) 26),
        OBSESSIVE_COMPULSIVE_DISORDER("obsessive compulsive disorder", (byte) 27),
        OPHTHALMIC_DISEASE("ophthalmic disease", (byte) 28),
        OTHER("other", (byte) 29),
        OTHER_NEURODEVELOPMENTAL_DISEASE("other neurodevelopmental disease", (byte) 30),
        OTHER_NEUROLOGICAL_DISEASE("other neurological disease", (byte) 31),
        OTHER_NEUROPSYCHIATRIC_DISEASE("other neuropsychiatric disease", (byte) 32),
        PRIMARY_IMMUNE_DEFICIENCY("primary immune deficiency", (byte) 33),
        PULMONARY_DISEASE("pulmonary disease", (byte) 34),
        SCHIZOPHRENIA("schizophrenia", (byte) 35),
        SUDDEN_DEATH("sudden death", (byte) 36),
        ALZHEIMERS_DISEASE("alzheimers disease", (byte) 37),
        CEREBRAL_PALSY("cerebral palsy", (byte) 38),
        NA("NA", (byte) 39);

        private String name;
        private byte index;

        private Phenotype(String name, byte index) {
            this.name = name;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        public byte getIndex() {
            return index;
        }
    }
}
