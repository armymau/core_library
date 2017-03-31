package core.utils;


import java.util.regex.Pattern;

public class CoreConstants {

    public static final String TAG = ">>> CORE";
    public static final boolean isDebug = true;
    public static final boolean isSigned = false;

    public static final int HTTP_TIMEOUT = 7 * 1000;    // milliseconds
    public static final int LONG_DELAY = 5000;          // 3.5 seconds
    public static final int SHORT_DELAY = 2000;         // 2 seconds

    public static final int NO_CONNECTION = 0;
    public static final int MOBILE = 1;
    public static final int WIFI = 2;

    public static final int CHECK_PERMISSIONS_REQUEST_CODE = 3423;


    public static final String HTTP_REQUEST_METHOD_GET = "GET";
    public static final String HTTP_REQUEST_METHOD_POST = "POST";
    public static final String HTTP_METHOD_REPONSE_CACHING_FAILED = "REPONSE_CACHING_FAILED";
    public static final int HTTP_METHOD_GET = 0;
    public static final int HTTP_METHOD_POST = 1;
    public static final int HTTP_METHOD_GET_WITH_AUTHORIZATION = 2;
    public static final int HTTP_METHOD_POST_WITH_MULTIPART = 3;
    public static final int HTTP_METHOD_GET_WITH_MULTI_AUTHORIZATION = 4;
    public static final int HTTP_METHOD_POST_WITH_MULTI_AUTHORIZATION = 5;
    public static final String HTTP_METHOD_CACHED = "CACHED RESPONSE";


    //SECURITY UTILS
    public static final String DELIMITER = "]";
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    //RATING POPUP UTILS
    public static final int INTERVAL_DAY_RATING_POPUP = 3;
    public static final int INTERVAL_DAY_RATING_POPUP_REMIND = 7;
    public static final String MARKET_DETAILS = "market://details?id=";
    public static final String MARKET_LANDING_PAGE = "http://play.google.com/store/apps/details?id=";
    public static final String RATING_PREFERENCES = "rating_preferences";
    public static final String RATING_DATE = "rating_date";
    public static final String RATING_SHOW_POPUP = "rating_showpopup";
    public static final String RATING_SHOW_REMIND = "rating_remind";

    //PATTERN REGEX
    public static final String PATTERN_IMAGE_REGEX = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>";
    public static final String PATTERN_URL_REGEX = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";
    public static final String PATTERN_MENTION_REGEX = "@([A-Za-z0-9_-]+)";
    public static final String PATTERN_HASHTAG_REGEX = "#([A-Za-z0-9_-]+)";
    public static final String PATTERN_NUMBER_REGEX = "^[0-9]{7,15}$";
    public static final String PATTERN_EMAIL_REGEX = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+";
    public static final String PATTERN_NAME_REGEX = "^[_A-Za-z-\\+]+(\\.[_A-Za-z-]+)*";
    public static final String PATTERN_PASSWORD_REGEX = "(?=.*\\d)(?=.*[a-zA-Z0-9])";

    public static final Pattern NUMBER_PATTERN = Pattern.compile(CoreConstants.PATTERN_NUMBER_REGEX);
    public static final Pattern EMAIL_PATTERN = Pattern.compile(CoreConstants.PATTERN_EMAIL_REGEX);
    public static final Pattern NAME_PATTERN = Pattern.compile(CoreConstants.PATTERN_NAME_REGEX);
    public static final Pattern IMAGE_PATTERN = Pattern.compile(CoreConstants.PATTERN_IMAGE_REGEX);
    public static final Pattern PASSWORD_PATTERN = Pattern.compile(CoreConstants.PATTERN_PASSWORD_REGEX);
    public static final Pattern MENTION_PATTERN = Pattern.compile(CoreConstants.PATTERN_MENTION_REGEX);
    public static final Pattern HASHTAG_PATTERN = Pattern.compile(CoreConstants.PATTERN_HASHTAG_REGEX);
}
