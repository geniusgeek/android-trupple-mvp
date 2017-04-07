/*
 * Copyright (C) 2015 Glowworm Software
 * Copyright (C) 2014 Nizamutdinov Adel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// based on https://gist.github.com/adelnizamutdinov/31c8f054d1af4588dc5c

package com.github.geniusgeek.trupple_mvp.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.github.geniusgeek.trupple_mvp.R;

/**
 * created by geniusgeek
 *
 * @author: based on https://gist.github.com/adelnizamutdinov/31c8f054d1af4588dc5c
 * Date: 6/1/16
 * Time: 7:13 AM
 */
public final class EmptyRecyclerView extends RecyclerView {
    @Nullable
    private View emptyView;
    @NonNull
    private final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            checkIfEmpty();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setViewFromXml(context, attrs);
    }

    /**
     * set the view from xml
     *
     * @param context
     * @param attrs
     */
    private void setViewFromXml(final Context context, final AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EmptyRecyclerView,
                0, 0
        );

        try {
            @LayoutRes int layout = ta.getResourceId(R.styleable.EmptyRecyclerView_emptyView, 0);
            //LayoutInflater.from ( getContext () ).
            emptyView = inflate(getContext(), layout, (ViewGroup) getParent());

        } finally {
            ta.recycle();
        }
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }

        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        checkIfEmpty();
    }

    @Override
    public void swapAdapter(Adapter adapter, boolean removeAndRecycleExistingViews) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }

        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        super.swapAdapter(adapter, removeAndRecycleExistingViews);
        checkIfEmpty();
    }

    /**
     * Indicates the view to be shown when the adapter for this object is empty
     *
     * @param emptyView
     */
    public void setEmptyView(@Nullable View emptyView) {
        this.emptyView = emptyView;
    }


    /**
     * Check adapter item count and toggle visibility of empty view if the adapter is empty
     */
    private void checkIfEmpty() {
        if (emptyView != null) {
            emptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
            setVisibility(emptyView.getVisibility() == VISIBLE ? GONE : VISIBLE);
        }
    }

}