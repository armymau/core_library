package core.service;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import core.fragment.ProgressDialogFragment;


public abstract class ProgressAsyncTask extends AsyncTask<String, String, String> {
    private final Activity activity;
    private boolean isVisibleProgress = true;

    ProgressAsyncTask(Activity context, boolean isVisibleProgress) {
        ProgressDialogFragment progressFragment = new ProgressDialogFragment();
        this.activity = context;
        this.isVisibleProgress = isVisibleProgress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isVisibleProgress) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    protected void onProgressUpdate(String values) {
        super.onProgressUpdate(values);
    }
}
