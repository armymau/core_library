package core_kt.fragment

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

class ProgressDialogFragment : DialogFragment() {

    lateinit var pd: ProgressDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        pd = ProgressDialog(activity)
        pd.setMessage(getString(arguments!!.getInt(KEY_MSG)))
        isCancelable = false
        pd.setCancelable(false)
        pd.setCanceledOnTouchOutside(false)
        return pd
    }

    fun updateMessage(msg: String) {
        pd.setMessage(msg)
    }

    fun show(context: FragmentActivity, resId: Int) {
        try {
            val arguments = Bundle(1)
            arguments.putInt(KEY_MSG, resId)
            setArguments(arguments)
            if (!ProgressDialogFragment.wantToCloseDialog) {
                show(context.supportFragmentManager, ProgressDialogFragment::class.java.simpleName)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    fun show(fragmentManager: FragmentManager, resId: Int) {
        try {
            val arguments = Bundle(1)
            arguments.putInt(KEY_MSG, resId)
            setArguments(arguments)
            if (!ProgressDialogFragment.wantToCloseDialog) {
                show(fragmentManager, ProgressDialogFragment::class.java.simpleName)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    companion object {
        private val KEY_MSG = "KEY_MSG"
        private var wantToCloseDialog = false

        fun wantToCloseDialog(wantToCloseDialog: Boolean) {
            ProgressDialogFragment.wantToCloseDialog = wantToCloseDialog
        }
    }
}

