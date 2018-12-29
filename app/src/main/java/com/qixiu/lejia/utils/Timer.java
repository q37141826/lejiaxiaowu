package com.qixiu.lejia.utils;

import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

public class Timer extends CountDownTimer {
    TextView textView;
    private long totalTime;
    private long intervalTime;
    private int unableBack,enableBack;

    public int getUnableBack() {
        return unableBack;
    }

    public void setUnableBack(int unableBack) {
        this.unableBack = unableBack;
    }

    public int getEnableBack() {
        return enableBack;
    }

    public void setEnableBack(int enableBack) {
        this.enableBack = enableBack;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }


    public Timer( long totalTime,long intervalTime) {
        super(totalTime, intervalTime);
    }
    public Timer() {
        super(10 * 1000, 1000);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        Log.i("test", "??");
        if (textView != null) {
            textView.setText(millisUntilFinished / 1000 + "s"+"  下一步");
            textView.setEnabled(false);
            if(unableBack!=0){
                textView.setBackgroundResource(unableBack);
            }
        }
    }

    @Override
    public void onFinish() {
        if (textView != null) {
            textView.setText("下一步");
            textView.setEnabled(true);
            if(enableBack!=0){
                textView.setBackgroundResource(enableBack);
            }
        }
    }

}