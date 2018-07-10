package com.zlzkj.app.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by ZJUXP on 2016-08-24.
 */
public class DateUtils {
    public static void main(String[] args) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("本周的周一: " + format.format(getThisWeekMonday(new Date())) + "  ===>>> " + getThisWeekMonday(new Date()));
        System.out.println("上周的周一: " + format.format(getLastWeekMonday(new Date())) + "  ===>>> " + getLastWeekMonday(new Date()));
        System.out.println("本月第一天: " + format.format(getThisMonthFirstDay()) + "  ===>>> " + getThisMonthFirstDay());
        System.out.println("一周前: " + format.format(getWeekBefore(new Date())) + "  ===>>> " + getWeekBefore(new Date()));
        System.out.println("一周后: " + format.format(getWeekAfter(new Date())) + "  ===>>> " + getWeekAfter(new Date()));
        System.out.println("上个月第一天: " + format.format(getLastMonthFirstDay()) + "  ===>>> " + getLastMonthFirstDay());
        System.out.println("上个月最后一天: " + format.format(getLastMonthFinalDay()) + "  ===>>> " + getLastMonthFinalDay());
        System.out.println("该天零点: " + format.format(getLastMidnight(new Date())) + "  ===>>> " + getLastMidnight(new Date()));
        System.out.println("现在: " + format.format(getNow()) + "  ===>>> " + getNow());
        System.out.println("该天前一天的这个点: " + format.format(getLastDay(new Date())) + "  ===>>> " + getLastDay(new Date()));
        System.out.println("今年第一天: " + format.format(getYearFirst()) + "  ===>>> " + getYearFirst());
        System.out.println("去年的今天: " + format.format(getWholeYearBefore(new Date())) + "  ===>>> " + getWholeYearBefore(new Date()));
    }

    public static String format(Timestamp timestamp) {
        String tsStr = "";
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            tsStr = sdf.format(timestamp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tsStr;
    }

    //本周的周一
    public static Date getThisWeekMonday(Date now) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(now);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    //一周前
    public static Date getWeekBefore(Date sunday) {
        return new Date(sunday.getTime() - 7 * 24 * 3600 * 1000l);
    }

    //一周后
    public static Date getWeekAfter(Date sunday) {
        return new Date(sunday.getTime() + 7 * 24 * 3600 * 1000l);
    }

    //本月第一天
    public static Date getThisMonthFirstDay() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    //上个月第一天
    public static Date getLastMonthFirstDay() {
        Calendar c = Calendar.getInstance();//获取当前日期
        c.add(Calendar.MONTH, -1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    //上个月最后一天
    public static Date getLastMonthFinalDay() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    //现在
    public static Date getNow() {
        Calendar c = new GregorianCalendar();
        c.setTime(new Date());
        c.add(c.DATE, 0);//把日期往后增加一天.整数往后推,负数往前移动
        return c.getTime();
    }

    //昨天的这个点
    public static Date getLastDay(Date now) {
        Calendar c = new GregorianCalendar();
        c.setTime(now);
        c.add(c.DATE, -1);//把日期往后增加一天.整数往后推,负数往前移动
        return c.getTime();
    }

    public static Date getLastMidnight(Date now) {
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        Date zero = c.getTime();
        return zero;
    }

    public static Date getLastWeekMonday(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(getThisWeekMonday(date));
        c.add(Calendar.DATE, -7);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    public static Date getYearFirst() {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.set(Calendar.YEAR, getNowYear());
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    //获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);
        return Integer.valueOf(gc.get(1));
    }


    //去年的今天这个点
    public static Date getWholeYearBefore(Date now) {
        Calendar c = new GregorianCalendar();
        c.setTime(now);
        c.add(c.YEAR, -1);//把日期往后增加一天.整数往后推,负数往前移动
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    //判断日期是否有效
    public static Date formatDate(String str,String formatstr) {
        if(formatstr == null) formatstr = "yyyy/MM/dd HH:mm";
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat(formatstr);//"yyyy/MM/dd HH:mm"
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
            format.setLenient(false);
            return format.parse(str);
        } catch (ParseException e) {
            // e.printStackTrace();
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            throw new RuntimeException("["+str+"]时间格式有误");
        }

    }

    //判断日期是否有效
    public static Date formatDate(String str,List<String> formatList) {

        for(String formatstr:formatList){
            // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
            SimpleDateFormat format = new SimpleDateFormat(formatstr);//"yyyy/MM/dd HH:mm"
            try {
                // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
                format.setLenient(false);
                return format.parse(str);
            } catch (ParseException e) {
                // e.printStackTrace();
                // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
                //throw new RuntimeException("["+str+"]时间格式有误");
            }
        }
        throw new RuntimeException("[<font color=#f00 >"+str+"</font>]时间格式有误");
    }
}
