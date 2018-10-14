package ch.epfl.sweng.swengproject.helpers.inputvalidation;

import android.widget.Toast;

import ch.epfl.sweng.swengproject.MyApplication;

public class InscriptionValidator {
    private InscriptionValidator(){}

    public static boolean fieldsAreValid(String email, String password, String firstName, String lastName){

        if(email == null || email.length() == 0){
            Toast.makeText(MyApplication.getAppContext(), "An email address is mandatory", Toast.LENGTH_LONG).show();
            return false;
        }else if(email.length() > 50){
            Toast.makeText(MyApplication.getAppContext(), "An email address cannot be longer than 50 characters", Toast.LENGTH_LONG).show();
            return false;
        }else if(password == null || password.length() < 6){
            Toast.makeText(MyApplication.getAppContext(), "A password of length 6 minimum is required", Toast.LENGTH_LONG).show();
            return false;
        }else if(password.length() > 50){
            Toast.makeText(MyApplication.getAppContext(), "Your password cannot be longer than 50 characters", Toast.LENGTH_LONG).show();
            return false;
        }else if(firstName != null && firstName.length() > 50){
            Toast.makeText(MyApplication.getAppContext(), "Your first name cannot be longer than 50 characters", Toast.LENGTH_LONG).show();
            return false;
        }else if(lastName != null && lastName.length() > 50){
            Toast.makeText(MyApplication.getAppContext(), "Your last name cannot be longer than 50 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
