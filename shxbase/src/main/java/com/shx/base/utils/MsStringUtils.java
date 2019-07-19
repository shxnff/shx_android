package com.shx.base.utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MsStringUtils {

    public static int str2int(String text) {
        int myNum = 0;

        try {
            myNum = Integer.parseInt(text);
        } catch (Exception nfe) {
            nfe.getStackTrace();
        }
        return myNum;
    }

    public static long str2long(String text) {
        long myNum = 0;

        try {
            myNum = Long.parseLong(text);
        } catch (Exception nfe) {
            nfe.getStackTrace();
        }
        return myNum;
    }

    public static double str2double(String text) {
        double myNum = 0;

        try {
            myNum = Double.parseDouble(text);
        } catch (Exception nfe) {
            nfe.getStackTrace();
        }
        return myNum;
    }

    public static float str2flt(String text) {
        float myNum = 0;

        try {
            myNum = Float.parseFloat(text);
        } catch (Exception nfe) {
            nfe.getStackTrace();
        }
        return myNum;
    }

    public static String formatDouble(double value) {
        BigDecimal b2 = new BigDecimal(value);
        b2 = b2.setScale(2, BigDecimal.ROUND_DOWN);

        DecimalFormat fnum = new DecimalFormat("##0.00");

        return fnum.format(b2.doubleValue());
    }

    public static String formatOne(double value) {
        BigDecimal b2 = new BigDecimal(value);
        b2 = b2.setScale(1, BigDecimal.ROUND_DOWN);
        DecimalFormat fnum = new DecimalFormat("##0.0");
        return fnum.format(b2.doubleValue());
    }

    public static String timeToString(String time) {
        long time2 = str2long(time);
        Date dat = new Date(time2 * 1000);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd");
        return format.format(gc.getTime());
    }

    public static String timeToString4(String time) {
        long time2 = str2long(time);
        Date dat = new Date(time2);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd");
        return format.format(gc.getTime());
    }

    public static String timeToString5(String time) {
        long time2 = str2long(time);
        Date dat = new Date(time2);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat(
                "MM-dd HH:mm");
        return format.format(gc.getTime());
    }

    public static String timeToString2(String time) {
        long time2 = str2long(time);
        //Date dat = new Date(time2*1000);
        Date dat = new Date(time2);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        return format.format(gc.getTime());
    }

    public static String timeToString3(String time) {
        long time2 = str2long(time);
        Date dat = new Date(time2);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dat);
        SimpleDateFormat format = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm");
        return format.format(gc.getTime());
    }

    public static String byteFormat(long appSize) {

        //long appSize = str2long(appSizeStr);
        String result = "";
        float lastSize = (float) (((int) (appSize / 1024.0 * 100)) / 100.0);
        if (appSize >= 1024) {

            lastSize = (float) (((int) (lastSize / 1024.0 * 100)) / 100.0);

            result = lastSize + "MB";
        } else {
            result = appSize + "KB";
        }

        return result;
    }

    /***
     * 判断 String 是否正数int
     *
     * @param input
     * @return
     */
    public static boolean isInteger(String input) {
        Matcher mer = Pattern.compile("^[0-9]+$").matcher(input);
        return mer.find();
    }

    /***
     * 判断 String 是否是 int正负都可判断
     *
     * @param input
     * @return
     */
    public static boolean isIntegerAll(String input) {
        Matcher mer = Pattern.compile("^[+-]?[0-9]+$").matcher(input);
        return mer.find();
    }

    //保留两位小数
    public static String setmoneyString(double d2) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d2);
    }

    public static String setmoneyOneString(double d2) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(d2);
    }

    public static boolean isEmpty(CharSequence str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 加法运算
     */
    public static String addDouble(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        BigDecimal p3 = p1.add(p2);
        return String.valueOf(p3);
    }

    /**
     * 减法运算
     */
    public static String subDouble(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        BigDecimal p3 = p1.subtract(p2);
        return String.valueOf(p3);
    }

    /**
     * 乘法运算
     */
    public static String mul(double m1, double m2) {
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        BigDecimal p3 = p1.multiply(p2);
        return String.valueOf(p3);
    }


    /**
     * 除法运算
     */
    public static String div(double m1, double m2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("Parameter error");
        }
        BigDecimal p1 = new BigDecimal(Double.toString(m1));
        BigDecimal p2 = new BigDecimal(Double.toString(m2));
        BigDecimal p3 = p1.divide(p2, scale, BigDecimal.ROUND_DOWN);
        return String.valueOf(p3);
    }

    public static String moneyFormat(int data) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(data);
    }

    /**
     * 数字金额大写转换
     * 要用到正则表达式
     */
    public static String digitUppercase(String money){
        if (money.length() == 0)
            return "";
        if (Double.parseDouble(money) == 0)
            return "零圆整";

        String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{"圆", "万", "亿"}, {"", "拾", "佰", "仟"}};

        String [] numArray = money.split("\\.");

        String amountInWords = "";

        double n = Double.parseDouble(money);
        int integerPart = (int)Math.floor(n);

        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String temp ="";
            int tempNum = integerPart%10000;
            if (tempNum != 0 || i == 0) {
                for (int j = 0; j < unit[1].length && integerPart > 0; j++) {
                    temp = digit[integerPart%10]+unit[1][j] + temp;
                    integerPart = integerPart/10;
                }
                /*
                 *正则替换，加上单位
                 *把零佰零仟这种去掉，再去掉多余的零
                 */
                amountInWords = temp.replaceAll("(零.)+", "零").replaceAll("^$", "零").replaceAll("(零零)+", "零") + unit[0][i] + amountInWords;
            } else {
                integerPart /= 10000;
                temp = "零";
                amountInWords = temp + amountInWords;
            }
            amountInWords = amountInWords.replace("零" + unit[0][i], unit[0][i] + "零");
            if (i > 0) amountInWords = amountInWords.replace("零" + unit[0][i-1], unit[0][i-1] + "零");
        }

        String fWordsStr = "";
        if (numArray.length > 1) {
            String fStr = numArray[1];
            int iLen = fraction.length < fStr.length() ? fraction.length : fStr.length();
            for (int i = 0; i < iLen; i++) {
                int numInt = Integer.parseInt(fStr.substring(i, i+ 1));
                if (numInt == 0) continue;
                if (amountInWords.length() > 0 && fWordsStr.length() == 0 && i > 0)
                    fWordsStr = "零";
                fWordsStr += (digit[numInt] + fraction[i]);
            }
        }
        if (fWordsStr.length() == 0) fWordsStr = "整";
        amountInWords = amountInWords + fWordsStr;
        amountInWords = amountInWords.replaceAll("(零零)+", "零").replace("零整", "整");

        return  amountInWords;
    }
}
