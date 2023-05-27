package utils;

public class MathUtils {

    public static int randomSign() {
        return Math.random() > 0.5 ? 1 : -1;
    }

    //bitwise to convert short to unsigned representation
    public static int unsignedShort(short s) {
        return s & 0xFFFF;
    }

    //convert int to short
    public static short intToShort(int i) {
        return (short) (i & 0xFFFF);
    }


}
