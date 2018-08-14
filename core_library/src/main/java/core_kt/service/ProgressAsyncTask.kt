package core_kt.service

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.support.v4.app.FragmentActivity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager

import armymau.it.core_library.R
import core_kt.fragment.ProgressDialogFragment


abstract class ProgressAsyncTask<T> internal constructor(private val activity: Activity, isVisibleProgress: Boolean) : AsyncTask<String, String, T>() {

    private var isVisibleProgress = true
    private val progressFragment: ProgressDialogFragment = ProgressDialogFragment()

    init {
        this.isVisibleProgress = isVisibleProgress
    }

    override fun onPreExecute() {
        super.onPreExecute()
        if (isVisibleProgress) {
            (activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(activity.window.decorView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            progressFragment.show(activity as FragmentActivity, R.string.core_waiting)
        }
    }

    protected fun onProgressUpdate(values: String) {
        super.onProgressUpdate(values)
    }

    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        if (progressFragment.isAdded) {
            progressFragment.dismiss()
        }
    }
}
