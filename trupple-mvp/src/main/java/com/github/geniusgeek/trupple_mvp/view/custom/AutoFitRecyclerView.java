package com.github.geniusgeek.trupple_mvp.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

public class AutoFitRecyclerView extends EmptyRecyclerView {
    private GridLayoutManager manager;
    private int columnWidth = -1;
    private int spanCount = -1;

    public AutoFitRecyclerView(Context context) {
        super(context);
        init(context, null);
    }

    public AutoFitRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AutoFitRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            int[] attrsArray = {
                    android.R.attr.columnWidth
            };
            TypedArray array = context.obtainStyledAttributes(attrs, attrsArray);
            columnWidth = array.getDimensionPixelSize(0, -1);
            array.recycle();
        }

        manager = new GridLayoutManager(getContext(), -1);
        setLayoutManager(manager);

        // int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        //  addItemDecoration(new GridSpacingItemDecoration(spanCount, 14, false));
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        if (columnWidth > 0) {
            spanCount = Math.max(1, getMeasuredWidth() / columnWidth);
            ;
            manager.setSpanCount(spanCount);

        }
    }
}