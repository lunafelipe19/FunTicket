package com.famtechnology.funticket.util;

import com.crashlytics.android.Crashlytics;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ailton on 08/05/2017.
 */

public class DateFormatter {

    public final static String DATE_DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public final static String DATE_DEFAULT_FORMAT_WITHOUT_T = "yyyy-MM-dd HH:mm:ss";
    public final static String DATE_ONLY_HOUR_DEFAULT_FORMAT = "HH:mm:ss";
    public final static String DATE_DEFAULT_FORMAT_WITHOUT_TIME = "yyyy-MM-dd";
    public final static String DATE_BRAZIL_DEFAULT = "dd/MM/yyyy";
    public final static String DATE_MONTH_YEAR_DEFAULT = "MMMM 'de' yyyy";

    public static String formatToHourMin(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date(time));
    }

    public static String formatToMinSec(long time) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        return dateFormat.format(new Date(time));

        return String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }

    public static String formatToHourMinSec(long time) {
        return String.format(Locale.getDefault(), "%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(time)),
                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)));
    }

    /**
     * Formats a specific calendar into a String format
     * @param format The String format Ex. dd/MM/aaaa
     * @param calendar The Calendar instance
     */
    public static String format(String format, Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, new Locale("pt", "BR"));
        return simpleDateFormat.format(calendar.getTime());
    }

    public static void cleartime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    public static String format(String format, String currentDateFormat, String date) {
        try {
            SimpleDateFormat currentFormat = new SimpleDateFormat(currentDateFormat, new Locale("pt", "BR"));
            SimpleDateFormat toFormat = new SimpleDateFormat(format, new Locale("pt", "BR"));

            Date currentDate = currentFormat.parse(date);
            return toFormat.format(currentDate);
        } catch (Exception error) {
            Crashlytics.logException(error);
            return "";
        }
    }

    public static Date stringToDate(String date) throws Exception {
        return stringToDate(DateFormatter.DATE_DEFAULT_FORMAT, date);
    }

    public static Date stringToDate(String currentDateFormat, String date) throws Exception {
        SimpleDateFormat currentFormat = new SimpleDateFormat(currentDateFormat, new Locale("pt", "BR"));
        return currentFormat.parse(date);
    }

    public static boolean isDateBeforeThanCurrentDate(String stringDate) throws Exception {
        SimpleDateFormat currentFormat = new SimpleDateFormat(DATE_BRAZIL_DEFAULT, new Locale("pt", "BR"));
        Date date = currentFormat.parse(stringDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);

        return isDateBeforeThanCurrentDate(calendar.getTime());
    }

    public static boolean isDateBeforeThanCurrentDate(Date date) {
        Date currentDate = Calendar.getInstance().getTime();
        return date.before(currentDate);// || date.equals(currentDate);
    }

    /**
     * Returns the current hour in HH:mm format
     * @return String with current hour
     */
    public static String getCurrentHour() {
        //creates a simple date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        //and returns the hour in hh:mm format
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * Returns the current hour in HH:mm format
     */
    public static String getCurrentDate() {
        //creates a simple date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        //and returns the hour in hh:mm format
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * Returns the current hour in HH:mm format
     */
    public static String getDate(Calendar calendar, String format) {
        //creates a simple date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());

        //and returns the hour in hh:mm format
        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * Returns the current hour in HH:mm format
     */
    public static String getDate(Calendar calendar) {
        return getDate(calendar, "dd/MM/yyyy");
    }

    /**
     * Returns the current hour in HH:mm format
     */
    public static String getCurrentDateDefaultFormmat() {
        //creates a simple date format
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateFormatter.DATE_DEFAULT_FORMAT, Locale.getDefault());

        //and returns the hour in hh:mm format
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public static boolean isZeroOrEmpty(String hour) {
        return hour.isEmpty() || hour.equals("00:00:00");
    }
}
