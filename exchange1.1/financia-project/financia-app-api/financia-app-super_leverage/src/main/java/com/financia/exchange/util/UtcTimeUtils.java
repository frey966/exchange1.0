package com.financia.exchange.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * UTC 时间工具类
 */
public class UtcTimeUtils {

    private static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm") ;

    /**

     * 得到utc时间，类型为字符串，格式为"yyyy-MM-dd HH:mm:ss"

     * 如果获取失败，返回null

     * @return

     */

    public static String getUtcTimeStr() {

        StringBuffer utcTimeBuffer = new StringBuffer();

        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();

        // 2、取得时间偏移量：
        int zoneoffset = cal.get(Calendar.ZONE_OFFSET);

        // 3、取得夏令时差：
        int dstoffset = cal.get(Calendar.DST_OFFSET);

        // 4、从本地时间里扣除这些差量，即可以取得utc时间：
        cal.add(Calendar.MILLISECOND, -(zoneoffset + dstoffset));

        int year = cal.get(Calendar.YEAR);

        int month = cal.get(Calendar.MONTH)+1;

        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);

        int minute = cal.get(Calendar.MINUTE);

        utcTimeBuffer.append(year).append("-").append(month).append("-").append(day);

        utcTimeBuffer.append(" ").append(hour).append(":").append(minute);

        try{
            Date utcTime = format.parse(utcTimeBuffer.toString());
            return format.format(utcTime);
        }catch(ParseException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 获取UTC时间的当前小时和分钟数
     * @return
     */
    public static Map<String,Integer> getUtcHourAndMinuteAndSecond() {

        StringBuffer utcTimeBuffer = new StringBuffer();

        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance();

        // 2、取得时间偏移量：
        int zoneoffset = cal.get(Calendar.ZONE_OFFSET);

        // 3、取得夏令时差：
        int dstoffset = cal.get(Calendar.DST_OFFSET);

        // 4、从本地时间里扣除这些差量，即可以取得utc时间：
        cal.add(Calendar.MILLISECOND, -(zoneoffset + dstoffset));

        int year = cal.get(Calendar.YEAR);

        int month = cal.get(Calendar.MONTH)+1;

        int day = cal.get(Calendar.DAY_OF_MONTH);

        int hour = cal.get(Calendar.HOUR_OF_DAY);

        int minute = cal.get(Calendar.MINUTE);

        int second = cal.get(Calendar.SECOND);

        Map<String , Integer> result = new HashMap<>();
        result.put("hour", hour);
        result.put("minute", minute);
        result.put("second",second);
        return result;
    }

}
