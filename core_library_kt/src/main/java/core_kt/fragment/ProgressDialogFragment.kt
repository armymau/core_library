package core_kt.fragment

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

class ProgressDialogFragment : DialogFragment() {

    lateinit var pd: ProgressDialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        pd = ProgressDialog(activity)
        pd.setMessage(getString(arguments?.getInt(KEY_MSG) ?: -1))
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

