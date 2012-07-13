package com.example.vitamio.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: william-work from mobicloud.com.cn
 * Date: 12-7-11
 * Time: 下午2:37
 */
public class StringUtils {
    private static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    private static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd hh:mm:ss";
    public static final String EMPTY = "";

    public static void main(String[] args) {
        System.out.println("The Date is: " + getDate() + "\n" + "The Date Time is: " + getDateTime());
    }

    /**
     * 格式化日期字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 格式化日期字符串
     *
     * @param date
     * @return
     */
    public static String formatDate(Date date) {
        return formatDate(date, DEFAULT_DATE_PATTERN);
    }

    /**
     * 格式化日期时间字符串
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDateTime(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    /**
     * 格式化日期时间字符串
     *
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        return formatDateTime(date, DEFAULT_DATETIME_PATTERN);
    }

    /**
     * 获取当前时期
     *
     * @return
     */
    public static String getDate() {
        return formatDate(new Date(), DEFAULT_DATE_PATTERN);
    }

    /**
     * 获取当前时期时间
     * @return
     */
    public static String getDateTime() {
        return formatDate(new Date(), DEFAULT_DATETIME_PATTERN);
    }


}
