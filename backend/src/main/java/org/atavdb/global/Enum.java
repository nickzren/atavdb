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
            if(filter == NA.value) {
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
            if(gt == NA.value) {
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
}
