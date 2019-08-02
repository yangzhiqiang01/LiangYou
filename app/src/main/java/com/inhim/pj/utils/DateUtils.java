package com.inhim.pj.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	@SuppressLint("SimpleDateFormat") public static String getThisweekthedate(){
		Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		//获取本周一的日期
		String sunday=df.format(cal.getTime());
		//System.out.println(df.format(cal.getTime()));
		//这种输出的是上个星期周日的日期，因为老外那边把周日当成第一天
		cal.add(Calendar.WEEK_OF_YEAR, 1);
		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		//增加一个星期，才是我们中国人理解的本周日的日期
		//cal.add(Calendar.WEEK_OF_YEAR, 1);
		//System.out.println(df.format(cal.getTime()));
		String saturday=df.format(cal.getTime());
		String date=sunday+"-"+saturday;
		return date;
	}


	private static SimpleDateFormat formatter;
	@SuppressLint("SimpleDateFormat")
	public static String getThisdate(){
		formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		String str = formatter.format(curDate);
		return str;

	}

	@SuppressLint("SimpleDateFormat")
	public static String getThisYear(){
		formatter = new SimpleDateFormat ("yyyy");
		Date curDate = new Date(System.currentTimeMillis());//获取当前时间
		String str = formatter.format(curDate);
		return str;

	}

	public static Date strToDate(String style, String date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

    /**
     * 将字符串格式的时间转为毫秒值
     *
     * @param dateTime
     *            字符串时间（格式：2015 -12- 31 23:59:53）
     * @return
     */
    public static Long getLongFromString(String dateTime) {
        try {
            /**
             * 将字符串数据转化为毫秒数
             */
            StringBuffer buffer = new StringBuffer();
            char[] charArray = dateTime .toCharArray();
            for (int i = 0; i < charArray .length ; i ++) {
                if (Character.isDigit( charArray[i ])) {
                    buffer.append( charArray[i ]);
                }
            }
            dateTime = buffer.toString();
            buffer = null;
            Calendar c = Calendar. getInstance();
            c.setTime( new SimpleDateFormat("yyyyMMddHHmmss" ).parse(dateTime ));
            return c .getTimeInMillis();
            /**
             * 将毫秒数转化为时间
             */
            // String sstime = "1339033320000";
            // Date date = new Date(sstime);
            //
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd
            // HH:mm:ss");
            //
            // System.out.println("毫秒数转化后的时间为：" + sdf.format(date));
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return null ;
    }


	public static String dateToStr(String style, Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		return formatter.format(date);
	}

	public static String clanderTodatetime(Calendar calendar, String style) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		return formatter.format(calendar.getTime());
	}

	public static String DateTotime(long date, String style) {
		SimpleDateFormat formatter = new SimpleDateFormat(style);
		return formatter.format(date);
	}

}
