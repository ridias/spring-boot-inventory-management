package ad.inventory.shared;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatHelper {

	private DateTimeFormatHelper() {
		
	}

	public static String transformSecondsToTime(long numSeconds) {
		if(numSeconds < 0) return "";
		int hours = (int) numSeconds / 3600;
		int minutes = ((int) numSeconds / 60) % 60;
		int seconds = (int) numSeconds % 60;
		
		String hoursStr = hours < 10 ? "0" + hours : "" + hours;
		String minutesStr = minutes < 10 ? "0" + minutes : "" + minutes;
		String secondsStr = seconds < 10 ? "0" + seconds : "" + seconds;
		
		return hoursStr + ":" + minutesStr + ":" + secondsStr;
	}
	
	public static String transformLocalDateTimeToString(LocalDateTime datetime, String format) {
		DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern(format);
		return datetime.format(formatterOut);
	}

	public static LocalDateTime transformStringToLocalDateTime(String datetime, String format) {
		DateTimeFormatter formatterOut = DateTimeFormatter.ofPattern(format);
		return LocalDateTime.parse(datetime, formatterOut);
	}
	
	public static LocalDateTime formatLocalDateTimeNow(String format) {
		LocalDateTime now = LocalDateTime.now();
		String nowStr = transformLocalDateTimeToString(now, format);
		return transformStringToLocalDateTime(nowStr, format);
	}
	
	public static String getMonthStrFromMonthNum(int month) {
		String[] months = { "Gen", "Feb", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Oct", "Nov", "Des" };
		if(month < 1 || month > 12) return "";
		return months[month - 1];
	}
	
	public static String getDayStrFromDayNum(int dayoftheweek) {
		String[] days = { "DL", "DM", "DX", "DJ", "DV", "DS", "DI" };
		if(dayoftheweek < 1 || dayoftheweek > 7) return "";
		return days[dayoftheweek - 1];
	}
}
