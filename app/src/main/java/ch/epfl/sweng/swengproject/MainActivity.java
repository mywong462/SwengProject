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
        scrWelcome = findViewById(R.id.welcome_scr);
        scrWelcome.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent m) {

                timer.cancel();
                timer.purge();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_left);
                return true;
            }
        });


        logo = findViewById(R.id.logo);

        TimerTask move = new TimerTask() {

            @Override

            public void run() {
                moveLogo();
            }

        };


        long delay = 0;


        timer.scheduleAtFixedRate(move, delay, second);

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

}
