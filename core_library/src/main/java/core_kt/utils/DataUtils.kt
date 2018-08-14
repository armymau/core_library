package core_kt.utils

import kotlin.experimental.and
import kotlin.experimental.or

fun md5(s: String): String? {
    try {
        val md = java.security.MessageDigest.getInstance("MD5")
        val array = md.digest(s.toByteArray())
        val sb = StringBuilder()
        for (anArray in array) {
            sb.append(Integer.toHexString((anArray and 0xFF.toByte() or 0x100.toByte()).toInt()).substring(1, 3))
        }
        return sb.toString()
    } catch (e: java.security.NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return null
}