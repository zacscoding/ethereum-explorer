package org.explorer.util;

import java.math.BigInteger;

/**
 * @author zacconding
 * @Date 2018-06-17
 * @GitHub : https://github.com/zacscoding
 */
public class BIUtil {

    public static boolean isZero(BigInteger value) {
        return BigInteger.ZERO.equals(value);
    }

    public static boolean isLessThan(BigInteger left, long right) {
        return isLessThan(left, BigInteger.valueOf(right));
    }

    public static boolean isLessThan(BigInteger left, BigInteger right) {
        return left.compareTo(right) < 0;
    }
}
