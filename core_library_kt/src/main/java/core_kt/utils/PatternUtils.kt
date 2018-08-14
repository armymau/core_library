package core_kt.utils

import android.content.Context
import android.widget.EditText
import android.widget.RadioGroup
import armymau.it.core_library.R

fun verifyEmptyValue(value: String, editText: EditText, message: String): Boolean {
    var isError = false
    if (value.isEmpty() || value.trim { it <= ' ' }.isEmpty()) {
        editText.error = message
        isError = true
    } else {
        editText.error = null
    }
    return !isError
}

fun verifyInsertNameValue(value: String, editText: EditText): Boolean {
    var nameValue = value.replace("\\s+".toRegex(), "")

    return if (NAME_PATTERN.matcher(nameValue).matches()) {
        editText.error = null
        true
    } else {
        false
    }
}

fun verifyInsertPasswordValue(value: String, editText: EditText): Boolean {
    value.replace("\\s+".toRegex(), "")

    return if (PASSWORD_PATTERN.matcher(value).matches()) {
        editText.error = null
        true
    } else {
        false
    }
}

fun isValidEmail(context: Context, target: CharSequence, editText: EditText): Boolean {
    return verifyEmptyValueForRequired(context, target.toString(), editText) && EMAIL_PATTERN.matcher(target).matches()
}

fun verifyEmptyValueForRequired(context: Context, value: String, editText: EditText): Boolean {
    var isError = false
    if (value.isEmpty() || value.trim { it <= ' ' }.isEmpty()) {
        editText.error = context.getString(R.string.core_required_field)
        isError = true
    } else {
        editText.error = null
    }
    return !isError
}

fun verifyEmptyRadioGroupValueForRequired(radiogroup: RadioGroup): Boolean {
    return radiogroup.checkedRadioButtonId != -1
}