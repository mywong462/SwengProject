package ch.epfl.sweng.swengproject.helpers.alertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

public class LoginAlertDialog extends DialogFragment {

    // Use this instance of the interface to deliver action events
    LoginADListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (LoginADListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.getClass().getName() + " must implement LoginADListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please certify your email ")
                .setMessage("You cannot log in to this app with an email that has not been validate. Please open your mailbox and click the link you received from us.")
                .setNeutralButton("Ok", null)
                .setPositiveButton("Send me an email again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onLoginDialogPositivClick(LoginAlertDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
