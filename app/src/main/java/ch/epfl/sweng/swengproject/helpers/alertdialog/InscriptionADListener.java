package ch.epfl.sweng.swengproject.helpers.alertdialog;

import android.support.v4.app.DialogFragment;

public interface InscriptionADListener {
    void onInscriptionDialogPositiveClick(DialogFragment dialog);
    void onInscriptionDialogNegativeClick(DialogFragment dialog);
}