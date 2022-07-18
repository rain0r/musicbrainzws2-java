package org.mc2.util.miscellaneous;

import java.util.Calendar;
import java.util.TimeZone;

import org.mc2.util.exceptions.MC2Exception;

/**
 *
 * @author marco
 */
public class CalendarUtils {

	public static String calcIndexTime(long durTot) {

		if (durTot > Integer.MAX_VALUE)
			return "";

		int d = (int) durTot;
		return calcIndexTime(d);
	}

	public static String calcIndexTime(int durTot) {

		Integer Secs = durTot / 1000;
		Integer msec = durTot - Secs * 1000;
		Integer csec = msec / 10;

		Integer mm = Secs / 60;
		Integer ss = Secs - mm * 60;

		String MM = mm.toString();
		if (MM.length() == 1)
			MM = "0".concat(MM);
		String SS = ss.toString();
		if (SS.length() == 1)
			SS = "0".concat(SS);
		String CC = csec.toString();
		if (CC.length() == 1)
			CC = "0".concat(CC);

		return MM.concat(":").concat(SS).concat(":").concat(CC);
	}

	public static long calcIndexTimeInMillis(String durStr) throws MC2Exception {

		if (durStr == null)
			return 0;
		try {
			Calendar cal = Calendar.getInstance();
			TimeZone tx = TimeZone.getDefault();
			int offset = tx.getOffset(cal.getTimeInMillis());
			cal.setTimeInMillis(0);

			int msec = 0;
			int sec = 0;
			int min = 0;

			long time;

			String[] split = durStr.split(":");
			if (split.length == 1) {
				msec = Integer.parseInt(split[0]) * 10;
				cal.set(Calendar.MILLISECOND, msec);
			}
			else if (split.length == 2) {
				sec = Integer.parseInt(split[0]);
				msec = Integer.parseInt(split[1]) * 10;
				cal.set(Calendar.SECOND, sec);
				cal.set(Calendar.MILLISECOND, msec);
			}
			else if (split.length == 3) {
				min = Integer.parseInt(split[0]);
				sec = Integer.parseInt(split[1]);
				msec = Integer.parseInt(split[2]) * 10;
				cal.set(Calendar.MINUTE, min);
				cal.set(Calendar.SECOND, sec);
				cal.set(Calendar.MILLISECOND, msec);
			}
			else {
				throw new MC2Exception("invalid time format, should be MM:SS:CC");
			}
			/*
			 * Used calendar just as a entry validation. Strange stuffs with dayligth saving offset...
			 */
			long timecal = cal.getTimeInMillis();
			long timeOffset = timecal + offset;

			time = (msec + sec * 1000 + min * 60 * 1000);

			return time;

		}
		catch (NumberFormatException ex) {
			throw new MC2Exception(ex);
		}
	}

	public static String calcDurationString(Long durms) {

		if (durms == null)
			return "";

		Calendar cal = Calendar.getInstance();
		TimeZone tx = TimeZone.getDefault();
		int offset = tx.getOffset(durms);
		cal.setTimeInMillis(durms - offset);

		String dur;

		if (durms > 3600000) // 1 h.
		{
			dur = String.format("%1$tH:%1$tM:%1$tS", cal);
		}
		else {
			dur = String.format("%1$tM:%1$tS", cal);
		}

		return dur;
	}

	public static String calcDurationString(int sectors) {

		return calcDurationString(calcDurationInMillis(sectors));
	}

	public static long calcDurationInMillis(int sectors) {
		return Long.valueOf(sectors * 1000 / 75);
	}

	public static long calcDurationInMillis(String durStr) throws MC2Exception {

		if (durStr == null)
			return 0;
		try {
			Calendar cal = Calendar.getInstance();
			TimeZone tx = TimeZone.getDefault();
			int offset = tx.getOffset(cal.getTimeInMillis());
			cal.setTimeInMillis(0);

			int sec = 0;
			int min = 0;
			int hour = 0;

			long time;

			String[] split = durStr.split(":");
			if (split.length == 1) {
				sec = Integer.parseInt(split[0]);
				cal.set(Calendar.SECOND, sec);
			}
			else if (split.length == 2) {
				min = Integer.parseInt(split[0]);
				sec = Integer.parseInt(split[1]);
				cal.set(Calendar.MINUTE, min);
				cal.set(Calendar.SECOND, sec);
			}
			else if (split.length == 3) {
				hour = Integer.parseInt(split[0]);
				min = Integer.parseInt(split[1]);
				sec = Integer.parseInt(split[2]);
				cal.set(Calendar.HOUR, hour);
				cal.set(Calendar.MINUTE, min);
				cal.set(Calendar.SECOND, sec);
			}
			else {
				throw new MC2Exception("invalid time format, should be HH:MM:SS");
			}
			/*
			 * Used calendar just as a entry validation. Strange stuffs with dayligth saving offset...
			 */
			long timecal = cal.getTimeInMillis();
			long timeOffset = timecal + offset;

			time = (sec + min * 60 + hour * 60 * 60) * 1000;

			return time;

		}
		catch (NumberFormatException ex) {
			throw new MC2Exception(ex);
		}
	}

	public static int calcDurationInSector(long durms) {
		return ((int) durms * 75 / 1000);
	}

	public static int calcDurationInSector(String durSt) throws MC2Exception {
		return calcDurationInSector(calcDurationInMillis(durSt));
	}
}
