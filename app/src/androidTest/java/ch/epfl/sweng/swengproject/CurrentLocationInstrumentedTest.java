package ch.epfl.sweng.swengproject;

import android.location.Location;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import static junit.framework.TestCase.fail;

@RunWith(AndroidJUnit4.class)
public class CurrentLocationInstrumentedTest {

    private static final String emulatorAuthTokenFile = "/home/simon/.emulator_console_auth_token";

    private String emulatorAuthToken;

    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);


    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Ignore
    public void setCurrentLocationParametersValidTest(){
        mActivityRule.getActivity().currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), mActivityRule.getActivity());
    }

    @Ignore
    public void setCurrentLocationParametersInvalidTest1() {
        try {
            mActivityRule.getActivity().currentLocation.setCurrentLocationParameters(null, mActivityRule.getActivity());
        }catch(NullPointerException e){
            return;
        }
        fail();
    }

    @Ignore
    public void setCurrentLocationParametersInvalidTest2() {
        try {
            mActivityRule.getActivity().currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), null);
        }catch(NullPointerException e){
            return;
        }
        fail();
    }

    @Test
    public void isGetLastLocationCorrect(){
        mActivityRule.getActivity().currentLocation.setCurrentLocationParameters(mActivityRule.getActivity(), mActivityRule.getActivity());

        getEmulatorAuthToken();
        byte byteCommand[] = "telnet localhost 5554 && geo fix 7 46 && exit".getBytes();
        ByteArrayInputStream commands = new ByteArrayInputStream(byteCommand);
        System.setIn(new BufferedInputStream(commands));

        //LatLng lastLocationFromObject = mActivityRule.getActivity().currentLocation.getLastLocation();
    }

    private void getEmulatorAuthToken(){
        try {
            //Process proc = Runtime.getRuntime().exec("cat /home/simon/.emulator_console_auth_token >> /home/simon/token.txt");
            //String[] catCommand = new String[]{"cat /home/simon/.emulator_console_auth_token >> /home/simon/token.txt"};
            //Process proc = new ProcessBuilder(catCommand).start();
            Process p = Runtime.getRuntime().exec("cat /home/simon/.emulator_console_auth_token");
            p.waitFor();
            //Buffere

        }catch (IOException e){
        }catch (InterruptedException e){}

    }


}
