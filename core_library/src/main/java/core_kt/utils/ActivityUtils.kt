package core_kt.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.util.*

/**
 * Per mostare la tastiera.
 * @param activity  Attività che invoca il metodo.
 * @param view  View per cui si vuole mostrare la tastiera.
 */
fun showSoftKeyboard(activity: Activity, view: View) {
    view.requestFocus()
    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Per nascondare la tastiera.
 * @param activity  Attività che invoca il metodo.
 * @param view  View per cui si vuole nascondere la tastiera.
 */
fun hideSoftKeyboard(activity: Activity, view: View?) {
    if (view != null) {
        val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

/**
 * Per chiedere i permessi. Nell'activity che lo invoca implementare "onRequestPermissionsResult".
 * @param permissions  l'array di permessi da chiedere.
 * @param requestCode va beh, s'è capito.
 * @return  false se i permessi vengono chiesti, true se non ce n'è bisogno.
 */
fun checkPermission(activity: Activity, permissions: Array<String>?, requestCode: Int): Boolean {
    val permissionsL = ArrayList<String>()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null && permissions.size > 0) {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsL.add(permission)
            }
        }
    }
    if (permissionsL.size > 0) {
        ActivityCompat.requestPermissions(activity, permissionsL.toTypedArray(), requestCode)
        return false
    }
    return true
}

/**
 * Controlla se ci sono permessi negati nell'array passato.
 * @param grantResults  array contenente i risultati delle richieste.
 * @return  true se ci sono permessi negati, false altrimenti.
 */
fun checkDeniedPermission(grantResults: IntArray): Boolean {
    var permissionDenied = false
    for (grantResult in grantResults) {
        if (grantResult != PackageManager.PERMISSION_GRANTED) {
            permissionDenied = true
            break
        }
    }
    return permissionDenied
}

/**
 * Per inviare una mail.
 *
 * @param emails  gli indirizzi a cui spedire la mail.
 * @param subject  l'oggetto della mail.
 * @param body  il messaggio della mail.
 * @param chooser  il testo da mostrare nel popup di scelta dell'App Mail.
 * @param fail  il messaggio da mostrare in caso di errore.
 */
fun sendMail(context: Context, emails: Array<String>, subject: String?, body: String?, chooser: String, fail: String) {

    val i = Intent(Intent.ACTION_SEND)
    i.putExtra(Intent.EXTRA_EMAIL, emails)
    if (subject != null && subject.trim { it <= ' ' }.isNotEmpty()) {
        i.putExtra(Intent.EXTRA_SUBJECT, subject)
    }
    if (body != null && body.trim { it <= ' ' }.isNotEmpty()) {
        i.putExtra(Intent.EXTRA_TEXT, body)
    }
    i.type = "message/rfc822"
    try {
        context.startActivity(Intent.createChooser(i, chooser))
    } catch (ex: android.content.ActivityNotFoundException) {
        Toast.makeText(context, fail, Toast.LENGTH_LONG).show()
    }

}