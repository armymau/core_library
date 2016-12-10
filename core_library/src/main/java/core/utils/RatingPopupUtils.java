package core.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RatingPopupUtils {

	public static void showRatingDialog(final Context context, String title, String message, String rateNow, String noRate, String rateLater) {
		setDatePopup(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(rateNow, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				updateInfoVoting(context, CoreConstants.RATING_SHOW_POPUP, false);
				final String appPackageName = context.getPackageName();
				try {
					context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CoreConstants.MARKET_DETAILS + appPackageName)));
				} catch (android.content.ActivityNotFoundException anfe) {
					context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(CoreConstants.MARKET_LANDING_PAGE + appPackageName)));
				}
			}
		});
		builder.setNegativeButton(noRate, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				updateInfoVoting(context, CoreConstants.RATING_SHOW_POPUP, false);
			}
		});
		builder.setNeutralButton(rateLater, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				updateInfoVoting(context, CoreConstants.RATING_SHOW_REMIND, true);
			}
		});
		builder.create();
		builder.show();
	}

	private static void updateInfoVoting(Context context, String key, boolean value) {
		getSP(context).edit().putBoolean(key, value).commit();
	}

	public static void verifyShowingRatingPopup(Context context, String title, String message, String rateNow, String noRate, String rateLater) {
		if (getShowPopup(context)) {
			int day = CoreConstants.INTERVAL_DAY_RATING_POPUP;
			if (getRemindMeLater(context)) {
				day = CoreConstants.INTERVAL_DAY_RATING_POPUP_REMIND;
			}
			if (getDifferenceDay(context) >= day) {
				showRatingDialog(context, title, message, rateNow, noRate, rateLater);
			}
		} else if (getDatePopup(context).equals("")) {
			setDatePopup(context);
			updateInfoVoting(context, CoreConstants.RATING_SHOW_POPUP, true);
		}
	}

	private static boolean getRemindMeLater(Context context) {
		return getSP(context).getBoolean(CoreConstants.RATING_SHOW_REMIND, false);
	}

	private static boolean getShowPopup(Context context) {
		return getSP(context).getBoolean(CoreConstants.RATING_SHOW_POPUP, false);
	}

	private static void setDatePopup(Context context) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		getSP(context).edit().putString(CoreConstants.RATING_DATE, sdf.format(Calendar.getInstance().getTime())).commit();
	}

	private static String getDatePopup(Context context) {
		return getSP(context).getString(CoreConstants.RATING_DATE, "");
	}

	private static SharedPreferences getSP(Context context) {
		return context.getSharedPreferences(CoreConstants.RATING_PREFERENCES, Context.MODE_PRIVATE);
	}

	private static int getDifferenceDay(Context context) {

		int day = -1;

		String firstDate = getDatePopup(context);
		if (!firstDate.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			Date d2 = null, d1 = null;
			try {
				d1 = sdf.parse(firstDate);
				d2 = Calendar.getInstance().getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}

			day = Days.daysBetween(new DateTime(d1), new DateTime(d2)).getDays();
			System.out.println("**** = " + day);
		}
		return day;
	}
}
