package com.qixiu.lejia.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumUtils {

    public static int getInterger(String str) {
        int num = 0;
        try {
            num = Integer.parseInt(str);

        } catch (Exception e) {
        }
        return num;
    }


    public static double getDouble(String str) {
        double num = 0;
        try {
            num = Double.parseDouble(str);
        } catch (Exception e) {
        }
        return num;
    }


//    public static String formatDouble3(double d) {
//        NumberFormat nf = NumberFormat.getNumberInstance();
//
//        // 保留两位小数
//        nf.setMaximumFractionDigits(2);
//
//
//        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
//        nf.setRoundingMode(RoundingMode.UP);
//
//        return nf.format(d);
//    }

    public static String formatDouble3(double d) {
        return formatDouble3(d, false);
    }

    public static String formatDouble3(double d, boolean isHard) {
        if (isHard) {
            return formatDoubleHard(d, 2);
        } else {
//        nf.setRoundingMode(RoundingMode.UP);//科学计数
            return formatDouble3(d, 2);
        }
    }


    public static String formatDouble3(double d, int lenght) {
        if(d==0){
            return "0.00";
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(lenght);
        return nf.format(d);
    }

    public static String formatDoubleHard(double d, int lenght) {
        if(d==0){
            return "0.00";
        }
        StringBuffer buffer = new StringBuffer("#.");
        for (int i = 0; i < lenght; i++) {
            buffer.append("0");
        }
        DecimalFormat df = new DecimalFormat(buffer.toString());
        return df.format(d);
    }

}
