package com.github.geniusgeek.trupple_mvp.presenter;

import android.app.Activity;

import com.github.geniusgeek.trupple_mvp.view.ContextView;

/**
 * The base interface that an operations ("Ops") class in the
 * Presenter layer must implement.
 * NOte this can be used for any application
 */
public interface PresenterOps<RequiredViewOps extends ContextView> {
    /**
     * Hook method dispatched by the GenericActivity framework to
     * initialize an operations ("Ops") object after it's been
     * instantiated.
     *
     * @param view The currently active RequiredViewOps.
     */
    void onCreate(RequiredViewOps view);

    /**
     * Hook method dispatched by the GenericActivity framework to
     * update an operations ("Ops") object after a runtime
     * configuration change has occurred in the View layer.
     *
     * @param view The currently active RequiredViewOps.
     */
    void onConfigurationChange(RequiredViewOps view);

    /**
     * Hook method called when an Ops object in the Presenter layer is
     * destroyed.
     *
     * @param isChangingConfigurations True if a runtime configuration triggered the onDestroy() call.
     */
    void onDestroy(boolean isChangingConfigurations);

    /**
     * forward the call to the activity to navigate to the next activity
     *
     * @param activityClass
     */
    void goToNextActivity(Class<? extends Activity> activityClass);


}
