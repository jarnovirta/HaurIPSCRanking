package haur_ranking.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateFormatUtils {
	public static String calendarToDateString(Calendar date) {
		String dateString = null;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		try {
			dateString = sdf.format(date.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateString;
	}
}
