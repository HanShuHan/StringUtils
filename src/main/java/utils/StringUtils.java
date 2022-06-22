package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final String halfWidthRange = "\u0000-\u00FF";
    private static final String halfWidthKana = "\uFF61-\uFF9F";
    private static final String regex = "[" + halfWidthRange + halfWidthKana + "]";
    private static final Pattern pattern = Pattern.compile(regex);

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
        for(int i = 0; i < str.length(); i++){
            widths += checkWidthOf(str.charAt(i));
        }
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
        Matcher matcher = pattern.matcher(String.valueOf(character));
        return matcher.matches() ? 1 : 2;
    }

    /**
     * @param str 欲切割的字串
     * @param width 欲切割的長度(全形：2；半形：1)總計
     */
    public static void slice(String str, int width) {
        slice(str, width, false);
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

    public static String slice(String str, int afterWidth, int slicingWidth, boolean includeFirstCutChar, boolean includeSecondCutChar)
    {
        String subStr = tailStringOfSliced(str, afterWidth, !includeFirstCutChar);
        return slice(subStr, slicingWidth, includeSecondCutChar);
    }

    public static String tailStringOfSliced(String str, int width, boolean includeCutChar) {
        String headString = slice(str, width, includeCutChar);
        return headString.length() < str.length() ? str.substring(headString.length()) : "";
    }

    public static void split(String str, int width) {
        split(str, width, false);
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

    public static int indexOf(String str, int fromWidth, boolean includeCutChar) {
        if (fromWidth <= 0)
            return -1;

        int currentWidth = 0;
        for (int i = 0; i < str.length(); i++) {
            currentWidth += checkWidthOf(str.charAt(i));
            if (currentWidth == fromWidth)
                return i;
            if (currentWidth > fromWidth)
                return includeCutChar ? i : i - 1;
        }
        return -1;
    }

    public static String leftPad(String str, int totalWidth, char ch)
    {
        int padRound = totalWidth - widthOf(str);
        StringBuilder sb = new StringBuilder();
        while(padRound > 0)
        {
            sb.append(ch);
            padRound--;
        }
        return sb + str;
    }

    public static String rightPad(String str, int totalWidth, char ch)
    {
        int padRound = totalWidth - widthOf(str);
        StringBuilder sb = new StringBuilder();
        while(padRound > 0)
        {
            sb.append(ch);
            padRound--;
        }
        return str + sb;
    }

}
