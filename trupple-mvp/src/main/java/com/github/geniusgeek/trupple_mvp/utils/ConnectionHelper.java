package com.github.geniusgeek.trupple_mvp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * utility static class for connection related operations
 * Created by DOTECH on 17/02/2016.
 */
public final class ConnectionHelper {
    private static final String TAG = "ConnectionHelper";
    public static long lastNoConnectionTs = -1;
    public static boolean isOnline = true;
    private static int networkId;

    private ConnectionHelper() {
        throw new AssertionError("cannot instantiate this class");
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        return isConnected;
    }

    public static boolean isConnectedOrConnecting(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }


    public static boolean isMobileDataConnected(Context context) {

        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean) method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }
        return mobileDataEnabled;
    }

    /**
     * @param context
     * @returns true if connected and false otherwise
     */
    public static boolean checkWifiConnectedState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

        //Toast.makeText(context, "Wifi Connected!", Toast.LENGTH_SHORT).show();
//Toast.makeText(context, "Wifi Not Connected!", Toast.LENGTH_SHORT).show();
        return activeNetInfo != null && activeNetInfo.isConnected() && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * get the mac address of the phone, please note this should never be public because security of the system
     * will be compromised if so.
     *
     * @param context
     * @return
     */
    private static String getMacAddress(Context context) {
        WifiManager wimanager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        String macAddress = wimanager.getConnectionInfo().getMacAddress();
        if (macAddress == null) {
            macAddress = "Device don't have mac address or wi-fi is disabled";
        }
        return macAddress;
    }


    /**
     * util to return the Ip address of the phone
     *
     * @return
     */
    public static String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces.nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    ip = inetAddress.getHostAddress();
                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip = "Something Wrong! " + e.toString();
        }

        return ip;
    }

    /**
     * check if the internet is connected in background
     *
     * @param context
     * @return
     */
    public static boolean isBackgroundNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected() && connectivityManager.getBackgroundDataSetting());

    }

    /**
     * check if the internet is connected in foreground
     *
     * @param context
     * @return
     */
    public static boolean isForegroundNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }
}
