package com.sella.cast.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static String formateDateToString(Date date, String string) {
		Date d = Calendar.getInstance().getTime(); // Current time
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); // Set your date format
		return sdf.format(d); // Get Date String according to date format
	}
	
	
	

}
