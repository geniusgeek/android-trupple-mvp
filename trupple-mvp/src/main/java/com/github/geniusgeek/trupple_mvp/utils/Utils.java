package com.github.geniusgeek.trupple_mvp.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.WorkerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    /**
     * get valueset of a map
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Set<V> valueSet(final Map<K, V> map) {
        return new HashSet<>(map.values());
    }

    /**
     *
     * @param input
     * @return
     */
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

    /**
     * hide keyboard input
     * @param activity
     */
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * remove a character from a string
     * @param s
     * @param c
     * @return
     */
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

    /**
     * delete non digit from string
     * @param s
     * @return
     */
    public static String deleteAllNonDigit(String s) {
        String temp = s.replaceAll("\\D", "");
        return temp;
    }

    /**
     * test equality of two string insensitive of Whitespace and Case
     * @param a
     * @param b
     * @return
     */

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

   /**
     * A method to download json data from url
     */
    @WorkerThread
    public static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception while dow url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
