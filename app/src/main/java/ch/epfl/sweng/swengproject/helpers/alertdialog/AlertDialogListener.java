package ch.epfl.sweng.swengproject.helpers.alertdialog;

import android.support.v4.app.DialogFragment;

public interface AlertDialogListener {
    public void onDialogPositiveClick(DialogFragment dialog);
    public void onDialogNeutralClick(DialogFragment dialog);
    public void onDialogNegativeClick(DialogFragment dialog);
}
