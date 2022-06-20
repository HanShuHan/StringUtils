package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    /**
     * @param character 欲檢查是否為半形或全形的 char
     * @return 半形：1；全形：2
     */
    public static int widthOf(char character) {
        return checkWidthOf(character);
    }

    /**
     * @param str 欲檢查是否為半形或全形的 string
     * @return 半形：1；全形：2
     */
    public static int widthOf(String str) {
        int widths = 0;
        char[] chars = str.toCharArray();
        for (char c : chars)
            widths += checkWidthOf(c);
        return widths;
    }

    public static int widthOf(String str, int beginIndex, int endIndex) {
        String subString = str.substring(beginIndex, endIndex);
        return widthOf(subString);
    }

    public static int widthOf(String str, int beginIndex) {
        String subString = str.substring(beginIndex);
        return widthOf(subString);
    }

    /**
     * @param character 欲檢查是否為半形或全形的 char
     * @return 全形：2；半形：1
     */
    private static int checkWidthOf(char character) {
        String halfWidthRange = "\u0000-\u00FF";
        String halfWidthKana = "\uFF61-\uFF9F";
        String regex = "[" + halfWidthRange + halfWidthKana + "]";

        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(String.valueOf(character));
        return matcher.matches() ? 1 : 2;
    }

    /**
     * @param str            欲切割的字串
     * @param width          欲切割的長度(全形：2；半形：1)總計
     * @param includeCutChar 是否包括未被完整切割的字元
     * @return 傳回值
     */
    public static String slice(String str, int width, boolean includeCutChar) {
        if (width <= 0)
            return "";

        int currentWidth = 0;
        for (int i = 0; i < str.length(); i++) {
            currentWidth += widthOf(str.charAt(i));
            // 長度相等時
            if (currentWidth == width)
                return str.substring(0, i + 1);
            // 奇偶數問題
            if (currentWidth > width)
                return includeCutChar ? str.substring(0, i + 1) : str.substring(0, i);
        }
        // str.equals("") OR width的長度 > str的Width
        return str;
    }

    public static String tailStringOfSliced(String str, int width, boolean includeCutChar) {
        String headString = slice(str, width, includeCutChar);
        return headString.length() < str.length() ? str.substring(headString.length()) : "";
    }

    public static boolean isLastIndex(int index, String str) {
        if (str.equals(""))
            return index == 0;
        else
            return index == str.length() - 1;
    }

    public static String[] split(String str, int width, boolean includeCutChar)
    {
        List<String> strList = new ArrayList<>();
        String tailString = str;
        String currentString = "";
        while (tailString.length() > 0 && currentString.length() != tailString.length())
        {
            currentString = tailString;
            String slicedStr = slice(tailString, width, includeCutChar);
            if(!slicedStr.equals(""))
                strList.add(slice(tailString, width, includeCutChar));
            tailString = tailStringOfSliced(tailString, width, includeCutChar);
        }
        String[] strings = new String[strList.size()];
        return strList.toArray(strings);
    }

    public static int indexOf(String str, int fromWidth) {
        if (fromWidth <= 0)
            return -1;

        int currentWidth = 0;
        for (int i = 0; i < str.length(); i++) {
            currentWidth += widthOf(str.charAt(i));
            if (currentWidth >= fromWidth)
                return i;
        }
        return str.length() - 1;
    }

//    public static String slice(String str, int afterWidth, int slicingWidth, boolean includeCutChar)
//    {
//
//    }

}
