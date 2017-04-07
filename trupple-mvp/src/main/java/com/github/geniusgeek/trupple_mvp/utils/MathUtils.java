package com.github.geniusgeek.trupple_mvp.utils;

import java.math.BigDecimal;

/**
 * Created by Genius on 12/24/2015.
 */
public final class MathUtils {

    /**
     * @throws AssertionError when trying to create an instance of this class
     */
    private MathUtils() {
        throw new AssertionError("cannot instantiate this class");
    }


    /**
     * check if two or more values are equal
     * @param val
     * @return
     */
    public static boolean isEqual(double vala,double... val) {

        BigDecimal a = roundingUpBigDecimal(vala, 3);
        for (double value : val) {
            BigDecimal b = roundingUpBigDecimal(value, 3);
            if (a.equals(b)) return true;
        }
        return false;
    }

    /**
     * roundup a number
     * @param number the digit to roundup
     * @param numberOfDecimalPt the number of decimal points to roundup to
     * @return
     */
    public static float roundingUpFloat(double number, int numberOfDecimalPt) {
        return new BigDecimal(number).setScale(numberOfDecimalPt, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * roundup a number
     * @param number the digit to roundup
     * @param numberOfDecimalPt the number of decimal points to roundup to
     * @return
     */
    public static BigDecimal roundingUpBigDecimal(double number, int numberOfDecimalPt) {
        return new BigDecimal(number).setScale(numberOfDecimalPt, BigDecimal.ROUND_HALF_UP);

    }
}
