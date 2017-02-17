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


  /*  public double calculateSegment(Location location){
        double longitude=location.getLongitude();
        double latitude=location.getLatitude();
        Point point = new Point(latitude,longitude);
        double radius=LocationUtils.FIVE_MILES_RADIUS;
        double area=Math.PI*Math.pow(radius,2);
    }*/


    public static boolean isEqual(double... val) {

        BigDecimal a = roundingUpBigDecimal(val[0], 3);
        for (double value : val) {
            BigDecimal b = roundingUpBigDecimal(value, 3);
            if (a.equals(b)) return true;
        }
        return false;
    }

    public static float roundingUpFloat(double number, int numberOfDecimalPt) {
        return new BigDecimal(number).setScale(numberOfDecimalPt, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static BigDecimal roundingUpBigDecimal(double number, int numberOfDecimalPt) {
        return new BigDecimal(number).setScale(numberOfDecimalPt, BigDecimal.ROUND_HALF_UP);

    }
}
