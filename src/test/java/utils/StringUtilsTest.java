package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void widthOf() {
        assertEquals(1, StringUtils.widthOf('ｶ'));
        assertEquals(2, StringUtils.widthOf('施'));
        assertEquals(2, StringUtils.widthOf('Ｌ'));
        assertEquals(1, StringUtils.widthOf('a'));
        assertEquals(1, StringUtils.widthOf(','));
    }

    @Test
    void testWidthOf() {
        assertEquals(1, StringUtils.widthOf(","));
        assertEquals(3, StringUtils.widthOf(",好"));
        assertEquals(5, StringUtils.widthOf(",好Ｌ"));
        assertEquals(6, StringUtils.widthOf(",好Ｌｶ"));
        assertEquals(7, StringUtils.widthOf(",好Ｌｶa"));
        assertEquals(8, StringUtils.widthOf(",好ＬｶaA"));
    }

    @Test
    void testWidthOf1() {
//        assertEquals(3, StringUtils.widthOf(",好ＬｶaA", -1, 1));
        assertEquals(2, StringUtils.widthOf(",好ＬｶaA", 1, 2));
        assertEquals(2, StringUtils.widthOf(",好ＬｶaA", 4, 6));
        assertEquals(0, StringUtils.widthOf(",好ＬｶaA", 1, 1));
//        assertEquals(2, StringUtils.widthOf(",好ＬｶaA", 4, 11));
    }

    @Test
    void testWidthOf2() {
        assertEquals(5, StringUtils.widthOf(",好ＬｶaA", 2));
    }

    @Test
    void slice() {
        assertEquals(",好Ｌ", StringUtils.slice(",好Ｌ", 8, true));
        assertEquals(",好", StringUtils.slice(",好ＬｶaA", 4, false));
        assertEquals(",好ＬｶaA", StringUtils.slice(",好ＬｶaA", 40, false));
        assertEquals("", StringUtils.slice(",好ＬｶaA", 0, false));
    }

    @Test
    void slice2() {
        assertEquals("好Ｌ", StringUtils.slice(",好ＬｶaA", 2, 3, true, true));
        assertEquals("好", StringUtils.slice(",好ＬｶaA", 2, 3, true, false));
        assertEquals("Ｌ", StringUtils.slice(",好ＬｶaA", 2, 1, false, true));
        assertEquals("", StringUtils.slice(",好ＬｶaA", 2, 1, false, false));
        assertEquals("Ｌｶ", StringUtils.slice(",好ＬｶaA", 2, 3, false, false));
        assertEquals("ＬｶaA", StringUtils.slice(",好ＬｶaA", 2, 13, false, false));
        assertEquals("", StringUtils.slice(",好ＬｶaA", 12, -13, false, false));
        assertEquals("", StringUtils.slice(",好ＬｶaA", 12, 13, false, false));
        assertEquals(",", StringUtils.slice(",好ＬｶaA", -12, 2, false, false));
        assertEquals(",好", StringUtils.slice(",好ＬｶaA", -12, 2, false, true));
    }

    @Test
    void tailStringOfSliced() {
        assertEquals("ｶaA", StringUtils.tailStringOfSliced(",好ＬｶaA", 4, true));
        assertEquals("ＬｶaA", StringUtils.tailStringOfSliced(",好ＬｶaA", 4, false));
        assertEquals("", StringUtils.tailStringOfSliced(",好ＬｶaA", 40, false));
        assertEquals(",好ＬｶaA", StringUtils.tailStringOfSliced(",好ＬｶaA", 0, false));
    }

    @Test
    void split() {
        assertArrayEquals(new String[]{"a"}, StringUtils.split("a好好好", 1, false));
        assertArrayEquals(new String[]{"a", "好", "好", "好"}, StringUtils.split("a好好好", 1, true));
        assertArrayEquals(new String[]{"a好", "好", "好"}, StringUtils.split("a好好好", 2, true));
        assertArrayEquals(new String[]{"a", "好", "好", "好"}, StringUtils.split("a好好好", 2, false));
        assertArrayEquals(new String[]{"a好", "好好"}, StringUtils.split("a好好好", 3, true));
        assertArrayEquals(new String[]{"a好", "好", "好"}, StringUtils.split("a好好好", 3, false));
        assertArrayEquals(new String[]{}, StringUtils.split("a好好好", 0, false));
        assertArrayEquals(new String[]{}, StringUtils.split("a好好好", -1, false));
        assertArrayEquals(new String[]{"a好好好"}, StringUtils.split("a好好好", 11, false));
    }

    @Test
    void indexOf() {
        assertEquals(1, StringUtils.indexOf(",好ＬｶaA", 2, true));
        assertEquals(0, StringUtils.indexOf(",好ＬｶaA", 2, false));
        assertEquals(-1, StringUtils.indexOf(",好ＬｶaA", 11, true));
        assertEquals(-1, StringUtils.indexOf(",好ＬｶaA", 11, false));
        assertEquals(-1, StringUtils.indexOf(",好ＬｶaA", -1, true));
        assertEquals(-1, StringUtils.indexOf(",好ＬｶaA", -1, false));
        assertEquals(5, StringUtils.indexOf(",好ＬｶaA", 8, true));
        assertEquals(5, StringUtils.indexOf(",好ＬｶaA", 8, false));
    }

    @Test
    void leftPad() {
        assertEquals("123", StringUtils.leftPad("123", -1, 'x'));
        assertEquals("123", StringUtils.leftPad("123", 0, 'x'));
        assertEquals("123", StringUtils.leftPad("123", 1, 'x'));
        assertEquals("123", StringUtils.leftPad("123", 3, 'x'));
        assertEquals("xx123", StringUtils.leftPad("123", 5, 'x'));
        assertEquals("x12Ｌ", StringUtils.leftPad("12Ｌ", 5, 'x'));
        assertEquals("x12喔", StringUtils.leftPad("12喔", 5, 'x'));
    }

    @Test
    void rightPad() {
        assertEquals("123", StringUtils.rightPad("123", -1, 'x'));
        assertEquals("123", StringUtils.rightPad("123", 0, 'x'));
        assertEquals("123", StringUtils.rightPad("123", 1, 'x'));
        assertEquals("123", StringUtils.rightPad("123", 3, 'x'));
        assertEquals("123xx", StringUtils.rightPad("123", 5, 'x'));
        assertEquals("12Ｌx", StringUtils.rightPad("12Ｌ", 5, 'x'));
        assertEquals("12喔x", StringUtils.rightPad("12喔", 5, 'x'));
    }

}