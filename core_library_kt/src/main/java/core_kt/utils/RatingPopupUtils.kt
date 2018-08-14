package core_kt.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import org.joda.time.DateTime
import org.joda.time.Days
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun showRatingDialog(context: Context, title: String, message: String, rateNow: String, noRate: String, rateLater: String) {
    setDatePopup(context)
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(rateNow) { _, _ ->
        updateInfoVoting(context, RATING_SHOW_POPUP, false)
        val appPackageName = context.packageName
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_DETAILS + appPackageName)))
        } catch (anfe: android.content.ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_LANDING_PAGE + appPackageName)))
        }
    }
    builder.setNegativeButton(noRate) { _, _ -> updateInfoVoting(context, RATING_SHOW_POPUP, false) }
    builder.setNeutralButton(rateLater) { _, _ -> updateInfoVoting(context, RATING_SHOW_REMIND, true) }
    builder.create()
    builder.show()
}

private fun updateInfoVoting(context: Context, key: String, value: Boolean) {
    getSP(context).edit().putBoolean(key, value).apply()
}

fun verifyShowingRatingPopup(context: Context, title: String, message: String, rateNow: String, noRate: String, rateLater: String) {
    if (getShowPopup(context)) {
        var day = INTERVAL_DAY_RATING_POPUP
        if (getRemindMeLater(context)) {
            day = INTERVAL_DAY_RATING_POPUP_REMIND
        }
        if (getDifferenceDay(context) >= day) {
            showRatingDialog(context, title, message, rateNow, noRate, rateLater)
        }
    } else if (getDatePopup(context) == "") {
        setDatePopup(context)
        updateInfoVoting(context, RATING_SHOW_POPUP, true)
    }
}

private fun getRemindMeLater(context: Context): Boolean {
    return getSP(context).getBoolean(RATING_SHOW_REMIND, false)
}

private fun getShowPopup(context: Context): Boolean {
    return getSP(context).getBoolean(RATING_SHOW_POPUP, false)
}

private fun setDatePopup(context: Context) {
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    getSP(context).edit().putString(RATING_DATE, sdf.format(Calendar.getInstance().time)).apply()
}

private fun getDatePopup(context: Context): String {
    return getSP(context).getString(RATING_DATE, "")!!
}

private fun getSP(context: Context): SharedPreferences {
    return context.getSharedPreferences(RATING_PREFERENCES, Context.MODE_PRIVATE)
}

private fun getDifferenceDay(context: Context): Int {
    var day = -1

    val firstDate = getDatePopup(context)
    if (firstDate != "") {
        val sdf = SimpleDateFormat("dd-MM-yyyy")

        var d2: Date? = null
        var d1: Date? = null
        try {
            d1 = sdf.parse(firstDate)
            d2 = Calendar.getInstance().time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        day = Days.daysBetween(DateTime(d1), DateTime(d2)).days
        println("**** = $day")
    }
    return day
}