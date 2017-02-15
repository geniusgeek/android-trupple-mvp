package com.github.geniusgeek.trupple_mvp.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;


import com.github.geniusgeek.trupple_mvp.common.ProgressDialogFragment;

import java.lang.reflect.Field;
import java.util.List;


/**
 * This is a generic Fragment that guarantees retainance of te instance on rotation.
 * Created by Genius on 4/11/16.
 */
public abstract class GenericFragment extends Fragment {
    /**
     * Debugging tag used by the Android logger.
     */
    public final static String TAG = new Object() {
    }.getClass().getEnclosingClass().getSimpleName();

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static <T extends Fragment> T newInstance(Bundle args, Class<T> clss) {

        T type = null;
        try {
            type = clss.newInstance();
            type.setArguments(args);
        } catch (InstantiationException | IllegalAccessException | java.lang.InstantiationException e) {
            e.printStackTrace();
        }

        return type;
    }

    public static <T extends Fragment> T newInstance(Class<T> clss) {
        try {
            return clss.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) new Fragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(retain());
    }

    /**
     * retain instance by default
     *
     * @return
     */
    protected boolean retain() {
        return true;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getFragmentView(), container, false);

    }

    /**
     * get the View for the Fragment
     *
     * @return
     */
    protected abstract int getFragmentView();


    protected ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    /**
     * innitialize any   recyclerview, this can be moved to a generic Activity/presenter
     */
    protected RecyclerView recyclerViewInnitializer(Activity context, int id, RecyclerView.Adapter<?> adapter,
                                                    RecyclerView.LayoutManager layoutManager) {
        RecyclerView recyclerView = (RecyclerView) context.findViewById(id);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        //ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration (context, R.dimen.item_offset);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        //recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(itemAnimator);
        return recyclerView;
    }

    public String getName() {
        return getClass().getSimpleName();
    }

    public void switchVisibility(final View view) {
        boolean visible = view.getVisibility() == View.VISIBLE;
        TranslateAnimation translateDown = new TranslateAnimation(0, 0, 0, view.getY());
        TranslateAnimation translateUp = new TranslateAnimation(0, 0, view.getY(), 0);
        translateDown.setDuration(300);
        translateDown.setFillAfter(true);
        translateUp.setDuration(300);
        translateUp.setFillAfter(true);
        view.setVisibility(visible ? View.INVISIBLE : View.VISIBLE);
        if (visible)
            view.startAnimation(translateUp);
        else
            view.startAnimation(translateDown);
    }


    protected void showProgress(String title, String text) {
        ProgressDialogFragment progressDialogFragment = (ProgressDialogFragment) ProgressDialogFragment.newInstance(title, text);
        progressDialogFragment.setTargetFragment(this, 0);
        progressDialogFragment.show(getFragmentManager(), ProgressDialogFragment.TAG);
    }

    protected void hideProgress() {
        ProgressDialogFragment fragment = (ProgressDialogFragment) getFragmentManager().findFragmentByTag(ProgressDialogFragment.TAG);

        if (fragment != null) {
            fragment.dismiss();
            getFragmentManager().beginTransaction().remove(fragment).commit();


        }

    }


    /**
     * set the childfragment to be accessible, to prevent state loss
     */
    protected final void setChildFragmentAccessible(FragmentManager fm) {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, fm);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This is necessary to have a clean ChildFragmentManager, old fragments might be called otherwise
     *
     * @param childFragmentManager
     */
    @SuppressWarnings({"@Nullable", "@NonNull", "RestrictedApi"})
    protected final void cleanChildFragments(FragmentManager childFragmentManager) {
        List<Fragment> childFragments = childFragmentManager.getFragments();
        if (childFragments != null && !childFragments.isEmpty()) {
            FragmentTransaction ft = childFragmentManager.beginTransaction();
            for (Fragment fragment : childFragments) {
                ft.remove(fragment);
            }
            ft.commit();
        }
    }


    /**
     * set mChildFragmentManager = null; to fix the bug in pre-lolipop and avoid the error
     * on {@link android.support.v4.view.ViewPager
     *
     * @error java.lang.IllegalStateException: Activity has been destroyed
     * at android.support.v4.app.FragmentManagerImpl.enqueueAction(FragmentManager.java:1515)
     * at android.support.v4.app.BackStackRecord.commitInternal(BackStackRecord.java:638)
     * at android.support.v4.app.BackStackRecord.commitAllowingStateLoss(BackStackRecord.java:621)
     * at android.support.v4.app.FragmentStatePagerAdapter.finishUpdate(FragmentStatePagerAdapter.java:162)
     * at android.support.v4.view.ViewPager.setAdapter(ViewPager.java:476)
     */
    /*@Override
    public void onDetach() {
        super.onDetach ( );
        //set the fragment manager to null
        setChildFragmentAccessible ( null);
    }*/


    /**
     * get the hosting activity
     *
     * @return
     */
    protected BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    /**
     * get the current activity
     *
     * @param <T>
     * @return
     */
    protected <T extends Activity> T getCurrentActivity() {
        return (T) getActivity();
    }

    public interface ConnectedCallback {
        void internetConnected(boolean connected);
    }
}