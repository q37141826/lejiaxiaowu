package com.qixiu.lejia.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.qixiu.lejia.R;

import java.util.ArrayList;
import java.util.List;


public class StepView extends View {

    private List<String> steps = new ArrayList<>();

    private int mCircleRadius;
    private int mSelectedCircleColor;
    private int mUnselectedCircleColor;
    private int mNumberSize;
    private int mSelectedNumberColor;
    private int mUnselectedNumberColor;
    private int mTextSize;
    private int mSelectedTextColor;
    private int mUnselectedTextColor;
    private int mLineSize;
    private int mSelectedLineColor;
    private int mUnselectedLineColor;


    private int mTextMargin;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int[] circlesX;
    private int[] startLinesX;
    private int[] endLinesX;
    private int circlesY;
    private int textY;

    //当前step
    private int currentStep = 0;

    private Rect bounds = new Rect();


    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint.setTextAlign(Paint.Align.CENTER);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StepView, 0, R.style.StepView);

        currentStep = a.getInteger(R.styleable.StepView_sv_current_step, 0);

        mCircleRadius = a.getDimensionPixelSize(R.styleable.StepView_sv_circle_radius, 0);
        mSelectedCircleColor = a.getColor(R.styleable.StepView_sv_selected_circle_color, Color.BLACK);
        mUnselectedCircleColor = a.getColor(R.styleable.StepView_sv_unselected_circle_color, Color.BLACK);

        mNumberSize = a.getDimensionPixelSize(R.styleable.StepView_sv_number_size, 0);
        mSelectedNumberColor = a.getColor(R.styleable.StepView_sv_selected_number_color, Color.BLACK);
        mUnselectedNumberColor = a.getColor(R.styleable.StepView_sv_unselected_number_color, Color.BLACK);

        mTextSize = a.getDimensionPixelSize(R.styleable.StepView_sv_text_size, 0);
        mSelectedTextColor = a.getColor(R.styleable.StepView_sv_selected_text_color, Color.BLACK);
        mUnselectedTextColor = a.getColor(R.styleable.StepView_sv_unselected_text_color, Color.BLACK);

        mLineSize = a.getDimensionPixelSize(R.styleable.StepView_sv_line_size, 0);
        mSelectedLineColor = a.getColor(R.styleable.StepView_sv_selected_line_color, Color.BLACK);
        mUnselectedLineColor = a.getColor(R.styleable.StepView_sv_unselected_line_color, Color.BLACK);

        mTextMargin = a.getDimensionPixelSize(R.styleable.StepView_sv_text_margin, 0);

        CharSequence[] descriptions = a.getTextArray(R.styleable.StepView_sv_steps);
        if (descriptions != null) {
            for (CharSequence item : descriptions) {
                steps.add(item.toString());
            }
        }

        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        measureAttributes();
    }

    private int measureWidth(int widthMeasureSpec) {
        return MeasureSpec.getSize(widthMeasureSpec);
    }

    private int measureHeight(int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (heightMode == MeasureSpec.AT_MOST) {
            height = getPaddingTop()
                    + getPaddingBottom()
                    + mCircleRadius * 2
                    + mTextMargin;
            if (!steps.isEmpty()) {
                height += measureStepsHeight();
            }
        }
        return height;
    }

    private int measureStepsHeight() {
        paint.setTextSize(mTextSize);
        int fontHeight = fontHeight();
        int max = 0;
        for (int i = 0; i < steps.size(); i++) {
            String text = steps.get(i);
            String[] split = text.split("\\n");
            if (split.length == 1) {
                max = Math.max(fontHeight, max);
            } else {
                max = Math.max(fontHeight * split.length, max);
            }
        }
        return max;
    }

    private int fontHeight() {
        return (int) Math.ceil(paint.descent() - paint.ascent());
    }

    private void measureAttributes() {
        circlesY = (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        circlesX = getCirclePositions();
        paint.setTextSize(mTextSize);
        textY = circlesY + mCircleRadius + mTextMargin + fontHeight() / 2;
        measureLines();
    }

    private int[] getCirclePositions() {
        return getCirclePositionsWithText(measureSteps());
    }

    private int[] measureSteps() {
        int[] result = new int[steps.size()];
        for (int i = 0; i < steps.size(); i++) {
            result[i] = (int) paint.measureText(steps.get(i)) + /* correct possible conversion error */ 1;
        }
        return result;
    }

    private int[] getCirclePositionsWithText(int[] textWidth) {
        int[] result = new int[textWidth.length];
        if (result.length == 0) {
            return result;
        }
        result[0] = getPaddingLeft() + Math.max(textWidth[0] / 2, mCircleRadius);
        if (result.length == 1) {
            return result;
        }
        result[textWidth.length - 1] = getMeasuredWidth() - getPaddingRight() -
                Math.max(textWidth[textWidth.length - 1] / 2, mCircleRadius);
        if (result.length < 3) {
            return result;
        }
        float spaceLeft = result[textWidth.length - 1] - result[0];
        int margin = (int) (spaceLeft / (textWidth.length - 1));
        for (int i = 1; i < textWidth.length - 1; i++) {
            result[i] = result[i - 1] + margin;
        }
        return result;
    }

    private void measureLines() {
        startLinesX = new int[getStepCount() - 1];
        endLinesX = new int[getStepCount() - 1];
        int padding = mCircleRadius - 1;

        for (int i = 1; i < getStepCount(); i++) {
            startLinesX[i - 1] = circlesX[i - 1] + padding;
            endLinesX[i - 1] = circlesX[i] - padding;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int stepSize = getStepCount();

        if (stepSize == 0) {
            return;
        }

        for (int i = 0; i < stepSize; i++) {
            drawStep(canvas, i, circlesX[i], circlesY);
        }

        for (int i = 0; i < startLinesX.length; i++) {
            if (i < currentStep) {
                drawLine(canvas, startLinesX[i], endLinesX[i], circlesY, true);
            } else {
                drawLine(canvas, startLinesX[i], endLinesX[i], circlesY, false);
            }
        }

    }

    private void drawStep(Canvas canvas, int step, int circleCenterX, int circleCenterY) {
        final String text = steps.get(step);
//        final boolean isSelected = step == currentStep;
//        final boolean isDone = done ? step <= currentStep : step < currentStep;
        final String number = String.valueOf(step + 1);
        int radius = mCircleRadius;

        if (step <= currentStep) {
            paint.setColor(mSelectedCircleColor);
        } else {
            paint.setColor(mUnselectedCircleColor);
        }
        canvas.drawCircle(circleCenterX, circleCenterY, radius, paint);


        if (step <= currentStep) {
            paint.setColor(mSelectedTextColor);
        } else {
            paint.setColor(mUnselectedTextColor);
        }
        drawText(canvas, text, circleCenterX, textY, paint);


        if (step <= currentStep) {
            paint.setColor(mSelectedNumberColor);
        } else {
            paint.setColor(mUnselectedNumberColor);
        }
        drawNumber(canvas, number, circleCenterX, paint);

    }

    private void drawNumber(Canvas canvas, String number, int circleCenterX, Paint paint) {
        paint.getTextBounds(number, 0, number.length(), bounds);
        float y = circlesY + bounds.height() / 2f - bounds.bottom;
        canvas.drawText(number, circleCenterX, y, paint);
    }

    private void drawText(Canvas canvas, String text, int x, int y, Paint paint) {
        if (text.isEmpty()) {
            return;
        }
        String[] split = text.split("\\n");
        if (split.length == 1) {
            canvas.drawText(text, x, y, paint);
        } else {
            for (int i = 0; i < split.length; i++) {
                canvas.drawText(split[i], x, y + i * fontHeight(), paint);
            }
        }
    }

    private void drawLine(Canvas canvas, int startX, int endX, int centerY, boolean highlight) {
        if (highlight) {
            paint.setColor(mSelectedLineColor);
            paint.setStrokeWidth(mLineSize);
            canvas.drawLine(startX, centerY, endX, centerY, paint);
        } else {
            paint.setColor(mUnselectedLineColor);
            paint.setStrokeWidth(mLineSize);
            canvas.drawLine(startX, centerY, endX, centerY, paint);
        }
    }

    public int getStepCount() {
        return steps.size();
    }

    public void go(int step) {
        if (step >= 0 && step < getStepCount()) {
            if (currentStep != step) {
                currentStep = step;
                invalidate();
            }
        }
    }

    public void next() {
        if (currentStep < getStepCount()) {
            currentStep += 1;
            invalidate();
        }
    }

    public void back() {
        if (currentStep > 0) {
            currentStep -= 1;
            invalidate();
        }
    }


}
