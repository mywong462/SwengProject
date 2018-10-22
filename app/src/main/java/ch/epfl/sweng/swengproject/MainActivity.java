package ch.epfl.sweng.swengproject;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    
    private ConstraintLayout scrWelcome;

    private ImageView logo;

    private final int second = 1000;

    private  Timer timer = new Timer();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        ((MyApplication) this.getApplication()).setUser_need_ttl_OnStart(0L);
        ((MyApplication) this.getApplication()).setUser_need_ppl_OnStart(0);

        scrWelcome = findViewById(R.id.welcome_scr);
        scrWelcome.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent m) {


                if (timer!=null){
                    timer.cancel();
                    timer = null;
                }

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_left);
                return true;
            }
        });

        /** For testing and developing purposes, generates and logs the InstanceId token */
        get_fcm_InstanceId();


        logo = findViewById(R.id.logo);

        TimerTask move = new TimerTask() {

            @Override

            public void run() {

                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        moveLogo();
                    }
                });
            }

        };


        long delay = 0;


        timer.scheduleAtFixedRate(move, delay, second);

        moveLogo();

    }

    private void moveLogo(){

        Pair<Float,Float> p = randomPos();

        logo.setX(p.first);
        logo.setY(p.second);

    }

    private Pair<Float,Float> randomPos(){

        float x = 0;
        float y = 0;

        Display display = getWindowManager(). getDefaultDisplay();

        Point size = new Point();
        display. getSize(size);
        int screenWidth = size.x;
        int screenHeight = size.y;

        double factor1 = Math.random();
        double factor2 = Math.random();

        double imageHeight = logo.getMeasuredHeight();
        double imageWidth = logo.getMeasuredWidth();

        x = (float)( screenWidth * factor1);
        y = (float)(screenHeight * factor2);

        if(x > screenWidth - imageWidth){
            x = (float)(x - imageWidth);
        }
        else if(x < imageWidth){
            x = (float)(x + imageWidth);
        }

        if(y > screenWidth - imageHeight){
            y = (float)(y - imageHeight);
        }
        else if(y < imageHeight){
            y = (float)(y + imageHeight);
        }

        return new Pair<>(x,y);
    }

    /** To retrieve the current registration token of the client app */
    public void get_fcm_InstanceId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d("token", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("token", "success getting new InstanceId: " + token);
                    }
                });
    }

}
