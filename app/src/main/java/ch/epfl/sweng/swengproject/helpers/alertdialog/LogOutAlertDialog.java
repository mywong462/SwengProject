package ch.epfl.sweng.swengproject.helpers.alertdialog;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class LogOutAlertDialog extends DialogFragment {

    // Use this instance of the interface to deliver action events
    private AlertDialogGenericListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AlertDialogGenericListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.getClass().getName() + " must implement LoginADListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Warning")
                .setMessage("Login Out will delete everything that is stored locally.\n Do you really want to log out ?")
                .setNeutralButton("Cancel", null)
                .setNegativeButton("Log Out", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onNegativeClick(LogOutAlertDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        final AlertDialog ad = builder.create();

        ad.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
            }
        });

        return ad;
    }
}
