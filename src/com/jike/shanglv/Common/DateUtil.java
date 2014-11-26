package com.jike.shanglv.Common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.net.ParseException;

@SuppressLint("SimpleDateFormat")
public class DateUtil {
	/*
	 * �Ƚ���������ʱ��Ĵ�С
	 */
	public static boolean isDateBefore(String date1, String date2) {
		try {
			DateFormat df = DateFormat.getDateTimeInstance();
			return df.parse(date1).before(df.parse(date2));
		} catch (Exception e) {
			return false;
		}
	}

	public static String GetTodayDate() {
		String temp_str = "";
		Date dt = new Date();
		// "yyyy-MM-dd HH:mm:ss aa" ����aa��ʾ�����硱�����硱 HH��ʾ24Сʱ�� �������hh��ʾ12Сʱ��
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		temp_str = sdf.format(dt);
		return temp_str;
	}

	public static String GetNow() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// �������ڸ�ʽ
		return df.format(new Date());
	}

	/**
	 * ��ȡ����֮��i������ڣ�����:GetDateAfterToday(1)
	 * */
	public static String GetDateAfterToday(int i) {
		Date date = new Date();// ȡʱ��
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, i);// ��������������һ��.����������,������ǰ�ƶ�
		date = calendar.getTime(); // ���ʱ���������������һ��Ľ��
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		return dateString;
	}

	public static Boolean isOverThirtyMinite(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return true;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// ���趨��ʱ��
		long calmill = cal.getTimeInMillis(); // �Ƚ�ʱ������ɺ�����
		// ϵͳʱ��ĺ�����
		long sysmill = System.currentTimeMillis();
		System.out.println(sysmill);
		if (sysmill > calmill + 1800000) // 1800000������30����
		{
			return true;// ����֧��
		}
		return false;
	}

	/**
	 * ���ָ�����ڵ�ǰһ��
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws java.text.ParseException
	 * @throws Exception
	 */
	public static String getSpecifiedDayBefore(String specifiedDay)
			throws java.text.ParseException {// ������new
												// Date().toLocalString()���ݲ���
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);

		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c
				.getTime());
		return dayBefore;
	}

	/**
	 * ���ָ�����ڵĺ�һ��
	 * 
	 * @param specifiedDay
	 * @return
	 * @throws java.text.ParseException
	 */
	public static String getSpecifiedDayAfter(String specifiedDay)
			throws java.text.ParseException {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);

		String dayAfter = new SimpleDateFormat("yyyy-MM-dd")
				.format(c.getTime());
		return dayAfter;
	}

	/**
	 * �ж��ƶ������Ƿ���ڽ���
	 */
	public static Boolean IsMoreThanToday(String specifiedDay) {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Date d = null;
		try {
			date = dfs.parse(GetDateAfterToday(0));
			d = dfs.parse(specifiedDay);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return false;
		}
		if (d.after(date))
			return true;
		else
			return false;
	}

	/**
	 * ���ָ������ʱ���HH:mm
	 */
	public static String getTime(String dateString)
			throws java.text.ParseException {
		String time = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(dateString);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			// Calendar.HOUR 12Сʱ�� Calendar.HOUR_OF_DAY 24Сʱ�Ƶ�ʱ��
			String minute = "";
			String hour = "";
			if (c.get(Calendar.MINUTE) < 10) {
				minute = "0" + Integer.toString(c.get(Calendar.MINUTE));
			} else
				minute = Integer.toString(c.get(Calendar.MINUTE));
			if (c.get(Calendar.HOUR_OF_DAY) < 10) {
				hour = "0" + Integer.toString(c.get(Calendar.HOUR_OF_DAY));
			} else
				hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
			time = hour + ":" + minute;
		} catch (Exception e) {
			time = dateString.substring(dateString.length() - 8,
					dateString.length() - 3);
		}
		return time;
	}

	/**
	 * ���ָ������ʱ���YYYY-MM:dd ��-��-��
	 */
	public static String getDate(String dateString)
			throws java.text.ParseException {
		String time = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = sdf.parse(dateString);
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			String month = "";
			if (c.get(Calendar.MONTH) < 9)
				month = "0" + (c.get(Calendar.MONTH) + 1);
			else
				month = String.valueOf(c.get(Calendar.MONTH) + 1);
			time = c.get(Calendar.YEAR) + "-" + month + "-"
					+ c.get(Calendar.DAY_OF_MONTH);
		} catch (Exception e) {
			e.printStackTrace();
			return dateString.substring(0, dateString.indexOf(" "));
		}
		return time;
	}
	/**
	 * ���ָ������ʱ���MM:dd ��-��
	 */
	public static String getMonthDayDate(String dateString)
			throws java.text.ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse(dateString);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String month = "", day = "";
		if (c.get(Calendar.MONTH) < 9)
			month = "0" + (c.get(Calendar.MONTH) + 1);
		else
			month = String.valueOf(c.get(Calendar.MONTH) + 1);
		if (c.get(Calendar.DAY_OF_MONTH) < 9)
			day = "0" + c.get(Calendar.DAY_OF_MONTH);
		else
			day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String time = month + "-" + day;
		return time;
	}

	/**
	 * ���ָ������ʱ��Ϊ���ڼ�
	 */
	public static String getDayOfWeek(String dateString)
			throws java.text.ParseException {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String today = formatter.format(date);

		String str1 = DateUtil.getDate(dateString);
		String tomorrow = DateUtil.getSpecifiedDayAfter(today);
		String houtian = DateUtil.getSpecifiedDayAfter(DateUtil
				.getSpecifiedDayAfter(today));

		if (DateUtil.getDate(dateString).equals(today)) {
			return "����";
		} else if (DateUtil.getDate(dateString).trim()
				.equals(DateUtil.getSpecifiedDayAfter(today).trim())) {
			return "����";
		} else if (DateUtil.getDate(dateString).equals(
				DateUtil.getSpecifiedDayAfter(DateUtil
						.getSpecifiedDayAfter(today)))) {
			return "����";
		}

		String[] weekDays = { "������", "����һ", "���ڶ�", "������", "������", "������", "������" };
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date2 = sdf.parse(dateString);
		Calendar c = Calendar.getInstance();
		c.setTime(date2);
		Integer time = c.get(Calendar.DAY_OF_WEEK);
		return weekDays[time - 1];
	}

	public static Boolean compareDateIsBefore(String DATE1, String DATE2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt1 = df.parse(DATE1);
			Date dt2 = df.parse(DATE2);
			if (dt1.getTime() > dt2.getTime()) {
				// System.out.println("dt1 >dt2");
				return true;
			} else if (dt1.getTime() < dt2.getTime()) {
				// System.out.println("dt1<dt2");
				return false;
			} else {// ͬһ��
				return true;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return false;
	}

	/**
	 * ��������֮����������Ĺ�ͨ
	 * 
	 * @param from
	 *            �_ʼ�r�g
	 * @param to
	 *            ���K�˕r�g
	 * @return������
	 */
	public static String getDaysBetweenTwoDates(String dateFrom, String dateEnd) {
		Date dtFrom = null;
		Date dtEnd = null;
		dtFrom = toDate(dateFrom, "yyyyMMdd");
		dtEnd = toDate(dateEnd, "yyyyMMdd");
		long begin = dtFrom.getTime();
		long end = dtEnd.getTime();
		long inter = end - begin;
		if (inter < 0) {
			inter = inter * (-1);
		}
		long dateMillSec = 24 * 60 * 60 * 1000;

		long dateCnt = inter / dateMillSec;

		long remainder = inter % dateMillSec;

		if (remainder != 0) {
			dateCnt++;
		}
		return String.valueOf(dateCnt);
	}

	/**
	 * �ַ���(yyyyMMdd)ת����Ϊjava.util.Date
	 * 
	 * @param sDate
	 *            �ַ���(yyyyMMdd)
	 * @param sFmt
	 *            format
	 * @return Date java.util.Date����
	 */
	public static Date toDate(String sDate, String sFmt) {
		Date dt = null;
		try {
			dt = new SimpleDateFormat(sFmt).parse(sDate);
		} catch (Exception e) {
			return dt;
		}
		return dt;
	}

	// ��ȡ�����С
	public static int adjustFontSize(int screenWidth, int screenHeight) {
		screenWidth = screenWidth > screenHeight ? screenWidth : screenHeight;
		/**
		 * 1. ����ͼ�� onsizechanged���ȡ��ͼ��ȣ�һ�������Ĭ�Ͽ����320�����Լ���һ�����ű��� rate = (float)
		 * w/320 w��ʵ�ʿ�� 2.Ȼ������������ߴ�ʱ paint.setTextSize((int)(8*rate));
		 * 8���ڷֱ��ʿ�Ϊ320 ����Ҫ���õ������С ʵ�������С = Ĭ�������С x rate
		 */
		int rate = (int) (6 * (float) screenWidth / 320); // ���Լ�������������Ƚ��ʺϣ���Ȼ����Բ��Ժ����޸�
		return rate < 15 ? 15 : rate; // ����̫СҲ���ÿ���
	}

	/**
	 * Listȥ��
	 * */
	public static List removeDuplicateWithOrder(List list) {
		Set set = new HashSet();
		List newList = new ArrayList();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (set.add(element))
				newList.add(element);
		}
		return newList;
	}
}
