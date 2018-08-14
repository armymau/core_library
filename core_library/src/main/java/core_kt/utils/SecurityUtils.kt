package core_kt.utils

import android.util.Base64
import java.security.SecureRandom
import java.util.*
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun encrypt(plaintext: String, pass: String): String? {
    try {
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        val iv = ByteArray(cipher.blockSize)
        SecureRandom().nextBytes(iv)
        cipher.init(Cipher.ENCRYPT_MODE, deriveKeyPad(pass), IvParameterSpec(iv))
        return Base64.encodeToString(iv, Base64.NO_WRAP) + DELIMITER + Base64.encodeToString(cipher.doFinal(plaintext.toByteArray()), Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}

fun decrypt(ciphertext: String, pass: String): String? {
    try {
        val fields = ciphertext.split(DELIMITER.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, deriveKeyPad(pass), IvParameterSpec(Base64.decode(fields[0], Base64.NO_WRAP)))
        return String(cipher.doFinal(Base64.decode(fields[1], Base64.NO_WRAP)))
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }

}

fun deriveKeyPad(pass: String): SecretKey {
    val keyBytes = ByteArray(32)
    Arrays.fill(keyBytes, 0x0.toByte())
    val passwordBytes = pass.toByteArray()
    System.arraycopy(passwordBytes, 0, keyBytes, 0, if (passwordBytes.size < keyBytes.size) passwordBytes.size else keyBytes.size)
    return SecretKeySpec(keyBytes, "AES")
}