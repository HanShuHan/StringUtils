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
     * @param length 欲切割的長度
     * @return 切割後的字串
     */
    public static String slice(String str, int length) {
        return slice(str, length, false);
    }

    /**
     * 回傳以半、全形的方式，計算長度(半形：1、全形：2)，切割後的字串
     * @param str 欲切割的字串
     * @param length 欲切割的長度
     * @param includeCutChar 是否包含未被完整切割的字元
     * @return 切割後的字串
     */
    public static String slice(String str, int length, boolean includeCutChar) {
        if (length <= 0)
            return "";

        int currentWidth = 0;
        for (int i = 0; i < str.length(); i++) {
            currentWidth += widthOf(str.charAt(i));
            // 長度相等時
            if (currentWidth == length)
                return str.substring(0, i + 1);
            // 奇偶數問題
            if (currentWidth > length)
                return includeCutChar ? str.substring(0, i + 1) : str.substring(0, i);
        }
        // str.equals("") OR width的長度 > str的Width
        return str;
    }

    /**
     * 回傳以半、全形的方式(半形：1、全形：2)，計算長度，切割後的字串；並且，可以選擇是否包含未被完整切割的字元
     * @param str 欲切割的字串
     * @param afterWidth 從多少長度之後開始切割
     * @param slicingWidth 欲切割多少長度的字串
     * @param includeFirstCutChar @param afterWidth 的長度計算，是否包含若未被完整切割的字元
     * @param includeSecondCutChar 新切割好的字串，是否包含末尾未被完整切割的字元
     * @return 切割後的字串
     */
    public static String slice(String str, int afterWidth, int slicingWidth, boolean includeFirstCutChar, boolean includeSecondCutChar)
    {
        String subStr = tailStringOfSliced(str, afterWidth, !includeFirstCutChar);
        return slice(subStr, slicingWidth, includeSecondCutChar);
    }

    /**
     * 回傳以半、全形的方式(半形：1、全形：2)，計算長度，切割字串後，所剩餘的字串
     * @param str 欲切割的字串
     * @param length 欲切割的字串長度
     * @param includeCutChar 被切割的字串，是否要包含為末尾末被完整切割的字元
     * @return 切割後剩餘的字串
     */
    public static String tailStringOfSliced(String str, int length, boolean includeCutChar) {
        String headString = slice(str, length, includeCutChar);
        return headString.length() < str.length() ? str.substring(headString.length()) : "";
    }

    public static String[] split(String str, int length) {
        return split(str, length, false);
    }

    /**
     * 將字串以固定長度切割後(計算方式：半形：1、全形：2)，依序存入陣列，回傳陣列
     * @param str 欲切割的字串
     * @param length 欲切割的字串長度
     * @param includeCutChar 每一次切割，是否包含該段末尾未被完整切割的字元
     * @return 切割處理後的字串陣列
     */
    public static String[] split(String str, int length, boolean includeCutChar)
    {
        List<String> strList = new ArrayList<>();
        String tailString = str;
        String currentString = "";
        while (tailString.length() > 0 && currentString.length() != tailString.length())
        {
            currentString = tailString;
            String slicedStr = slice(tailString, length, includeCutChar);
            if(!slicedStr.equals(""))
                strList.add(slice(tailString, length, includeCutChar));
            tailString = tailStringOfSliced(tailString, length, includeCutChar);
        }
        String[] strings = new String[strList.size()];
        return strList.toArray(strings);
    }

    /**
     * 以所選擇的長度，切割輸入的字串後，做字串補滿的動作(Padding)
     * @param str 欲切割處理的字串。
     * @param length 欲切割的長度，以半形、全形計算(半形：1，全形：2)。
     * @param option 切割完成後的字串，是否需要做以半形空白補滿的動作？
     *               -1：左邊補空白。
     *               1：右邊補空白。
     *               0或其它：不補空白。
     * @return 處理完成後的字串。
     */
    public static String slice(String str, int length, int option) {
        return slice(str, length, option, ' ');
    }

    /**
     * 以所選擇的長度，切割輸入的字串後。再以所希望的字元，做字串補滿的動作(Padding)
     * @param str 欲切割處理的字串。
     * @param length 欲切割的長度，以半形、全形計算(半形：1，全形：2)。
     * @param option 切割完成後的字串，是否需要做以半形空白補滿的動作？
     *               -1：左邊補空白。
     *               1：右邊補空白。
     *               0或其它：不補空白。
     * @param padChar 欲用來做補滿動作的字元。
     * @return 處理完成後的字串。
     */
    public static String slice(String str, int length, int option, char padChar) {
        String subString = slice(str, length, false);
        switch (option) {
            case -1:
                return leftPad(subString, length, padChar);
            case 1:
                return rightPad(subString, length, padChar);
            default:
                return subString;
        }
    }
    
    /**
     * 以 slice(str, length, false) 方法，切割後的字串；使用 Right Padding 處理字串後，回傳
     * @param str 欲處理的字串
     * @param length 欲切割的總長度，以半、全形計算(半形：1、全形：2)
     * @return 切割後的字串，以 Right Padding 處理，將字串右邊，以半形空白補滿後，回傳
     */
    public static String sliceRPad(String str, int length)
    {
        String subString = slice(str, length, false);
        return rightPad(subString, length, ' ');
    }

    /**
     * 以 slice(str, length, false) 方法，切割後的字串；使用 Right Padding 處理字串後，回傳
     * @param str 欲處理的字串
     * @param length 欲切割的總長度，以半、全形計算(半形：1、全形：2)
     * @param ch 欲 Padding 的字元
     * @return 切割後的字串，以 Right Padding 處理，將字串右邊，以選擇的字元補滿後，回傳
     */
    public static String sliceRPad(String str, int length, char ch)
    {
        String subString = slice(str, length, false);
        return rightPad(subString, length, ch);
    }

    /**
     * 以 slice(str, length, false) 方法，切割後的字串；使用 Left Padding 處理字串後，回傳
     * @param str 欲處理的字串
     * @param length 欲切割的總長度，以半、全形計算(半形：1、全形：2)
     * @return 切割後的字串，以 Left Padding 處理，將字串左邊，以半形空白補滿後，回傳
     */
    public static String sliceLPad(String str, int length)
    {
        String subString = slice(str, length, false);
        return leftPad(subString, length, ' ');
    }

    /**
     * 以 slice(str, length, false) 方法，切割後的字串；使用 Left Padding 處理字串後，回傳
     * @param str 欲處理的字串
     * @param length 欲切割的總長度，以半、全形計算(半形：1、全形：2)
     * @param ch 欲 Padding 的字元
     * @return 切割後的字串，以 Left Padding 處理，將字串左邊，以選擇的字元補滿後，回傳
     */
    public static String sliceLPad(String str, int length, char ch)
    {
        String subString = slice(str, length, false);
        return leftPad(subString, length, ch);
    }

    /**
     * 將字串以 Left Padding 處理後，回傳
     * @param str 欲加工處理的字串
     * @param length 預期的字串總長度，以(半形：1；全形：2)的方式計算
     * @return Left Padding 後的字串
     */
    public static String leftPad(String str, int length) {
        return leftPad(str, length, ' ');
    }

    /**
     * 將字串以所選擇的字元，做 Left Padding 處理後，回傳
     * @param str 欲加工處理的字串
     * @param length 預期的字串總長度，以(半形：1；全形：2)的方式計算
     * @param ch 欲用來 Padding 的字元
     * @return 處理後的字串
     */
    public static String leftPad(String str, int length, char ch)
    {
        int padRound = length - widthOf(str);
        StringBuilder sb = new StringBuilder();
        while(padRound > 0)
        {
            sb.append(ch);
            padRound--;
        }
        return sb + str;
    }

    /**
     * 將字串以 Right Padding 處理後，回傳
     * @param str 欲加工處理的字串
     * @param length 預期的字串總長度，以(半形：1；全形：2)的方式計算
     * @return Right Padding 後的字串
     */
    public static String rightPad(String str, int length)
    {
        return rightPad(str, length, ' ');
    }

    /**
     * 將字串以所選擇的字元，做 Right Padding 處理後，回傳
     * @param str 欲加工處理的字串
     * @param length 預期的字串總長度，以(半形：1；全形：2)的方式計算
     * @param ch 欲用來 Padding 的字元
     * @return 處理後的字串
     */
    public static String rightPad(String str, int length, char ch)
    {
        int padRound = length - widthOf(str);
        StringBuilder sb = new StringBuilder();
        while(padRound > 0)
        {
            sb.append(ch);
            padRound--;
        }
        return str + sb;
    }

}
