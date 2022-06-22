package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    // 半形字元 Unicode 區間
    private static final String halfWidthRange = "\u0000-\u00FF";
    // 日文半形字元 Unicode 區間
    private static final String halfWidthKana = "\uFF61-\uFF9F";
    // 組合
    private static final String regex = "[" + halfWidthRange + halfWidthKana + "]";
    private static final Pattern pattern = Pattern.compile(regex);

    /**
     * 檢查字元，是否為半形、或全形字元。若是半形字元，則回傳 1；不然，則回傳 2
     * @param character 欲檢查的字元
     * @return 半形：1；全形 2
     */
    public static int widthOf(char character) {
        return checkWidthOf(character);
    }

    /**
     * 檢查字串的總長度(半形：1；全形：2)
     * @param str 欲檢查的字串
     * @return 總長度(半形：1；全形：2)
     */
    public static int widthOf(String str) {
        int widths = 0;
        for(int i = 0; i < str.length(); i++){
            widths += checkWidthOf(str.charAt(i));
        }
        return widths;
    }

    /**
     * 檢查字元，是否為半形、或全形字元。若是半形字元，則回傳 1；不然，則回傳 2
     * @param character 欲檢查的字元
     * @return 半形：1；全形 2
     */
    private static int checkWidthOf(char character) {
        Matcher matcher = pattern.matcher(String.valueOf(character));
        // 符合半形字元 Unicode 區間，則回傳 1；不然，則回傳 2
        return matcher.matches() ? 1 : 2;
    }

    /**
     * 回傳以半、全形的方式(半形：1、全形：2)，計算長度，切割後的字串；並且，不包含未被完整切割的字元
     * @param str 欲切割的字串
     * @param width 欲切割的長度
     * @return 切割後的字串
     */
    public static String slice(String str, int width) {
        return slice(str, width, false);
    }

    /**
     * 回傳以半、全形的方式，計算長度(半形：1、全形：2)，切割後的字串
     * @param str 欲切割的字串
     * @param width 欲切割的長度
     * @param includeCutChar 是否包含未被完整切割的字元
     * @return 切割後的字串
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

    /**
     *
     * @param str
     * @param afterWidth
     * @param slicingWidth
     * @param includeFirstCutChar
     * @param includeSecondCutChar
     * @return
     */
    public static String slice(String str, int afterWidth, int slicingWidth, boolean includeFirstCutChar, boolean includeSecondCutChar)
    {
        String subStr = tailStringOfSliced(str, afterWidth, !includeFirstCutChar);
        return slice(subStr, slicingWidth, includeSecondCutChar);
    }

    /**
     *
     * @param str
     * @param width
     * @return
     */
    public static String sliceRPad(String str, int width)
    {
        String subString = slice(str, width, false);
        return rightPad(subString, width, ' ');
    }

    /**
     *
     * @param str
     * @param width
     * @param ch
     * @return
     */
    public static String sliceRPad(String str, int width, char ch)
    {
        String subString = slice(str, width, false);
        return rightPad(subString, width, ch);
    }

    /**
     *
     * @param str
     * @param width
     * @return
     */
    public static String sliceLPad(String str, int width)
    {
        String subString = slice(str, width, false);
        return leftPad(subString, width, ' ');
    }

    public static String sliceLPad(String str, int width, char ch)
    {
        String subString = slice(str, width, false);
        return leftPad(subString, width, ch);
    }

    public static String tailStringOfSliced(String str, int width, boolean includeCutChar) {
        String headString = slice(str, width, includeCutChar);
        return headString.length() < str.length() ? str.substring(headString.length()) : "";
    }

    public static String[] split(String str, int width) {
        return split(str, width, false);
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

    /**
     *
     * @param str
     * @param totalWidth
     * @return
     */
    public static String leftPad(String str, int totalWidth) {
        return leftPad(str, totalWidth, ' ');
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

    public static String rightPad(String str, int totalWidth)
    {
        return rightPad(str, totalWidth, ' ');
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
