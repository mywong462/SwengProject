package ch.epfl.sweng.swengproject.helpers.alertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class EmailAlreadyExistAlertDialog extends DialogFragment  {

    // Use this instance of the interface to deliver action events
    EmailAlExADListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (EmailAlExADListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.getClass().getName() + " must implement EmailAlExADListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("This email already exist in our database")
                .setMessage("If you want to use an other email address, please click the \"Change Email\" button below. If you want to use this address, please login with it")
                .setPositiveButton("Change Email", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onEmailAlExDialogPositiveClick(EmailAlreadyExistAlertDialog.this);
                    }
                })
                .setNegativeButton("Login with this email", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onEmailAlExNegativeClick(EmailAlreadyExistAlertDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
