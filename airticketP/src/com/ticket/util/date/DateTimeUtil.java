package com.ticket.util.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
	/**
	 * 获取今天的日期和时间
	 * @param dateFormat
	 * @return
	 */
	public static String getToday(String dateFormat){
		return new SimpleDateFormat(dateFormat).format(new Date());
	}
	
	/**
	 * 获取基于今天的前X天或后X天的日期和时间
	 * @param dateFormat
	 * @param day	正整数代表后X天，负整数代表前X天，0代表当天
	 * @return
	 */
	public static String getXDay(String dateFormat,int day){
		Calendar calendar = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		calendar.add(Calendar.DAY_OF_MONTH, day);//取当前日期的前X天. 
		String yesterday = new SimpleDateFormat(dateFormat).format(calendar.getTime());
		return yesterday;
	}
	
	public static String getYestoday(String dateFormat){
		Calendar calendar = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		calendar.add(Calendar.DAY_OF_MONTH, -1);//取当前日期的前一天. 
		String yesterday = new SimpleDateFormat(dateFormat).format(calendar.getTime());
		return yesterday;
	}
	
	public static String getYestodayBefore(String dateFormat){
		Calendar calendar = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
		calendar.add(Calendar.DAY_OF_MONTH, -2);//取当前日期的前两天. 
		String yesterday = new SimpleDateFormat(dateFormat).format(calendar.getTime());
		return yesterday;
	}
	
}
