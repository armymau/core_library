package core.utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioGroup;

import armymau.it.core_library.R;

public class PatternUtils {

	public static boolean verifyEmptyValue(Context context, String value, EditText editText, String message) {
		boolean isError = false;
		if (value == null || value.isEmpty() || value.trim().isEmpty()) {
			editText.setError(message);
			isError = true;
		} else {
			editText.setError(null);
		}
		return !isError;
	}

	public static boolean verifyInsertNameValue(Context context, String value, EditText editText) {
		if (value == null) {
			// editText.setError(context.getString(R.string.valore_non_valida));
			return false;
		} else {
			value = value.replaceAll("\\s+", "");

			if (CoreConstants.NAME_PATTERN.matcher(value).matches()) {
				editText.setError(null);
				return true;
			} else {
				// editText.setError(context.getString(R.string.valore_non_valida));
				return false;
			}
		}
	}

	public static boolean verifyInsertPasswordValue(Context context, String value, EditText editText) {
		if (value == null) {
			// editText.setError(context.getString(R.string.valore_non_valida));
			return false;
		} else {
			value.replaceAll("\\s+", "");

			if (CoreConstants.PASSWORD_PATTERN.matcher(value).matches()) {
				editText.setError(null);
				return true;
			} else {
				// editText.setError(context.getString(R.string.valore_non_valida));
				return false;
			}
		}
	}

	public static boolean isValidEmail(Context context, CharSequence target, EditText editText) {
		return verifyEmptyValueForRequired(context, target.toString(), editText) && CoreConstants.EMAIL_PATTERN.matcher(target).matches();
	}

	public static boolean verifyEmptyValueForRequired(Context context, String value, EditText editText) {
		boolean isError = false;
		if (value == null || value.isEmpty() || value.trim().isEmpty()) {
			editText.setError(context.getString(R.string.core_required_field));
			isError = true;
		} else {
			editText.setError(null);
		}
		return !isError;
	}

	public static boolean verifyEmptyRadioGroupValueForRequired(RadioGroup radiogroup) {
		if (radiogroup.getCheckedRadioButtonId() == -1)
			return false;
		return true;
	}
}
