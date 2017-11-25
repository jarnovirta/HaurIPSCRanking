package haur_ranking.utils;

public class DataFormatUtils {
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public static String formatTwoDecimalNumberToString(double number) {
		String returnString = String.valueOf(number);
		if (returnString.lastIndexOf('.') == returnString.length() - 2)
			returnString += "0";
		return returnString;
	}
}
