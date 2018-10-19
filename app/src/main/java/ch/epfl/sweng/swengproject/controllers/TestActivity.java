package ch.epfl.sweng.swengproject.controllers;

import android.icu.text.SymbolTable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.Task;

import java.util.concurrent.ExecutionException;

import ch.epfl.sweng.swengproject.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //AsyncTask tv = voidTask().execute();

        try {
            //boolean tb = boolTask().execute().get();
            System.out.println("Before Task");
            System.out.println(multTask().execute(2).get());
            System.out.println("After Task");



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private AsyncTask<Void, Void, Void> voidTask(){

       return new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {
                return null;
            }

           @Override
           protected void onPostExecute(Void aVoid) {
               System.out.println("task done");
           }
       };


    }

    private AsyncTask<Void, Void, Boolean> boolTask(){

        return new AsyncTask<Void, Void, Boolean>(){


            @Override
            protected Boolean doInBackground(Void... voids) {
                return true;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                super.onPostExecute(aBoolean);
            }
        };


    }

    private AsyncTask<Integer, Void, Integer> multTask(){

        return new AsyncTask<Integer, Void, Integer>(){


            @Override
            protected Integer doInBackground(Integer... integers) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 2 * integers[0];
            }
        };


    }
}
