package core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    /**  dateformat
     yyyy-MM-dd 1969-12-31
     yyyy-MM-dd 1970-01-01
     yyyy-MM-dd HH:mm 1969-12-31 16:00
     yyyy-MM-dd HH:mm 1970-01-01 00:00
     yyyy-MM-dd HH:mmZ 1969-12-31 16:00-0800
     yyyy-MM-dd HH:mmZ 1970-01-01 00:00+0000
     yyyy-MM-dd HH:mm:ss.SSSZ 1969-12-31 16:00:00.000-0800
     yyyy-MM-dd HH:mm:ss.SSSZ 1970-01-01 00:00:00.000+0000
     yyyy-MM-dd'T'HH:mm:ss.SSSZ 1969-12-31T16:00:00.000-0800
     yyyy-MM-dd'T'HH:mm:ss.SSSZ 1970-01-01T00:00:00.000+0000
     */


    public static boolean isToday(Calendar cal) {
        Calendar today = Calendar.getInstance(Locale.getDefault());
        return today.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean isTomorrow(Calendar cal) {
        Calendar yesterday = adjustDate(Calendar.getInstance(Locale.getDefault()), +1);
        return yesterday.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && yesterday.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR);
    }

    private static Calendar adjustDate(Calendar calendar, int differenceInDay) {
        calendar.setTimeInMillis(calendar.getTimeInMillis() + 24 * 60 * 60 * 1000 * differenceInDay);
        return calendar;
    }

    public static Calendar DateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        return cal;
    }

    public static String getFormattedTime(Date date) {
        try {
            return new SimpleDateFormat("HH:mm").format(date);
        } catch (Exception e) {
            return "undefined time";
        }
    }

    public static String getDayOfWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        return sdf.format(date);
    }

    public static String getDayCutOfWeek(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("EE");
        return sdf.format(date);
    }

    public static Date getFormattedDateFromString(String dateString) {
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getCustomFormattedDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
        Date convertedDate;
        try {
            convertedDate = dateFormat.parse(date);
        } catch (ParseException e) {
            convertedDate = new Date();
        }
        SimpleDateFormat df = new SimpleDateFormat("EE, dd/MM HH:mm", Locale.getDefault());
        String asGmt = df.format(convertedDate);
        return asGmt;
    }

    public static String getFormattedDate() {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date date = new Date();
        String datetime = dateformat.format(date);
        return datetime;
    }

    public static long getTimeMilliSec(String timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(timeStamp);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
