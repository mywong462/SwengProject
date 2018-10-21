package ch.epfl.sweng.swengproject;

import android.Manifest;
import android.app.Activity;
import android.arch.core.util.Function;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import ch.epfl.sweng.swengproject.storage.db.User;
import ch.epfl.sweng.swengproject.util.FakeLocation;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.text.Editable;
import android.widget.EditText;


import java.util.concurrent.Executor;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AddNeedInstrumentedTest2 {

    @Rule
    public GrantPermissionRule grantPermissionRule  = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Rule
    public final ActivityTestRule<AddNeedActivity> mActivityRule =
            new ActivityTestRule<>(AddNeedActivity.class, false, false);

    @Before
    public void create(){

        LocationServer ls = new FakeLocation();

        //inject the mocked object in the activity
        mActivityRule.launchActivity(new Intent());
        mActivityRule.getActivity().setAddNeedActivity(true, ls);

    }

    @Test
    public void canAccessActivityAndClickOnMap() {

        onView(withId(R.id.choose_loc_btn)).perform(click());

    }

    @Mock
    private EditText editValidityNotOk = mock(EditText.class);
    @Mock
    private EditText editDecsr = mock(EditText.class);
    @Mock
    private EditText editNbPeople = mock(EditText.class);
    @Mock
    private LatLng latLong = new LatLng(0.0, 0.0);
    @Mock
    private LocationServer locServ = mock(LocationServer.class);
    @Mock
    private Editable value1 = mock(Editable.class);
    @Mock
    private Editable value2 = mock(Editable.class);
    @Mock
    private Editable value3 = mock(Editable.class);

    /*
    @Test (expected = NumberFormatException.class)
    public void testGetAndSetToDatabaseValidityError(){
        when(editValidityNotOk.getText()).thenReturn(value1);
        when(value1.toString()).thenReturn("fail"); //not valid
        when(editDecsr.getText()).thenReturn(value2); //valid
        when(value2.toString()).thenReturn("descriptionAtLeast10");
        when(editNbPeople.getText()).thenReturn(value3); //valid
        when(value3.toString()).thenReturn("1");

        when(editDecsr.length()).thenReturn(10); //length should be at least 10
        mActivityRule.getActivity().getAndSetToDatabase(editValidityNotOk, editDecsr, editNbPeople);
    }*/

    @Test
    public void testGetAndSetToDatabaseDescrTooShort(){
        Looper.prepare();

        when(editValidityNotOk.getText()).thenReturn(value1);
        when(value1.toString()).thenReturn("1"); //not valid
        when(editDecsr.getText()).thenReturn(value2); //valid
        when(value2.toString()).thenReturn("tooshort");
        when(editNbPeople.getText()).thenReturn(value3); //valid
        when(value3.toString()).thenReturn("1");

        when(editDecsr.length()).thenReturn(8); //length should be at least 10
        mActivityRule.getActivity().getAndSetToDatabase(editValidityNotOk, editDecsr, editNbPeople);
    }

    @Test
    public void testGetAndSetToDatabaseNotEnoughPeople(){
        when(editValidityNotOk.getText()).thenReturn(value1);
        when(value1.toString()).thenReturn("1"); //not valid
        when(editDecsr.getText()).thenReturn(value2); //valid
        when(value2.toString()).thenReturn("descriptionAtLeast10");
        when(editNbPeople.getText()).thenReturn(value3); //valid
        when(value3.toString()).thenReturn("0");

        when(editDecsr.length()).thenReturn(10); //length should be at least 10

        mActivityRule.getActivity().getAndSetToDatabase(editValidityNotOk, editDecsr, editNbPeople);
    }

    @Test
    public void testGetAndSetOKLatLng(){

        when(editValidityNotOk.getText()).thenReturn(value1);
        when(value1.toString()).thenReturn("1"); //not valid
        when(editDecsr.getText()).thenReturn(value2); //valid
        when(value2.toString()).thenReturn("descriptionAtLeast10");
        when(editNbPeople.getText()).thenReturn(value3); //valid
        when(value3.toString()).thenReturn("1");


        when(editDecsr.length()).thenReturn(10); //length should be at least 10
        mActivityRule.getActivity().getAndSetToDatabase(editValidityNotOk, editDecsr, editNbPeople);
    }

    @Test
    public void testGetAndSetOKLoc(){

        when(editValidityNotOk.getText()).thenReturn(value1);
        when(value1.toString()).thenReturn("1"); //not valid
        when(editDecsr.getText()).thenReturn(value2); //valid
        when(value2.toString()).thenReturn("descriptionAtLeast10");
        when(editNbPeople.getText()).thenReturn(value3); //valid
        when(value3.toString()).thenReturn("1");

        when(editDecsr.length()).thenReturn(10); //length should be at least 10

        when(locServ.getLastLocation()).thenReturn(latLong);
        mActivityRule.getActivity().getAndSetToDatabase(editValidityNotOk, editDecsr, editNbPeople);
    }

    /*
    @Mock
    private FirebaseUser us = mock(FirebaseUser.class);

    @Test
    public void testWriteNewNeed(){
        when(Database.getDBauth.getCurrentUser()).thenReturn(us);
        when(Database.getDBauth.getCurrentUser().getEmail()).thenReturn("benoitknuchel@gmail.com");
        mActivityRule.getActivity().writeNewNeed("thisisavaliddescr", 10L, latLong, 1);
    }*/
/*
    @Mock
    private Task<DocumentReference> docRefTaskSucc = new Task<DocumentReference>() {
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
        public DocumentReference getResult() {
            return null;
        }

        @Nullable
        @Override
        public <X extends Throwable> DocumentReference getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnSuccessListener(@NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };
    @Mock
    private Task<DocumentReference> docRefTaskNotSucc = new Task<DocumentReference>() {
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
        public DocumentReference getResult() {
            return null;
        }

        @Nullable
        @Override
        public <X extends Throwable> DocumentReference getResult(@NonNull Class<X> aClass) throws X {
            return null;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnSuccessListener(@NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super DocumentReference> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<DocumentReference> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };

    @Test
    public void testWriteInsideTaskSucc(){
        Looper.prepare();
        AddNeedActivity addNeedActivity = new AddNeedActivity(true, latLong);
        addNeedActivity.writeNewNeedInside(docRefTaskSucc);
    }

    @Test
    public void testWriteInsideTaskNotSucc(){
        AddNeedActivity addNeedActivity = new AddNeedActivity(true, latLong);
        addNeedActivity.writeNewNeedInside(docRefTaskNotSucc);
    }*/

}
