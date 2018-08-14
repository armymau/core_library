package core_kt.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**  dateformat
 * yyyy-MM-dd 1969-12-31
 * yyyy-MM-dd 1970-01-01
 * yyyy-MM-dd HH:mm 1969-12-31 16:00
 * yyyy-MM-dd HH:mm 1970-01-01 00:00
 * yyyy-MM-dd HH:mmZ 1969-12-31 16:00-0800
 * yyyy-MM-dd HH:mmZ 1970-01-01 00:00+0000
 * yyyy-MM-dd HH:mm:ss.SSSZ 1969-12-31 16:00:00.000-0800
 * yyyy-MM-dd HH:mm:ss.SSSZ 1970-01-01 00:00:00.000+0000
 * yyyy-MM-dd'T'HH:mm:ss.SSSZ 1969-12-31T16:00:00.000-0800
 * yyyy-MM-dd'T'HH:mm:ss.SSSZ 1970-01-01T00:00:00.000+0000
 */

fun formattedDate() : String {
    val dateformat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    val date = Date()
    return dateformat.format(date)
}

fun isToday(cal: Calendar): Boolean {
    val today = Calendar.getInstance(Locale.getDefault())
    return today.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)
}

fun isTomorrow(cal: Calendar): Boolean {
    val yesterday = adjustDate(Calendar.getInstance(Locale.getDefault()), +1)
    return yesterday.get(Calendar.YEAR) == cal.get(Calendar.YEAR) && yesterday.get(Calendar.DAY_OF_YEAR) == cal.get(Calendar.DAY_OF_YEAR)
}

fun DateToCalendar(date: Date): Calendar {
    val cal = Calendar.getInstance(Locale.getDefault())
    cal.time = date
    return cal
}

fun getFormattedTime(date: Date): String {
    return try {
        return SimpleDateFormat("HH:mm").format(date)
    } catch (e: Exception) {
        "undefined time"
    }

}

fun getDayOfWeek(date: Date): String {
    val sdf = SimpleDateFormat("EEEE")
    return sdf.format(date)
}

fun getDayCutOfWeek(date: Date): String {
    val sdf = SimpleDateFormat("EE")
    return sdf.format(date)
}

fun getFormattedDateFromString(dateString: String): Date? {
    var date: Date? = null
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
    try {
        date = format.parse(dateString)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return date
}

fun getCustomFormattedDate(date: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    var convertedDate: Date
    try {
        convertedDate = dateFormat.parse(date)
    } catch (e: ParseException) {
        convertedDate = Date()
    }

    val df = SimpleDateFormat("EE, dd/MM HH:mm", Locale.getDefault())
    return df.format(convertedDate)
}

fun getTimeMilliSec(timeStamp: String): Long {
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    try {
        val date = format.parse(timeStamp)
        return date.time
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return 0
}

private fun adjustDate(calendar: Calendar, differenceInDay: Int): Calendar {
    calendar.timeInMillis = calendar.timeInMillis + 24 * 60 * 60 * 1000 * differenceInDay
    return calendar
}