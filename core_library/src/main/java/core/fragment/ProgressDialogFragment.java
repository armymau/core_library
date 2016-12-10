package core.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

public class ProgressDialogFragment extends DialogFragment {
    private static final String KEY_MSG = "KEY_MSG";
    private static boolean wantToCloseDialog = false;
    private ProgressDialog pd;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage(getString(getArguments().getInt(KEY_MSG)));
        setCancelable(false);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        return pd;
    }

    public void show(FragmentManager fragmentManager, int stringId) {
        try {
            Bundle arguments = new Bundle(1);
            arguments.putInt(KEY_MSG, stringId);
            setArguments(arguments);
            if (!ProgressDialogFragment.wantToCloseDialog) {
                show(fragmentManager, ProgressDialogFragment.class.getSimpleName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateMessage(String msg) {
        pd.setMessage(msg);
    }

    public static void wantToCloseDialog(boolean wantToCloseDialog) {
        ProgressDialogFragment.wantToCloseDialog = wantToCloseDialog;
    }
}

