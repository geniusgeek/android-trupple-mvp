package com.github.geniusgeek.trupple_mvp.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

/**
 * Defines methods for obtaining Contexts used by all views in the
 * "View" layer.
 */
public interface ContextView {
    /**
     * Get the Activity Context.
     */
    Context getActivityContext();

    /**
     * Get the Application Context.
     */
    Context getApplicationContext();

    /**
     * get the activity
     *
     * @return
     */
    FragmentActivity getActivity();

    /**
     * go to the next activity,
     * NOTE: please the extra should be an array containing key value
     *
     * @param clazz
     */
    <K, V> void goToNextActivity(Class<? extends Activity> clazz, K keyExtras, V ValueExtras);


}

