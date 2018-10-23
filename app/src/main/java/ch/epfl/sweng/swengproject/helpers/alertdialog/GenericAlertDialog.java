package ch.epfl.sweng.swengproject.helpers.alertdialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * A general class used to display some AlertDialog in the good way
 */
public class GenericAlertDialog extends DialogFragment  {

    // Use this instance of the interface to deliver action events
    private AlertDialogGenericListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the AlertDialogGenericListener so we can send events to the host
            mListener = (AlertDialogGenericListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.getClass().getName() + " must implement AlertDialogGenericListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        String title = getArguments().getString("title");
        String message = getArguments().getString("message");
        String positive = getArguments().getString("positiveButton");
        String neutral = getArguments().getString("neutralButton");
        String negative = getArguments().getString("negativeButton");
        final int myIdentifier = getArguments().getInt("dialogID", -2);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(title != null){
            builder.setTitle(title);
        }
        if(message != null){
            builder.setMessage(message);
        }
        if(positive != null){
            builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mListener.onPositiveClick(myIdentifier);
                }
            });
        }
        if(neutral != null){
            builder.setNeutralButton(neutral, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mListener.onNeutralClick(myIdentifier);
                }
            });
        }
        if(negative != null){
            builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mListener.onNegativeClick(myIdentifier);
                }
            });
        }

        final AlertDialog ad = builder.create();

        ad.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
                ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
            }
        });


        return ad;
        }
}
