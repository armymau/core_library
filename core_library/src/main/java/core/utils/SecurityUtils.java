package core.utils;

import android.util.Base64;

import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SecurityUtils {

	public static String encrypt(String plaintext, String pass) {
		try {
			Cipher cipher = Cipher.getInstance(CoreConstants.CIPHER_ALGORITHM);
			byte[] iv = new byte[cipher.getBlockSize()];
			new SecureRandom().nextBytes(iv);
			cipher.init(Cipher.ENCRYPT_MODE, deriveKeyPad(pass), new IvParameterSpec(iv));
			return Base64.encodeToString(iv, Base64.NO_WRAP) + CoreConstants.DELIMITER + Base64.encodeToString(cipher.doFinal(plaintext.getBytes()), Base64.NO_WRAP);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String decrypt(String ciphertext, String pass) {
		try {
			String[] fields = ciphertext.split(CoreConstants.DELIMITER);
			Cipher cipher = Cipher.getInstance(CoreConstants.CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, deriveKeyPad(pass), new IvParameterSpec(Base64.decode(fields[0], Base64.NO_WRAP)));
			return new String(cipher.doFinal(Base64.decode(fields[1], Base64.NO_WRAP)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static SecretKey deriveKeyPad(String pass) {
		byte[] keyBytes = new byte[32];
		Arrays.fill(keyBytes, (byte) 0x0);
		byte[] passwordBytes = pass.getBytes();
		System.arraycopy(passwordBytes, 0, keyBytes, 0, passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length);
		return new SecretKeySpec(keyBytes, "AES");
	}
}