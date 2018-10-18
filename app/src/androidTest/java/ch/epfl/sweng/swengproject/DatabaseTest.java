package ch.epfl.sweng.swengproject;


import android.app.Activity;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.concurrent.Executor;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DatabaseTest {

    //ALL MOCKED OBJECTS
    @Mock
    private FirebaseAuth auth = mock(FirebaseAuth.class);

    @Mock
    private FirebaseFirestore fbFirestore = mock(FirebaseFirestore.class);

    @Mock
    private CollectionReference collRef = mock(CollectionReference.class);

    @Mock
    private DocumentReference docRef = mock(DocumentReference.class);

    @Mock
    private com.google.firebase.firestore.EventListener eveList = mock(com.google.firebase.firestore.EventListener.class);

    @Mock
    private FirebaseFirestoreException fireExcep = mock(FirebaseFirestoreException.class);

    @Mock
    private ListenerRegistration listReg = mock(ListenerRegistration.class);

    @Mock
    private QuerySnapshot queryDocumentSnapshots = mock(QuerySnapshot.class);

    @Mock
    private DocumentSnapshot docSnap = mock(DocumentSnapshot.class);
    private List<DocumentSnapshot> listDocSnap = new ArrayList<>();



    //TESTS
    @Test
    public void testSaveNeed(){
        Need need = new Need();
        when(fbFirestore.collection("needs")).thenReturn(collRef);
        when(collRef.add(need)).thenReturn(new Task<DocumentReference>() {
            @Override
            public boolean isComplete() {
                return false;
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
        });
        Database.setReference(collRef);
        Database.saveNeed(need);
    }

/*
    @Test
    public void testGetNeeds(){
        //Save a need
        Need need = new Need("emit", "descr", 1, 1.0, 1.0, Categories.HELP, 1, "");
        when(fbFirestore.collection("needs")).thenReturn(collRef);
        when(collRef.add(need)).thenReturn(new Task<DocumentReference>() {
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
                return docRef;
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
        });
        Database.setReference(collRef);
        Database.saveNeed(need);


        when(collRef.addSnapshotListener(eveList)).thenReturn(listReg);
        fireExcep = null;
        when(eveList.onEvent(queryDocumentSnapshots, fireExcep));


        listDocSnap.add(docSnap);
        when(queryDocumentSnapshots.getDocuments()).thenReturn(listDocSnap);
    }*/

}
