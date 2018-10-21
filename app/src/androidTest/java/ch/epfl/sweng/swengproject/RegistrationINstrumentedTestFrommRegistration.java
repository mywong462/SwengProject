package ch.epfl.sweng.swengproject;

import android.app.Activity;
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

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class RegistrationINstrumentedTestFrommRegistration {


    @Rule
    public final ActivityTestRule<RegistrationActivity> mActivityRule =
            new ActivityTestRule<>(RegistrationActivity.class);

    @Before
    public void testMode(){
        mActivityRule.getActivity().setTestMode();
    }


    @Test
    public void testListenerSuccess(){


        Task<Void> t = new Task<Void>() {
            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public boolean isSuccessful() {
                return true;
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

    }


    @Test
    public void testListenerFail(){


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

    }


    @Test
    public void canClick(){

        onView(withId(R.id.register_btn2)).perform(click());


    }

}
