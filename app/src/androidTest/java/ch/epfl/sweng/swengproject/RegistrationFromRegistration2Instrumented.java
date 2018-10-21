package ch.epfl.sweng.swengproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.Executor;

public class RegistrationFromRegistration2Instrumented {


    @Rule
    public final ActivityTestRule<RegistrationActivity> mActivityRule =
            new ActivityTestRule<>(RegistrationActivity.class,false,false);


    @Before

    public void init(){
        mActivityRule.launchActivity(new Intent());
        mActivityRule.getActivity().setTestMode();

    }


    @Test
    public void testListenerFail(){

       // Looper.prepare();

        Task<Void> t = new Task<Void>() {
            @Override
            public boolean isComplete() {

                return true;
            }

            @Override
            public boolean isSuccessful() {
                return false;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Nullable
            @Override
            public Void getResult() {
                return null;
            }

            @Nullable
            @Override
            public <X extends Throwable> Void getResult(@NonNull Class<X> aClass) throws X {
                return null;
            }

            @Nullable
            @Override
            public Exception getException() {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnSuccessListener(@NonNull OnSuccessListener<? super Void> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super Void> onSuccessListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
                return null;
            }

            @NonNull
            @Override
            public Task<Void> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
                return null;
            }

            public Task<Void> addOnCompleteListener(OnCompleteListener listener){
                return null;

            }

        };


        t.addOnCompleteListener(mActivityRule.getActivity().listener);

         mActivityRule.getActivity().listener.onComplete(t);


    }

}
