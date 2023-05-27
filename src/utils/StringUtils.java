package utils;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class StringUtils
{

    public static String humanJavaTypeName(String camelCase) {
        String result = "";
        boolean prevUpperCase = false;
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (!Character.isLetter(c)) return camelCase;
            if (Character.isUpperCase(c) && !prevUpperCase) {
                if (i > 0) result += " ";
                result += c;
                prevUpperCase = true;
            }
            else {
                result += c;
                prevUpperCase = false;
            }
        }
        return result;
    }

    public static String camelCaseToUnderScoreUpperCase(String camelCase) {
        String result = "";
        boolean prevUpperCase = false;
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (!Character.isLetter(c)) return camelCase;
            if (Character.isUpperCase(c)) {
                if (prevUpperCase) return camelCase;
                result += "_" + c;
                prevUpperCase = true;
            }
            else {
                result += Character.toUpperCase(c);
                prevUpperCase = false;
            }
        }
        return result;
    }

    public static String trimVector3(Vector3 v) {
        return trimFloat(v.x) + ", " + trimFloat(v.y) + ", " + trimFloat(v.z);
    }

    //null safe trim float to 2 decimal places
    public static String trimFloat(float f) {
        if (f == (int) f) return String.valueOf((int) f);
        String string = String.format("%(-2.2f" , f);
        //ensure 5 digits
        if (string.length() <= 5) string += "0";
        return string;
    }

    public static boolean matchesSuffix(String string , String suffix) {
        int suffixLength = suffix.length();
        int stringLength = string.length();
        String stringSuffix = string.substring(stringLength - suffixLength);
        return stringSuffix.equals(suffix);

    }

    public static String formatMatrix4(Matrix4 matrix4){
        //format matrix 4 with trimmed floats

        float[] values = matrix4.getValues();
        String[] val = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            val[i] = trimFloat(values[i]);
        }
        //now format into 4 rows of 4 columns
        return "[" + val[0] + "|" + val[1] + "|" + val[2] + "|" + val[3] + "]\n" //
                + "[" + val[4] + "|" + val[5] + "|" + val[6] + "|" + val[7] + "]\n" //
                + "[" + val[8] + "|" + val[9] + "|" + val[10] + "|" + val[11] + "]\n" //
                + "[" + val[12] + "|" + val[13] + "|" + val[14] + "|" + val[15] + "]\n";



    }

    public static String extractFileType(String string){
        int lastDot = string.lastIndexOf(".");
        if(lastDot==-1) return "";
        return string.substring(lastDot+1);
    }

}
