/*
 * Copyright (c) 2016.  Lukaround Inc ;This program is free software: you can &#10;
 * redistribute it and/or modify;it under the terms of the Lukaround Inc Public License as &#10;
 * published by Lukaround Software Foundation, either version 3 of the License or (at your option) any later version ;&#10;This program is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; &#10;without even the implied warranty of;MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.&#10;  See www.lukaround.org/developer/licence
 */

package com.github.geniusgeek.trupple_mvp.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


/** Generic Implementation for <link>{@link AlertDialog}</> allowing customized views and listeners
 * created by geniusgeek
 * Date: 5/24/16
 * Time: 2:14 AM
 */
public final class AlertDialogFragment extends DialogFragment {

    private static final String PARAM_TITLE = "AlertDialogFragment.PARAM_TITLE";
    private static final String PARAM_MESSAGE = "AlertDialogFragment.PARAM_MESSAGE";
    private static final DialogInterface.OnClickListener NULL_LISTENER = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    };
    private static
    @LayoutRes
    int layoutRes;
    private DialogInterface.OnClickListener onOkClickListener;
    private DialogInterface.OnClickListener onCancelClickListener;
    private View view;

    /**
     * Factory method to create a <code>{@link AlertDialogFragment}</code>
     * @param title
     * @param message
     * @param onOkClickListener
     * @return
     */
    public static AlertDialogFragment newInstance(String title, String message, DialogInterface.OnClickListener onOkClickListener) {
        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        args.putString(PARAM_MESSAGE, message);
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.setArguments(args);
        alertDialogFragment.setOnOkClickListener(onOkClickListener == null ? NULL_LISTENER : onOkClickListener);
        alertDialogFragment.setOnCancelClickListener(NULL_LISTENER);
        return alertDialogFragment;
    }

    /**
     * return the view
     *
     * @return
     */
    public View getView() {
        return view;
    }

    public void setLayoutRes(Context context, int layoutRes) {
        AlertDialogFragment.layoutRes = layoutRes;
        view = LayoutInflater.from(context).inflate(layoutRes, null);

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String paramTitle = args.getString(PARAM_TITLE);
        String paramMessage = args.getString(PARAM_MESSAGE);
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                // Set Dialog Title
                .setTitle(paramTitle)
                // Set Dialog Message
                .setMessage(paramMessage)
                .setView(view)
                // Positive button
                .setPositiveButton("OK", onOkClickListener)

                // Negative Button
                .setNegativeButton("Cancel", onCancelClickListener)
                .setCancelable(false)
                .create();

    }

    public <T extends TextView> void setData(@IdRes int resId, CharSequence value) {
        final View dialogView = getView();
        ((T) dialogView.findViewById(resId)).setText(value);
    }

    public DialogInterface.OnClickListener getOnOkClickListener() {
        return onOkClickListener;
    }

    public void setOnOkClickListener(DialogInterface.OnClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    public DialogInterface.OnClickListener getOnCancelClickListener() {
        return onCancelClickListener;
    }

    public void setOnCancelClickListener(DialogInterface.OnClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }
}
