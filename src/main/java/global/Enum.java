package global;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author nick
 */
public class Enum {

    public enum FILTER {
        PASS(1), LIKELY(2), INTERMEDIATE(3), FAIL(4);
        private int value;

        private FILTER(int value) {
            this.value = value;
        }

        public int getValue() {
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
        HOM("HOM", (byte) 2),
        HET("HET", (byte) 1),
        REF("REF", (byte) 0),
        NA(Data.STRING_NA, (byte) Data.BYTE_NA);

        private String name;
        private byte value;

        private static Map map = new HashMap<>();

        static {
            for (GT gt : GT.values()) {
                map.put(gt.value, gt);
            }
        }

        private GT(String name, byte value) {
            this.name = name;
            this.value = value;
        }

        public static GT valueOf(byte gt) {
            return (GT) map.get(gt);
        }

        public String getName() {
            return name;
        }

        public byte value() {
            return value;
        }
    }

    public static void main(String[] args) {

        System.out.println(Enum.Gender.F.getIndex());
    }
}
