/*
 * Copyright (C) 2018. Uangel Corp. All rights reserved.
 *
 */

/**
 * String Util
 *
 * @file StringUtil.java
 * @author Tony Lim
 */

package media.platform.acsrmqsim.common;

/** 문자열 관련 Util */
public class StringUtil {
    private static final String STR_OK = "OK";
    private static final String STR_FAIL = "FAIL";

    private StringUtil() { }

    /** result 결과에 따라 정해진 포맷의 문자열을 리턴 */
    public static String getOkFail(boolean result) {
        return(result ? STR_OK : STR_FAIL);
    }

    public static boolean isNumeric(String str) {
        return str != null && str.matches("-?\\d+");
    }

    /** 문자열 비교 */
    public static boolean compareString(String a, String b) {
        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return  false;
        }

        return(a.equals(b));
    }
}
