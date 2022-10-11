package com.financia.exchange.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateUtil {
    public static final DateFormat YYYY_MM_DD_MM_HH_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final DateFormat HHMMSS = new SimpleDateFormat("HH:mm:ss");
    public static final DateFormat YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");

    public static final DateFormat YYYYMMDDMMHHSSSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static final DateFormat YYYY_MM_DD_MM_HH_SS_SSS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static final DateFormat YYYYMMDDMMHHSSSS = new SimpleDateFormat("yyyyMMddHHmmssSS");

    public static final DateFormat YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * 时间到分
     */
    public static final DateFormat YYYYMMDDHHMM = new SimpleDateFormat("yyyyMMddHHmm");


    public static final DateFormat YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

    public static String dateToString(Date date) {
        return YYYY_MM_DD_MM_HH_SS.format(date);
    }

    public static String dateToStringDate(Date date) {
        return YYYY_MM_DD.format(date);
    }

    public static String dateToStringLengthIs17(Date date) {
        return YYYYMMDDMMHHSSSSS.format(date);
    }

    public static String dateTo3S(Date date){
        return YYYY_MM_DD_MM_HH_SS_SSS.format(date);
    }

    /**
     * 开始时间 结束时间 是否合法  // 判断是否开始时间小于今天并且开始时间小于结束时间
     *
     * @param startDate
     * @param endDate
     */
    public static void validateDate(Date startDate, Date endDate) {
        Date currentDate = DateUtil.getCurrentDate();
        int compare = compare(startDate, currentDate);
        int compare2 = compare(startDate, endDate);
        Assert.isTrue(compare != -1, "startDate cannot be less than currentDate!");
        Assert.isTrue(compare2 != 1, "startDate must be less than endDate!");
    }

    public static void validateEndDate(Date endDate) {
        Date currentDate = DateUtil.getCurrentDate();
        int compare = compare(currentDate, endDate);
        Assert.isTrue(compare != 1, "currentDate must be less than endDate!");
    }

    /**
     * @param date1
     * @param date2
     * @return 1 大于 -1 小于 0 相等
     */
    public static int compare(Date date1, Date date2) {
        try {
            if (date1.getTime() > date2.getTime()) {
                return 1;
            } else if (date1.getTime() < date2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取当时日期时间串 格式 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateTime() {
        return YYYY_MM_DD_MM_HH_SS.format(new Date());
    }


    public static Date getStringToDate3S(String dateString){
        try {
            return YYYYMMDDMMHHSSSSS.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date getStringToDate2S(String dateString){
        try {
            return YYYYMMDDMMHHSSSS.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取当时日期串 格式 yyyy-MM-dd
     *
     * @return
     */
    public static String getDate() {
        return YYYY_MM_DD.format(new Date());
    }

    public static String getDateYMD() {
        return YYYYMMDD.format(new Date());
    }

    public static String getDateYMD(Date date) {
        return YYYYMMDD.format(date);
    }

    public static Date strToDate(String dateString) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date strToYYMMDDDate(String dateString) {
        Date date = null;
        try {
            date = YYYY_MM_DD.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static long diffDays(Date startDate, Date endDate) {
        long days = 0L;
        long start = startDate.getTime();
        long end = endDate.getTime();
        days = (end - start) / 86400000L;
        return days;
    }

    public static Date dateAddMonth(Date date, int month) {
        return add(date, 2, month);
    }

    public static Date dateAddDay(Date date, int day) {
        return add(date, 6, day);
    }

    public static Date dateAddYear(Date date, int year) {
        return add(date, 1, year);
    }

    public static String dateAddDay(String dateString, int day) {
        Date date = strToYYMMDDDate(dateString);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(6, day);
        return YYYY_MM_DD.format(calendar.getTime());
    }

    public static String dateAddDay(int day) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(6, day);
        return YYYY_MM_DD.format(calendar.getTime());
    }

    public static String dateAddMonth(int month) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(2, month);
        return YYYY_MM_DD.format(calendar.getTime());
    }

    public static String remainDateToString(Date startDate, Date endDate) {
        StringBuilder result = new StringBuilder();
        if (endDate == null) {
            return "过期";
        }
        long times = endDate.getTime() - startDate.getTime();
        if (times < -1L) {
            result.append("过期");
        } else {
            long temp = 86400000L;

            long d = times / temp;

            times %= temp;
            temp /= 24L;
            long m = times / temp;

            times %= temp;
            temp /= 60L;
            long s = times / temp;

            result.append(d);
            result.append("天");
            result.append(m);
            result.append("小时");
            result.append(s);
            result.append("分");
        }
        return result.toString();
    }

    private static Date add(Date date, int type, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, value);
        return calendar.getTime();
    }

    public static String getLinkUrl(boolean flag, String content, String id) {
        if (flag) {
            content = "<a href='finance.do?id=" + id + "'>" + content + "</a>";
        }
        return content;
    }

    public static long getTimeCur(String format, String date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.parse(sf.format(date)).getTime();
    }

    public static long getTimeCur(String format, Date date) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.parse(sf.format(date)).getTime();
    }

    public static Long getStrTime(String str) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.parse(str).getTime();
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static String getFormatTime(DateFormat format, Date date) throws ParseException {
        return format.format(date);
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public static long getTimeMillis() {
        return System.currentTimeMillis();
    }

    public static String getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case 1:
                return "周日";
            case 2:
                return "周一";
            case 3:
                return "周二";
            case 4:
                return "周三";
            case 5:
                return "周四";
            case 6:
                return "周五";
            case 7:
                return "周六";
            default:
                return "";
        }
    }

    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }

    /**
     * 得到当前时间与某个时间的差的分钟数
     *
     * @param date
     * @return
     */
    public static BigDecimal diffMinute(Date date) {
        return BigDecimalUtils.div(new BigDecimal(System.currentTimeMillis() - date.getTime()), new BigDecimal("60000"));
    }


    /**
     * 获取未来 第 past 天的日期
     *
     * @param past
     * @return
     */
    public static String getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    public static int getDatePart(Date date,int part) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(part);
    }

    public static Date getDate(Date date , int day){

        synchronized (YYYY_MM_DD){
            Calendar calendar = Calendar.getInstance() ;
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH,-day);
            date = calendar.getTime() ;
            try {
                return YYYY_MM_DD.parse(YYYY_MM_DD.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static String getDateRandom() {
        return DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+(int)((Math.random()*9+1)*10000);
    }

    /**
     * 判断当前时间是否是零点零分
     * @return
     */
    public static Boolean isLingDian() {
        return DateFormatUtils.format(new Date(), "HHmmss").equals("000000");
    }

    public static Date getDateNoTime(Date curDate,int amount) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(curDate);
        cal.add(Calendar.DAY_OF_MONTH, amount);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取分钟下期二元期权期号(
     * @return
     */
    public static String getMinIssueDateStr(Integer send) {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.SECOND, send);
        dt = c.getTime();
        return DateFormatUtils.format(dt, "yyyyMMddHHmm");
    }

//    public static void main(String[] args) {
//        System.out.println(getMinIssueDateStr(60));
//    }

    public static Map getYesterdayTime() {
        Long startTime = getBeginDayOfYesterday();
        Long endTime = getEndDayOfYesterDay();
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault()));
        String endTimeStr = ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.systemDefault()));
        Map map = new HashMap();
        map.put("startDate", startTimeStr);
        map.put("endDate", endTimeStr);
        return map;

    }

    /**
     * 2点
     * @return
     */
    public static String getTodayTwoClock() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 2);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return ftf.format(LocalDateTime.ofInstant(Instant.ofEpochMilli(cal.getTimeInMillis()), ZoneId.systemDefault()));
    }

    public static long getTodayTwoClockLong() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 2);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        DateTimeFormatter ftf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return cal.getTimeInMillis();
    }



    public static Long getBeginDayOfYesterday() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTimeInMillis();
    }

    public static Long getEndDayOfYesterDay() {
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTimeInMillis();
    }


    /**
     * .Description://根据字符日期返回星期几
     * .Author:麦克劳林
     * .@Date: 2018/12/29
     */
    public String getWeek(String dateTime){
        String week = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(dateTime);
            SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
            week = dateFm.format(date);
            week=week.replaceAll("星期","周");
        }catch (ParseException e){
            e.printStackTrace();
        }
        return week;
    }

    /**
     * 获取过去几天内的日期数组
     * @param intervals      intervals天内
     * @return              日期数组
     */
    public static List<String> getDays(int intervals) {
        intervals=intervals+1;
        List<String> pastDaysList = new ArrayList<>();
        for (int i = intervals -1; i > 0; i--) {
            pastDaysList.add(getPastDate(i));
        }
        return pastDaysList;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }




    /**
     * 获取过去几天内的日期数组
     * @param intervals      intervals天内
     * @return              日期数组
     */
    public static List<String> getLastDays(int intervals) {
        intervals=intervals+1;
        List<String> pastDaysList = new ArrayList<>();
        for (int i = intervals -1; i > 0; i--) {
            pastDaysList.add(getLastPastDate(i,intervals));
        }
        return pastDaysList;
    }

    public static String getLastPastDate(int past,int intervals) {
        intervals=intervals-1;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past-intervals);
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        return result;
    }

    public static String getLastPastDate(Long timeStamp) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String sd = sdf.format(new Date(timeStamp));      // 时间戳转换成时间
        return sd;
    }

    public static Date getTodayZero(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getYesterdayZero(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getSevenYesterdayZero(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 7);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }




    /**
     * 三十天之前
     * @return
     */
    public static Date getThYesterdayZero(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 30);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getNineYesterdayZero(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 90);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }


    public static Date addMinute(Date date, int amount){
        Calendar beforeTime = Calendar.getInstance();
        beforeTime.setTime(date);
        beforeTime.add(Calendar.MINUTE, amount);
        return beforeTime.getTime();
    }

    /**
     * 获取下一个十分钟时间戳
     * @param timeStamp
     * @return
     */
    public static Long getLatelyTenMinsTimes(Long timeStamp){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timeStamp));
        calendar.set(Calendar.MINUTE, (calendar.get(Calendar.MINUTE))/10*10);
        calendar.set(Calendar.SECOND,0);
        calendar.add(Calendar.MINUTE, 0);
        Date late = calendar.getTime();
        return late.getTime()/1000;
    }


    /**
     * 获取明天凌晨0点的时间戳 秒
     * @return
     */
    public static Long getTomorrowZero(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime().getTime()/1000;
    }


    public static void main(String[] args) {
        System.out.println(getLatelyTenMinsTimes(System.currentTimeMillis()));
//        System.out.println(getTomorrowZero());
    }
}