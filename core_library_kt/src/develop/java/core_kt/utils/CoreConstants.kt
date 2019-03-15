package core_kt.utils

const val isDebug = true
const val isSigned = false

const val TAG = ">>> CORE"
const val HTTP_TIMEOUT = 15 * 1000    // milliseconds
const val LONG_DELAY = 5000          // 3.5 seconds
const val SHORT_DELAY = 2000         // 2 seconds

const val NO_CONNECTION = 0
const val MOBILE = 1
const val WIFI = 2

const val CHECK_PERMISSIONS_REQUEST_CODE = 3423

const val HTTP_REQUEST_METHOD_GET = "GET"
const val HTTP_REQUEST_METHOD_POST = "POST"
const val HTTP_METHOD_REPONSE_CACHING_FAILED = "REPONSE_CACHING_FAILED"
const val HTTP_METHOD_GET = 0
const val HTTP_METHOD_POST = 1
const val HTTP_METHOD_GET_WITH_AUTHORIZATION = 2
const val HTTP_METHOD_POST_WITH_MULTIPART = 3
const val HTTP_METHOD_GET_WITH_MULTI_AUTHORIZATION = 4
const val HTTP_METHOD_POST_WITH_MULTI_AUTHORIZATION = 5
const val HTTP_METHOD_CACHED = "CACHED RESPONSE"


//SECURITY UTILS
const val DELIMITER = "]"
const val CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding"

//RATING POPUP UTILS
const val INTERVAL_DAY_RATING_POPUP = 3
const val INTERVAL_DAY_RATING_POPUP_REMIND = 7
const val MARKET_DETAILS = "market://details?id="
const val MARKET_LANDING_PAGE = "http://play.google.com/store/apps/details?id="
const val RATING_PREFERENCES = "rating_preferences"
const val RATING_DATE = "rating_date"
const val RATING_SHOW_POPUP = "rating_showpopup"
const val RATING_SHOW_REMIND = "rating_remind"

//PATTERN REGEX
const val PATTERN_IMAGE_REGEX = "<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>"
const val PATTERN_URL_REGEX = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]"
const val PATTERN_MENTION_REGEX = "@([A-Za-z0-9_-]+)"
const val PATTERN_HASHTAG_REGEX = "#([A-Za-z0-9_-]+)"
const val PATTERN_NUMBER_REGEX = "^[0-9]{7,15}$"
const val PATTERN_EMAIL_REGEX = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"
const val PATTERN_NAME_REGEX = "^[_A-Za-z-\\+]+(\\.[_A-Za-z-]+)*"
const val PATTERN_PASSWORD_REGEX = "(?=.*\\d)(?=.*[a-zA-Z0-9])"

val NUMBER_PATTERN = Pattern.compile(PATTERN_NUMBER_REGEX)
val EMAIL_PATTERN = Pattern.compile(PATTERN_EMAIL_REGEX)
val NAME_PATTERN = Pattern.compile(PATTERN_NAME_REGEX)
val IMAGE_PATTERN = Pattern.compile(PATTERN_IMAGE_REGEX)
val PASSWORD_PATTERN = Pattern.compile(PATTERN_PASSWORD_REGEX)
val MENTION_PATTERN = Pattern.compile(PATTERN_MENTION_REGEX)
val HASHTAG_PATTERN = Pattern.compile(PATTERN_HASHTAG_REGEX)