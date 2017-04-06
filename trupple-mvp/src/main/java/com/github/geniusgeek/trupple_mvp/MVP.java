package com.github.geniusgeek.trupple_mvp;

import android.view.View;

import com.github.geniusgeek.trupple_mvp.exceptions.ErrorResponse;
import com.github.geniusgeek.trupple_mvp.model.ModelOps;
import com.github.geniusgeek.trupple_mvp.presenter.PresenterOps;
import com.github.geniusgeek.trupple_mvp.view.ContextView;

import java.util.List;

/**
 * Created by Genius on 16/02/2016.
 * <p>
 * This class Defines the interfaces for the   Lukaround Client application
 * that are required and provided by the layers in the
 * Model-View-Presenter (MVP) pattern.  This design ensures loose
 * coupling between the layers in the app's MVP-based architecture.
 * <p>
 * The aim of this class is to insulate you from the underlying implementation and make it clean for you to work and handle
 * configuration change and other issues.
 */
public interface MVP {
    /**
     * generic model that any model class can implement to be able to get to presenter back and forth
     */
    interface GenericModelOps<M> extends ModelOps {
        String TAG = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();

        M getModel();

        /**
         * this defines the callback response to be carrried our on the presenter layer
         */
        interface ResponseOPsCallback<M> {
            void onSuccess(List<M> models);

            void onSuccess(M model);

            void onFailure(ErrorResponse cause);
        }
    }

    /**
     * this defines the Generic Operations to be performed on View Layer on the MVP pattern
     */
    interface GenericViewOps<T extends GenericPresenter> extends ContextView {

        String TAG = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();

        <T> T getPresenter();

        void showProgress(String title, String text);

        void hideProgress();


    }

    /**
     * this defines the Generic Operations to be performed by any presenter on Lukaround app based on the MVP pattern
     *
     * @param <M> any required model class
     * @param <V> the required view
     */
    interface GenericPresenter<M, V extends GenericViewOps> extends PresenterOps<V>, View.OnClickListener {
        // void setModel(M model);
        String TAG = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();

        V getView();

        M getModel();

    }
}
