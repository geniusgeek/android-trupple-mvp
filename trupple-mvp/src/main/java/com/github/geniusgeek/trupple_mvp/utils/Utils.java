package com.github.geniusgeek.trupple_mvp.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by DOTECH and edited by on 02/09/2016.
 */
public final class Utils {


    /**
     * @throws AssertionError when trying to create an instance of this class
     */
    private Utils() {
        throw new AssertionError("cannot instantiate this class");
    }

    /**
     * generic method for converting an array to a modifiable list
     *
     * @param args
     * @param <T>
     * @return
     */
    public static <T> List<T> asList(T... args) {
        return new ArrayList<T>(Arrays.asList(args));
    }


    public static <K, V> Set<V> valueSet(final Map<K, V> map) {
        return new HashSet<>(map.values());
    }

    public static String shuffle(String input) {
        List<Character> characters = new ArrayList<>();
        for (char c : input.toCharArray()) {
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while (characters.size() != 0) {
            int randPicker = (int) (Math.random() * characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String removeChar(String s, char c) {
        StringBuffer buf = new StringBuffer(s.length());
        buf.setLength(s.length());
        int current = 0;
        for (int i = 0; i < s.length(); i++) {
            char cur = s.charAt(i);
            if (cur != c) buf.setCharAt(current++, cur);
        }
        return buf.toString();
    }

    public static String deleteAllNonDigit(String s) {
        String temp = s.replaceAll("\\D", "");
        return temp;
    }

    public static boolean caseAndWhiteSpaceInsensitiveEq(String a, String b) {
        a = (null != a) ? a.replaceAll("\\s+", "").toUpperCase() : null;
        b = (null != b) ? b.replaceAll("\\s+", "").toUpperCase() : null;
        if (null == a && null == b) {
            return true;
        }
        if (null == a || null == b) {
            return false;
        }
        return a.equals(b);
    }

    /**
     * calculate the number of columns to use for a gridview
     *
     * @param context
     * @return
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    /**
     * calculate the density of the screen
     *
     * @param context
     * @return
     */
    public static boolean isLargeScreen(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float widthDpi = metrics.xdpi;
        float heightDpi = metrics.ydpi;
        float widthInches = widthPixels / widthDpi;
        float heightInches = heightPixels / heightDpi;

        //a² + b² = c² ie
        // The size of the diagonal in inches is equal to the square root of the
        // height in inches squared plus the width in inches squared.
        double diagonalInches = Math.sqrt(
                (widthInches * widthInches)
                        + (heightInches * heightInches));
        return diagonalInches >= 7;
    }
}
