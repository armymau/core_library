package core.service;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import armymau.it.core_library.R;
import core.fragment.ProgressDialogFragment;


public abstract class ProgressAsyncTask <T> extends AsyncTask<String, String, T> {
    private final Activity activity;
    private boolean isVisibleProgress = true;
    private ProgressDialogFragment progressFragment;

    ProgressAsyncTask(Activity context, boolean isVisibleProgress) {
        progressFragment = new ProgressDialogFragment();
        this.activity = context;
        this.isVisibleProgress = isVisibleProgress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isVisibleProgress) {
            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            progressFragment.show((FragmentActivity) activity, R.string.core_waiting);
        }
    }

    protected void onProgressUpdate(String values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(T result) {
        super.onPostExecute(result);
        if (progressFragment != null && progressFragment.isAdded()) {
            progressFragment.dismiss();
        }
    }
}
