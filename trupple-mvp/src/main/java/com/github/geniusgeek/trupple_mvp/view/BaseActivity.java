package com.github.geniusgeek.trupple_mvp.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.github.geniusgeek.trupple_mvp.MVP;

import java.io.Serializable;
import java.util.List;


/**
 * Created by DOTECH on 4/20/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements MVP.GenericViewOps {
    public static final String TAG = "BaseActivity";
    private ProgressDialog dialog;
    private ViewDataBinding mBinding;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, getLayout());
    }


    public void showShortToast(String text) {
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_SHORT).show();
    }

    public void showShortSnackbar(String text, int color) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar
                .make(parentLayout, text, Snackbar.LENGTH_SHORT)
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light));
        snackbar.show();
    }

    public ViewDataBinding getBindingInstance() {
        return mBinding;
    }


    /**
     * change the color of the status bar
     *
     * @param context the current application context
     * @param color   the color resource id  to use
     */
    protected final void changeStatusBarColor(Context context, @ColorRes int color) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(context, color));
        }
    }

    /**
     * Attach a {@link android.support.v4.app.Fragment} to a view, usually a
     * {@link android.view.ViewGroup}. The view is provided as resource ID, as
     * present in
     *
     * @param fragment       The Fragment to attach.
     * @param viewId         The resource ID for the view to attach the fragment to, as
     *                       found in R.id.
     * @param addToBackStack {@literal true} to allow the user to undo the operation with
     *                       the device's back button.
     * @param tag            name to give the Fragment as it is connected to the UI
     * @param context        An {@link android.support.v4.app.FragmentActivity} that hosts
     *                       the fragment and the view.
     */
    public void attachFragmentToView(Fragment fragment, int viewId,
                                     boolean addToBackStack, String tag, FragmentActivity context) {
        FragmentManager fragMan = context.getSupportFragmentManager();
        FragmentTransaction transaction = fragMan.beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.add(viewId, fragment, tag);
        transaction.commit();
    }

    /**
     * set and return the toolbar
     *
     * @param title
     * @param toolbarId
     * @param enableHome
     * @return
     */
    public Toolbar setToolbar(String title, int toolbarId, boolean enableHome) {
        toolbar = (Toolbar) findViewById(toolbarId);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setHomeButtonEnabled(enableHome);
            getSupportActionBar().setDisplayHomeAsUpEnabled(enableHome);
        }
        return toolbar;
    }

    /**
     * set and return the toolbar
     *
     * @param title
     * @param toolbar    the toolbar to add
     * @param enableHome
     * @return
     */
    public Toolbar setToolbar(String title, Toolbar toolbar, boolean enableHome) {
        if (toolbar != null) {
            this.toolbar = toolbar;
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setHomeButtonEnabled(enableHome);
            getSupportActionBar().setDisplayHomeAsUpEnabled(enableHome);
        }
        return toolbar;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void showProgress(String title, String text) {
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle(title);
        dialog.setMessage(text);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void hideProgress() {
        dialog.dismiss();
    }


    /**
     * template method to return the layout to be used
     *
     * @return
     */
    protected abstract int getLayout();

    @Override
    public FragmentActivity getActivity() {
        return this;
    }

    /**
     * Return the Activity context.
     */
    @Override
    public Context getActivityContext() {
        return this;
    }

    /**
     * Return the Application context.
     */
    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }


    @Override
    public <Type, V> void goToNextActivity(Class<? extends Activity> clazz, Type keyExtras, V valueExtras) {
        Intent intent = new Intent(getActivityContext(), clazz);
        if (keyExtras != null && valueExtras != null) {
            intent.putExtra(keyExtras.toString(), valueExtras.toString());
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    protected void loadPermissions(String perm, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
                ActivityCompat.requestPermissions(this, new String[]{perm}, requestCode);
            }
        }
    }

    /**
     * find fragment by the tag
     *
     * @param tag
     * @return
     */
    public Fragment findFragmentByTag(String tag) {
        FragmentManager mFragmentMgr = getSupportFragmentManager();
        return mFragmentMgr.findFragmentByTag(tag);
    }

    /**
     * remove a fragment if it exist
     *
     * @param tag
     * @return
     */
    public boolean removeFragmentIfExist(String tag) {
        FragmentManager mFragmentMgr = getSupportFragmentManager();
        FragmentTransaction mTransaction = mFragmentMgr.beginTransaction();
        Fragment childFragment = findFragmentByTag(tag);
        if (childFragment == null)
            return true;
        mTransaction.remove(childFragment);
        mTransaction.commit();
        return false;
    }

    /**
     * remove a fragment if it exist
     *
     * @param fragment
     * @return
     */
    @SuppressWarnings({"@Nullable", "@NonNull", "RestrictedApi"})
    public boolean removeFragmentIfExist(Fragment fragment) {
        FragmentManager mFragmentMgr = getSupportFragmentManager();
        List<Fragment> fragmentList = mFragmentMgr.getFragments();
        if (fragment != null && fragmentList.contains(fragment)) {
            FragmentTransaction ft = mFragmentMgr.beginTransaction();
            ft.remove(fragment);
            ft.commit();
            return true;
        }
        return false;
    }

    /**
     * show a fragment on an instance of fragmentTransaction
     *
     * @param fragment the fragment
     * @return the fragment transaction who is incharge of the current operation
     * @see FragmentTransaction
     */
    @NonNull
    public FragmentTransaction showFragmentOnFragmentTransaction(int id, Fragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(id, fragment, tag);

        // ft.add (R.id.timeline_coordinatorLayout,fragment,tag);
        return ft;
    }

    /**
     * get the current visible fragment
     *
     * @return
     */
    @SuppressWarnings({"@Nullable", "@NonNull", "RestrictedApi"})
    public Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    /**
     * TODO: please rewire this into the framework, therefore create a GenericPresenter class to do all these
     * <T></>
     *
     * @param tag
     * @param view
     * @return
     */
    protected <T extends Serializable, A extends Activity> void putSerializableToIntent(String tag, T value, A view) {
        Intent intent = view.getIntent();
        intent.putExtra(tag, value);
    }

    /**
     * TODO: please rewire this into the framework
     * <T></>
     *
     * @param tag
     * @param view
     * @return
     */
    protected <T extends String, A extends Activity> void putStringToIntent(String tag, String value, A view) {
        Intent intent = view.getIntent();
        intent.putExtra(tag, value);
    }

    /**
     * set visibility of a view
     *
     * @param view
     * @param visibility
     */
    public void setViewVisibility(View view, boolean visibility) {
        if (view == null)
            return;
        view.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }


    /**
     * toggle visibility on a view
     *
     * @param view
      */
    public void toggleVisibility(View view) {
        if (view == null)
            return;
        view.setVisibility(view.getVisibility()==View.VISIBLE ? View.GONE : View.VISIBLE);
    }


    /**
     * force the scrollview to scroll to the bottom if need be
     *
     * @param scrollId
     */
    protected final void scrollToBottom(@IdRes int scrollId) {
        final NestedScrollView scrollview = (NestedScrollView) findViewById(scrollId);
        scrollview.post(new Runnable() {
            @Override
            public void run() {
                scrollview.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

}