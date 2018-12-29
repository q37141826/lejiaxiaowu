package com.qixiu.lejia.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Checkable;

import com.alorma.timeline.TimelineView;
import com.qixiu.lejia.R;

/**
 * Created by Long on 2018/5/17
 */
public class BitmapTimelineView extends TimelineView implements Checkable {

    private Bitmap mCheckedBitmap;
    private Bitmap mUncheckedBitmap;

    private boolean checked;

    public BitmapTimelineView(Context context) {
        this(context, null);
    }

    public BitmapTimelineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BitmapTimelineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCheckedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timeline_done);
        mUncheckedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.timeline_normal);
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked != checked) {
            this.checked = checked;
            invalidate();
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        checked = !checked;
        invalidate();
    }

    @Override
    protected void drawIndicator(Canvas canvas, Paint paintStart, float centerX, float centerY, float size) {
        final Bitmap bitmap = checked ? mCheckedBitmap : mUncheckedBitmap;
        canvas.drawBitmap(bitmap, centerX - bitmap.getWidth() / 2, centerY - bitmap.getHeight() / 2, null);
    }

    @Override
    protected void drawInternal(Canvas canvas, Paint paintInternal, float centerX, float centerY, float size) {
    }

    @Override
    protected void drawBitmap(Canvas canvas, float left, float top, int size) {
    }

}
