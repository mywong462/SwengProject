package ch.epfl.sweng.swengproject.helpers.alertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

//https://developer.android.com/guide/topics/ui/dialogs#top_of_page
public  class InscriptionAlertDialog extends DialogFragment {

    // Use this instance of the interface to deliver action events
    InscriptionADListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (InscriptionADListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.getClass().getName() + " must implement InscriptionADListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please certify your email ")
                .setMessage("Before continuing to use this application, we must ensure that you provided your real email account. For this, please check your mailbox and click on the link you received from us. \nNo email received ? Maybe you made an error when typing your email...")
                .setPositiveButton("That's done !", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onInscriptionDialogPositiveClick(InscriptionAlertDialog.this);
                    }
                })
                .setNegativeButton("Check my email", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onInscriptionDialogNegativeClick(InscriptionAlertDialog.this);
                    }
                });
        // Create the AlertDialog object and return it
        final AlertDialog ad = builder.create();

        ad.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
            }
        });

        //ad.setCancelable(false);
        ad.setCanceledOnTouchOutside(false);

        return ad;
    }
}