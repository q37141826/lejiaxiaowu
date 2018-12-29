package com.qixiu.lejia.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by Long on 2017/11/22
 */
public abstract class BaseVerticalDivider extends RecyclerView.ItemDecoration {

    private static final String TAG = "BaseVerticalDivider";

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;
    private final Rect mBounds = new Rect();

    public BaseVerticalDivider(Context context) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        if (mDivider == null) {
            Log.w(TAG, "@android:attr/listDivider was not set in the theme used for this "
                    + "DividerItemDecoration. Please set that attribute all call setDrawable()");
        }
        a.recycle();
    }

    public void setDrawable(@NonNull Drawable drawable) {
        mDivider = drawable;
    }

    public Drawable getDivider() {
        return mDivider;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        canvas.save();
        final int left;
        final int right;
        //noinspection AndroidLintNewApi - NewApi lint fails to handle overrides.
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(child);
            final int viewType = viewHolder.getItemViewType();
            final int position = viewHolder.getAdapterPosition();
            if (isNeedDraw(viewType, position)) {
                parent.getDecoratedBoundsWithMargins(child, mBounds);
                final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
                final int top = bottom - mDivider.getIntrinsicHeight();
                //设置分割线的偏移
                setOffsets(viewType, position, left, right, bottom, top);
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.ViewHolder viewHolder = parent.getChildViewHolder(view);
        if (isNeedDraw(viewHolder.getItemViewType(), viewHolder.getAdapterPosition())) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        }
    }

    protected abstract boolean isNeedDraw(int itemViewType, int position);

    protected void setOffsets(int viewType, int position, int left, int right, int bottom, int top) {
        mDivider.setBounds(left, top, right, bottom);
    }


}
