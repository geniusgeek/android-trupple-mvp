package com.github.geniusgeek.trupple_mvp.presenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.geniusgeek.trupple_mvp.MVP;

import java.lang.ref.WeakReference;


/**
 * This class provides a framework for mediating access to the Model and View
 * layer in the Model-View-Presenter pattern.
 * <b>You can use <code> {@link com.github.geniusgeek.trupple_mvp.MVP.GenericPresenter}</code>
 * when defining Model and a View</b>
 * NOte: Only use this when you want to define ModelOps(strategy for a model Object) and a
 * @param <M> ModelOps an Op strategy for the model
 * @param <V> View defines the View which is usually an Activity or any subclass of
 *           <code>{@link com.github.geniusgeek.trupple_mvp.MVP.GenericViewOps}</code>
 */
public abstract class GenericPresenter<M extends MVP.GenericModelOps
        , V extends MVP.GenericViewOps> implements PresenterOps<V> {
    /**
     * Debugging tag used by the Android logger.
     */
    protected final String TAG = getClass ( ).getSimpleName ( );
    private WeakReference<V> mViewWeakReference;

    /**
     * Instance of the operations ("Ops") type.
     */
    private M mModelOps;

    /**
     * the view instance type
     */
    private V mView;


    /**
     * Lifecycle hook method that's called when the GenericPresenteris
     * created.
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void onCreate(M model, V view) {
/*
        Type[] types= ((ParameterizedType) getClass ( )
                .getGenericSuperclass ( )).getActualTypeArguments ( );
        Class<ParameterizedType> mModelClass = (Class<ParameterizedType>) types[0];

        Class<ParameterizedType>  mViewClass = (Class<ParameterizedType>) types[1];*/
        try {
            // Initialize the GenericPresenter fields.
            initialize ( model, view );
        } catch (InstantiationException
                | IllegalAccessException e) {
            Log.d ( TAG,
                    "handleConfiguration "
                            + e );
            // Propagate this as a runtime exception.
            throw new RuntimeException ( e );
        }
    }

    /**
     * Initialize the GenericPresenter fields.
     *
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private void initialize(M modelOps, V view)
            throws InstantiationException, IllegalAccessException {
        // Create the ModelType object.
        mModelOps = modelOps;
        mView = view;
        updateActivityView(mView);
        // Perform the first initialization.
        mModelOps.onCreate ( this);
    }

    public V getView() {
        if (mViewWeakReference.get() == null)
            throw new NullPointerException("the view is null");
        return mViewWeakReference.get();
     }

    /**
     * Return the initialized ProvidedModelOps instance for use by the
     * application.
     */
    @SuppressWarnings("unchecked")
    protected M getModelOps() {
        return (M) mModelOps;
    }


    /**
     *
     * @param id
     * @param adapter
     * @param layoutManager
     * @return
     */
    protected RecyclerView recyclerViewInnitializer(int id, RecyclerView.Adapter<?> adapter, RecyclerView.LayoutManager layoutManager) {
        RecyclerView recyclerView = (RecyclerView) ((Activity)getView()).findViewById ( id );
        recyclerView.setLayoutManager ( layoutManager );
        //ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration (context, R.dimen.item_offset);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator ( );
        itemAnimator.setAddDuration ( 1000 );
        itemAnimator.setRemoveDuration ( 1000 );
        //recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setAdapter ( adapter );
        recyclerView.setItemAnimator ( itemAnimator );
        return recyclerView;
    }

    @Override
    public void onCreate(final V view) {
        updateActivityView(view);
    }

    @Override
    public void onConfigurationChange(final V view) {
        updateActivityView(view);
    }

    @Override
    public void onDestroy(final boolean isChangingConfigurations) {

    }


    @Override
    public void goToNextActivity(Class<? extends Activity> activityClass) {
        getView().goToNextActivity(activityClass, null, null);
    }

    private void updateActivityView(V view) {
        this.mViewWeakReference = new WeakReference<>(view);
    }
}

