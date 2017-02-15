/*
 * Copyright (c) 2016.  Lukaround Inc ;This program is free software: you can &#10;
 * redistribute it and/or modify;it under the terms of the Lukaround Inc Public License as &#10;
 * published by Lukaround Software Foundation, either version 3 of the License or (at your option) any later version ;&#10;This program is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; &#10;without even the implied warranty of;MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.&#10;  See www.lukaround.org/developer/licence
 */

package com.github.geniusgeek.trupple_mvp.common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * @architect: Samuel Ekpe(Team Lead)
 * @author: Genius
 * @reviewers: Lukaround Android Team
 * Date: 5/20/16
 * Time: 3:25 AM
 */
public final class ProgressDialogFragment extends DialogFragment {
    public static final String TAG = ProgressDialogFragment.class.getSimpleName();
    private OnRequestCancel onRequestCancel;

    /**
     * factory meethod for creating new instance of the progress dialog
     *
     * @return
     */
    public static Fragment newInstance(final String postTitle, String postMessage) {
        Fragment fragment = new ProgressDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("category_title", postTitle);
        bundle.putString("message", postMessage);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
        dialog.setTitle(bundle.getString("category_title"));
        dialog.setMessage(bundle.getString("message"));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return dialog;
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        Log.d(TAG, "ProgressDialogFragment.onCancel");

        super.onCancel(dialog);

        Fragment fragment = getTargetFragment();

        // stop all requests on cancel
        if (fragment != null && fragment instanceof OnRequestCancel) {
            ((OnRequestCancel) fragment).onCancel();

        } else {
            dismiss();
        }
    }

    public OnRequestCancel getOnRequestCancel() {
        return onRequestCancel;
    }

    public void setOnRequestCancel(OnRequestCancel onRequestCancel) {
        this.onRequestCancel = onRequestCancel;
    }

    /**
     * interface to implement when target class have implementation when dialog is cancelled
     */
    public interface OnRequestCancel {
        void onCancel();
    }
}