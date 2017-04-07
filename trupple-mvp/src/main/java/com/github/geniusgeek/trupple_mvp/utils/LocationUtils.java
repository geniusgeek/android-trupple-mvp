package com.github.geniusgeek.trupple_mvp.utils;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import rx.Observable;
import rx.functions.Func0;

/**
 * This class serves the responsibility of doing location centric operations such as comparison etc
 * Created by Genius on 12/24/2015.
 */
public final class LocationUtils {
    public final static double AVERAGE_RADIUS_OF_EARTH = 6371;
    /**
     * 1 degree of latitude ~= 69 miles
     * 1 degree of longitude ~= cos(latitude)*69 miles
     */
    public static final int REQUEST_CODE = 100;
    private static final double MILES_DISTANCE = 5;
    private static final String TAG = LocationUtils.class.getSimpleName();

    /**
     * @throws AssertionError when trying to create an instance of this class
     */
    private LocationUtils() {
        throw new AssertionError("cannot instantiate this class");
    }


    /**
     * request Google location place
     * @param activity
     * @param requestCode
     */
    public static final void requestGooglePlace(Activity activity, int requestCode) {
        try {
            // The autocomplete activity requires Google Play Services to be available. The intent
            // builder checks this and throws an exception if it is not the case.
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(activity);
            Utils.hideSoftKeyboard(activity);
            activity.startActivityForResult(intent, requestCode);
        } catch (GooglePlayServicesRepairableException e) {
            // Indicates that Google Play Services is either not installed or not up to date. Prompt
            // the user to correct the issue.
            GoogleApiAvailability.getInstance().getErrorDialog(activity, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            // Indicates that Google Play Services is not available and the problem is not easily
            // resolvable.
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);

            Log.e(TAG, message);
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * convert from {@link android.location.Location} to {@link LatLng}
     *
     * @param location {@link android.location.Location} the location
     * @return returns {@link LatLng}
     */
    public static LatLng locationAdapter(android.location.Location location) {
        if (location == null) {
            return null;
        }
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    /**
     * convert from {@link Location} to {@link LatLng}
     *
     * @param location
     * @return
     * @patternUsed: Adapter Pattern(variant)
     * @see Location
     */
    public static LatLng latLngAdapter(android.location.Location location) {
        if (location == null) {
            return null;
        }
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    /**
     * get a location around a user with longituve and latitude and miles radius
     * @param y0
     * @param x0
     * @param radius
     * @return return {@link Location}
     */
    public static Location getALocationAround(double y0, double x0, int radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double miles = 1609.34 * radius;
        double radiusInDegrees = miles / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(y0);

        double foundLongitude = new_x + x0;
        double foundLatitude = y + y0;
        Location location = createLocation(foundLatitude, foundLongitude);
        return location;
    }


    /**
     * factory method to create location {@link Location} model of this application
     *
     * @param latitude the latitude
     * @param longitude the longitude
     * @return {@link Location}
     */
    @NonNull
    public static Location createLocation(double latitude, double longitude) {
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return location;
    }


    /**
     * get user locality {@link Observable}
     *
     * @param context the context
     * @param location the location
     * @param geoCodingApiKey the API key
     * @return  {@link Observable}
     */
    public static Observable<String> getLocalityObservable(final Context context, final Location location, final String geoCodingApiKey) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just(getLocality(context, location, geoCodingApiKey));
            }
        });

    }

    /**
     * get an observable for the country
     *
     * @param context the context
     * @param location the {@link Location}
     * @return  {@link Observable}
     */
    public static Observable<String> getCountryObservable(final Context context, final Location location) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just(getCountryName(context, location));
            }
        });

    }

    /**
     *  note this methos is called on the ui thread, therefore it is adviced that A threadpool or executor should be used to access
     * it, or the #getLocalityObservable Reactive approach should be used, this is left open-ended based on the developer preference
     * @param context the context
     * @param location the {@link Location}
     * @param geoCodingApiKey the API key
     * @return Locality
     */
    @Nullable
    public static String getLocality(Context context, Location location, String geoCodingApiKey) {
        List<Address> addressList = getAddresses(context, location);
        try {
            if (addressList.isEmpty())
                addressList = getStringFromLocation(location.getLatitude(), location.getLongitude(), geoCodingApiKey);

            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                return address.getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * get Addresses
     * @param context, the context
     * @param location, {@link Location}
     * @return List of Addresses
     */
    @Nullable
    private static List<Address> getAddresses(final Context context, final Location location) {
        Geocoder gcd = new Geocoder(context, Locale.getDefault());
        try {
            return gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "didnt get address");
        }
        return Collections.emptyList();
    }


    /**
     * get direct access to google maps to get the location from string
     *
     * @param address
     * @return
     * @throws JSONException
     * @throws UnsupportedEncodingException
     */
    public static LatLng getLocationFromString(String address) throws JSONException, UnsupportedEncodingException {

        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?address="
                + URLEncoder.encode(address, "UTF-8") + "&ka&sensor=false");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

        double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");

        double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");

        return new LatLng(lat, lng);
    }
  /*
    *//**
     * note this methos is called on the ui thread, therefore it is adviced that A threadpool or executor should be used to access
     * it, or the #getLocalityObservable Reactive approach should be used, this is left open-ended based on the developer preference
     *
     * @param context
     * @param location
     * @return
     *//*

    @Nullable
    public static String getLocality(Context context, Location location) {
        List<Address> addressList = getAddresses(context, location);
        try {
            if (addressList.isEmpty())
                addressList = getStringFromLocation(location.getLatitude(), location.getLongitude(),
                        context.getString(R.string.geocoding_api_key));

            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                return address.getAddressLine(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }*/

    /**
     * get locality from GoogleMaps, this is a fallback plan when normal Geocoder fails to get location.
     *
     * @param lat
     * @param lng
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static List<Address> getStringFromLocation(double lat, double lng, String apiKey) throws IOException, JSONException {

        String address = String.format(Locale.ENGLISH, "https://maps.googleapis.com/maps/api/geocode/json" +
                "?latlng=%1$f,%2$f&key=%3$s&sensor=true&language="
                + Locale.getDefault().getCountry(), lat, lng, apiKey);
        Log.d(TAG, "address: " + address);

        HttpGet httpGet = new HttpGet(address);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        List<Address> retList = new ArrayList<>();

        response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream stream = entity.getContent();
        int b;
        while ((b = stream.read()) != -1) {
            stringBuilder.append((char) b);
        }

        JSONObject jsonObject = new JSONObject(stringBuilder.toString());

        if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String indiStr = result.getString("formatted_address");
                Address addr = new Address(Locale.getDefault());
                addr.setAddressLine(0, indiStr);
                retList.add(addr);
            }
        }

        return retList;
    }

    /**
     * get the user country name
     *
     * @param context
     * @param location
     * @return
     */
    public static String getCountryName(Context context, Location location) {
        List<Address> addresses = getAddresses(context, location);
        if (addresses.size() > 0)
            return addresses.get(0).getCountryName();
        return " "; //return null if something bad happened
    }

    /**
     * turn on the location manager
     *
     * @param context
     */
    public static void turnOnLocation(final Context context) {
        // Get Location Manager and check for GPS & Network location services
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            showLocationDialog(context);
        }
    }

    /**
     * @param locationOne
     * @param locationTwo
     * @return
     */
    public static double getKmDistanceApart(Location locationOne, Location locationTwo) {
        return calculateDistanceFaster(locationOne.getLatitude(), locationOne.getLongitude(), locationTwo.getLatitude(), locationTwo.getLongitude());
    }

    /**
     * show Location Dialog
     * @param context
     */
    public static void showLocationDialog(final Context context) {
        // Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Location Services Disabled");
        builder.setMessage("Please enable location services ");
        builder.setPositiveButton("ENABLE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show location settings when the user acknowledges the alert dialog
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }


    /**
     * check if two location is equal
     *
     * @param mLocation
     * @param location
     * @return
     */
    public static boolean isLocationEquals(Location mLocation, Location location) {
        return MathUtils.isEqual(mLocation.getLatitude(), location.getLatitude());
    }

    /**
     * check if two location is equal using their respective latitude
     *
     * @return
     */
    public static boolean isLocationEquals(double lat1, double lat2) {
        return MathUtils.isEqual(lat1, lat2);
    }

    /**
     * veryfy if the location service is enabled
     *
     * @param
     * @return
     */
    public static boolean isLocationServiceEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);//get the location service
        //check to make sure the passive is not returned as the best provider. if it is then location service is not available
        String provider = lm.getBestProvider(new Criteria(), true);//get enabled only provider
        return (!TextUtils.isEmpty(provider) && !LocationManager.PASSIVE_PROVIDER.equals(provider));//return true if location service is not passive
    }

    public static float calculateDistance(Location currentLocation, Location destLocation) {
        return currentLocation.distanceTo(destLocation);
    }


    /**
     * get the distance between two location using Havenstine formula
     *
     * @param userLat
     * @param userLng
     * @param venueLat
     * @param venueLng
     * @return
     * @see
     */
    public static int calculateDistance(double userLat, double userLng,
                                        double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH * c));
    }

    /**
     * get the distance between two location using Optimized Havenstine formula
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     * @see
     */
    public static double calculateDistanceFaster(double lat1, double lon1,
                                                  double lat2, double lon2) {
        double piDistance = 0.017453292519943295;    // Math.PI / 180

        double a = 0.5 - Math.cos((lat2 - lat1) * piDistance) / 2 +
                Math.cos(lat1 * piDistance) * Math.cos(lat2 * piDistance) *
                        (1 - Math.cos((lon2 - lon1) * piDistance)) / 2;
        return 12742 * Math.asin(Math.sqrt(a)); // 2 * R; R = 6371 km
    }

    /**
     * get the distance between two close location  if (lat1,lon1) ~ (lat2,lon2)
     *
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     * @see
     */
    private static double calculateDistanceBetweenClosePoints(double lat1, double lon1,
                                                              double lat2, double lon2) {
        // Approximate Equirectangular -- works if (lat1,lon1) ~ (lat2,lon2)
        double x = (lon2 - lon1) * Math.cos((lat1 + lat2) / 2);
        double y = (lat2 - lat1);
        return 6371 * Math.sqrt(x * x + y * y);// where R is the earth's Radiu// 2 * R; R = 6371 km
    }

    public static boolean isLolipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    /**
     * Check if google play is installed or not
     *
     * @param context
     * @param request
     * @return
     */
    public static boolean checkPlayServices(Activity context, int request) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(context);
        try {
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activityWeakReference.get());
            if (resultCode == ConnectionResult.SUCCESS)
                 return true;

            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activityWeakReference.get(),
                        request).show();
            }
         } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return false;

    }

    /**
     * check location permission request
     *
     * @param context
     * @param permission_request
     * @return
     */
    public static final boolean checkLocationPermission(Activity context, int permission_request) {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        permission_request);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        permission_request);
            }
            return false;
        } else {
            return true;
        }
    }


    /**
     * Prompt user to enable GPS and Location Services
     *
     * @param mGoogleApiClient
     * @param activity
     */
    public static void locationChecker(GoogleApiClient mGoogleApiClient, final Activity activity) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(getResultCallback(activity));
    }

    @NonNull
    private static ResultCallback<LocationSettingsResult> getResultCallback(final Activity activity) {
        return new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //do the location based operation here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        };
    }


    /**
     * Prompt user to enable GPS and Location Services
     * @param mGoogleApiClient
     * @param activity
     */
    public static void locationChecker(LocationRequest locationRequest,GoogleApiClient mGoogleApiClient, final Activity activity) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(getResultCallback(activity));
    }
}
