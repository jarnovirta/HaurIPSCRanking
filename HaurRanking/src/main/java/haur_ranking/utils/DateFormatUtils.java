package haur_ranking.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormatUtils {
	public static String calendarToDateString(Calendar date) {
		if (date == null)
			return "";
		String dateString = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		try {
			dateString = sdf.format(date.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateString;
	}

	public static Calendar stringToCalendar(String dateString) {
		Calendar calendar = Calendar.getInstance();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
			calendar.setTime(sdf.parse(dateString));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return calendar;
	}
}
