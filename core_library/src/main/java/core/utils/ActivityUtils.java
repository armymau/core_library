package core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityUtils {
	
	/**
	 * Per mostare la tastiera.
	 * @param activity  Attività che invoca il metodo.
	 * @param view  View per cui si vuole mostrare la tastiera.
	 */
	public static void showSoftKeyboard(Activity activity, View view) {
		view.requestFocus();
		InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}
	
	/**
	 * Per nascondare la tastiera.
	 * @param activity  Attività che invoca il metodo.
	 * @param view  View per cui si vuole nascondere la tastiera.
	 */
	public static void hideSoftKeyboard(Activity activity, View view) {
	    if (view != null) {
	        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	    }
	}

	/**
	 * Per chiedere i permessi. Nell'activity che lo invoca implementare "onRequestPermissionsResult".
	 * @param permissions  l'array di permessi da chiedere.
	 * @param requestCode va beh, s'è capito.
	 * @return  false se i permessi vengono chiesti, true se non ce n'è bisogno.
	 */
	public static boolean checkPermission(Activity activity, String[] permissions, int requestCode) {
		ArrayList<String> permissionsL = new ArrayList<>();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null && permissions.length > 0) {
			for (String permission : permissions) {
				if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
					permissionsL.add(permission);
				}
			}
		}
		if (permissionsL.size() > 0) {
			ActivityCompat.requestPermissions(activity, permissionsL.toArray(new String[permissionsL.size()]), requestCode);
			return false;
		}
		return true;
	}

	/**
	 * Controlla se ci sono permessi negati nell'array passato.
	 * @param grantResults  array contenente i risultati delle richieste.
	 * @return  true se ci sono permessi negati, false altrimenti.
	 */
	public static boolean checkDeniedPermission(@NonNull int[] grantResults) {
		boolean permissionDenied = false;
		for (int grantResult : grantResults) {
			if (grantResult != PackageManager.PERMISSION_GRANTED) {
				permissionDenied = true;
				break;
			}
		}
		return permissionDenied;
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
	public static void sendMail(Context context, String[] emails, String subject, String body, String chooser, String fail) {

		Intent i = new Intent(Intent.ACTION_SEND);
		i.putExtra(Intent.EXTRA_EMAIL, emails);
		if(subject != null && subject.trim().length() > 0) {
			i.putExtra(Intent.EXTRA_SUBJECT, subject);
		}
		if(body != null && body.trim().length() > 0) {
			i.putExtra(Intent.EXTRA_TEXT, body);
		}
		i.setType("message/rfc822");
		try {
			context.startActivity(Intent.createChooser(i, chooser));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(context, fail, Toast.LENGTH_LONG).show();
		}
	}
}
