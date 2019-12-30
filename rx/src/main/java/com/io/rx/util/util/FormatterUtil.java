package com.io.rx.util.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/3/7.
 */

public class FormatterUtil {

    /**
     * @param value   毫秒
     * @param pattern 格式
     * @return
     */
    public static String formatTimePattern(long value, String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
//		formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String str = "";
        str = formatter.format(value);
        return str;
    }

    /**
     * 日期格式转字符串
     */
    public static String Date2String(Date date, String pattern) {
        if (date == null) return "";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        String str = format.format(date);
        return str;
    }


    public static Date StringToDate(String str, String pattern) {
        DateFormat format = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            // Fri Feb 24 00:00:00 CST 2012
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // 2012-02-24
//        date = java.sql.Date.valueOf(str);
        return date;
    }


    public static String stringToZhongYMD(String temp) {
        String[] t1 = temp.split("-");
        String[] t2 = t1[2].split(" ");
        return t1[0] + "年" + t1[1] + "月"
                + t2[0] + "日";
    }

    public static String stringToZhongMD(String temp) {
        String[] t1 = temp.split("-");
        return t1[0] + "月" + t1[1] + "日";
    }

    public static String getWeek2(int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c.set(year, month - 1, day);
//        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        return mWay;
    }

    public static String[] getYMD() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return new String[]{mYear, mMonth, mDay};
    }


    public static String getWeek3(int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        c.set(year, month - 1, day);
//        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String temp = "";
        if ("1".equals(mWay)) {
            temp = "7";
        } else if ("2".equals(mWay)) {
            temp = "1";
        } else if ("3".equals(mWay)) {
            temp = "2";
        } else if ("4".equals(mWay)) {
            temp = "3";
        } else if ("5".equals(mWay)) {
            temp = "4";
        } else if ("6".equals(mWay)) {
            temp = "5";
        } else if ("7".equals(mWay)) {
            temp = "6";
        }
        return temp;
    }


    public static String getY() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        return mYear;
    }

    public static String getYM() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        return mYear + "年" + mMonth + "月";
    }

    public static String getD() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        return mDay;
    }

    public static Integer getDays(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
//        calendar.get(Calendar.YEAR);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.MONTH, month - 1);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return maxDay;
    }

    public static String getFotMatterTime(String time) {
        String temp = "";
        String[] s = time.split(" ");
        String month = s[0].split("\\(")[0].split("月")[0];
        int length = s[0].split("\\(")[0].split("月")[1].length();
        String day = s[0].split("\\(")[0].split("月")[1].substring(0, length - 1);

        if (month.length() == 1) {
            month = "0" + month;
        }
        if (day.length() == 1) {
            day = "0" + day;
        }
        temp = month + "-" + day + " " + s[1];
        return temp;
    }


    /**
     * 服务器转本地时间
     */
    public static String dateTolocal(final String time) {
        if ((Integer.parseInt(time) + 1) % 8 == 0) {
            return "1";
        } else {
            return (Integer.parseInt(time) + 1) + "";
        }
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String formatTimeChina(long value) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        String str = "";
        str = formatter.format(value);
        String[] arr = str.split(":");
        if (arr != null && arr.length > 0) {
            String hour = arr[0];
            String minute = arr[1];
            String second = arr[2];
            if (Integer.parseInt(hour) == 0) {
                if (Integer.parseInt(minute) == 0) {
                    if (Integer.parseInt(second) == 0) {
                        return "";
                    } else {
                        return str = second + "秒";
                    }
                } else {
                    return str = minute + "分" + second + "秒";
                }
            } else {
                return str = hour + "小时" + minute + "分" + second + "秒";
            }
        }
        return "";

    }

}
