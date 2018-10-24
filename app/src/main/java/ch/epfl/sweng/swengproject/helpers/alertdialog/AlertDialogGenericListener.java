package ch.epfl.sweng.swengproject.helpers.alertdialog;

public interface AlertDialogGenericListener {
    void onPositiveClick(int id);
    void onNeutralClick(int id);
    void onNegativeClick(int id);
}
