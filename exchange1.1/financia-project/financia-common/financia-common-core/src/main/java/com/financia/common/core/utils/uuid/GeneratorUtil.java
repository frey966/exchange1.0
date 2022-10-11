package com.financia.common.core.utils.uuid;


import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class GeneratorUtil {

	public static final DateFormat YYYY_MM_DD_MM_HH_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 得到from到to的随机数，包括to
     * @param from
     * @param to
     * @return
     */
    public static int getRandomNumber(int from, int to) {
		float a = from + (to - from) * (new Random().nextFloat());
		int b = (int) a;
		return ((a - b) > 0.5 ? 1 : 0) + b;
	}

	public static float getRandomFloat(int from, int to) {
		float a = new Random().nextFloat()* (to - from) + from;
		return a;
	}
    /**
     * 根据用户ID获取推荐码
     * @param uid
     * @return
     */
    public static String getPromotionCode(Long uid) {
    	String seed = "E5FCDG3HQA4B1NOPIJ2RSTUV67MWX89KLYZ";
    	long num = uid + 10000;
    	long mod = 0;
    	StringBuffer code = new StringBuffer();
    	while(num > 0) {
    		mod = num % 35;
    		num = (num - mod) / 35;
    		code.insert(0, seed.charAt(Integer.parseInt(String.valueOf(mod))));
    	}
    	while(code.length() < 4) {
    		code.insert(0, "0");
    	}
		return code.toString();
    }
    public static String getNonceString(int len){
    	String seed = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		StringBuffer tmp = new StringBuffer();
		for (int i = 0; i < len; i++) {
			tmp.append(seed.charAt(getRandomNumber(0,61)));
		}
		return tmp.toString();
    }

    public static String getUUID(){
    	UUID uuid = UUID.randomUUID();
    	return uuid.toString();
    }

    public static String getOrderId(String prefix){
    	String body = String.valueOf(System.currentTimeMillis());
    	return prefix + body + getRandomNumber(10,99);
    }

	public static Date timestampToDateStr(Long timestamp) throws ParseException {
		String sd = YYYY_MM_DD_MM_HH_SS.format(new Date(timestamp)); // 时间戳转换日期
		return YYYY_MM_DD_MM_HH_SS.parse(sd);
	}


	/**
	 * 获取指定期号
	 */
	public static String generateOptionQs(Date date,int qs){
		return new StringBuilder().append(DateFormatUtils.format(date, "yyyyMMdd")).append(String.format("%04d", qs)).toString();

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
	public static Date getTimeByMin(int min){

		Date dt = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(dt);
		c.add(Calendar.MINUTE,min);
		dt =c.getTime();

		return dt;
	}


}
