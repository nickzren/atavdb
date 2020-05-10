package global;

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
        AMBIGUOUS("AMBIGUOUS",(byte) 2);

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

    public static void main(String[] args) {
        
        
        System.out.println(Enum.Gender.F.getIndex());
    }
}
