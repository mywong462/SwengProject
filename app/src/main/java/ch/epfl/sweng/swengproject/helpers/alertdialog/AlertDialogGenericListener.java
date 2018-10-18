package ch.epfl.sweng.swengproject.helpers.alertdialog;

import android.support.v4.app.DialogFragment;

public interface AlertDialogGenericListener {
    void onPositiveClick(DialogFragment dialog);
    void onNeutralClick(DialogFragment dialog);
    void onNegativeClick(DialogFragment dialog);
}
