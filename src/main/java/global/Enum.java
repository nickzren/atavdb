package global;

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
        M("MALE", (byte) 0),
        F("FEMALE", (byte) 1),
        AMBIGUOUS("AMBIGUOUS", (byte) 2);

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

    public static void main(String[] args) {

    }
}
