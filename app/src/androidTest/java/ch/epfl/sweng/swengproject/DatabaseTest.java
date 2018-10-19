package ch.epfl.sweng.swengproject;


import android.app.Activity;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.firestore.GeoPoint;
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

    //EVERY MOCKED OBJECTS
    @Mock
    private FirebaseFirestore fbFirestore = mock(FirebaseFirestore.class);

    @Mock
    private CollectionReference collRef = mock(CollectionReference.class);

    @Mock
    private DocumentReference docRef = mock(DocumentReference.class);

    @Mock
    private QuerySnapshot queryDocumentSnapshots = mock(QuerySnapshot.class);

    @Mock
    private DocumentSnapshot docSnap = mock(DocumentSnapshot.class);
    private List<DocumentSnapshot> listDocSnap = new ArrayList<>();

    @Mock
    private Task<QuerySnapshot> taskQuerySnapshot = new Task<QuerySnapshot>() {
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
        public QuerySnapshot getResult() {
            return queryDocumentSnapshots;
        }

        @Nullable
        @Override
        public <X extends Throwable> QuerySnapshot getResult(@NonNull Class<X> aClass) throws X {
            return queryDocumentSnapshots;
        }

        @Nullable
        @Override
        public Exception getException() {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnSuccessListener(@NonNull OnSuccessListener<? super QuerySnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnSuccessListener(@NonNull Executor executor, @NonNull OnSuccessListener<? super QuerySnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnSuccessListener(@NonNull Activity activity, @NonNull OnSuccessListener<? super QuerySnapshot> onSuccessListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnFailureListener(@NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnFailureListener(@NonNull Executor executor, @NonNull OnFailureListener onFailureListener) {
            return null;
        }

        @NonNull
        @Override
        public Task<QuerySnapshot> addOnFailureListener(@NonNull Activity activity, @NonNull OnFailureListener onFailureListener) {
            return null;
        }
    };



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

    @Test
    public void testDefineTaskGetNeeds(){

        when(docSnap.get("longitude")).thenReturn(1.0);
        when(docSnap.get("latitude")).thenReturn(1.0);
        when(docSnap.get("emitter")).thenReturn("emit");
        when(docSnap.get("description")).thenReturn("descr");
        when(docSnap.get("nbPeopleNeeded")).thenReturn(1);
        when(docSnap.get("timeToLive")).thenReturn(1L);
        when(docSnap.get("category")).thenReturn("HELP");
        when(docSnap.get("participants")).thenReturn("");
        when(docSnap.getReference()).thenReturn(docRef);

        listDocSnap.add(docSnap);
        when(queryDocumentSnapshots.getDocuments()).thenReturn(listDocSnap);

        Database.defineTaskGetNeeds(taskQuerySnapshot);
    }

    @Test
    public void testDefineTaskAddParticipant1(){
        listDocSnap.add(docSnap);
        when(queryDocumentSnapshots.getDocuments()).thenReturn(listDocSnap);

        when(docSnap.get("longitude")).thenReturn(0.0);
        when(docSnap.get("latitude")).thenReturn(0.0);
        when(docSnap.get("emitter")).thenReturn("emit");
        when(docSnap.get("description")).thenReturn("descr");
        when(docSnap.get("nbPeopleNeeded")).thenReturn(1);
        when(docSnap.get("timeToLive")).thenReturn(1L);
        when(docSnap.get("category")).thenReturn("HELP");
        when(docSnap.get("participants")).thenReturn("");
        when(docSnap.getReference()).thenReturn(docRef);


        Database.defineTaskAddParticipant(taskQuerySnapshot, new LatLng(0.0, 0.0));
    }

    @Test
    public void testDefineTaskAddParticipant2(){
        listDocSnap.add(docSnap);
        when(queryDocumentSnapshots.getDocuments()).thenReturn(listDocSnap);

        when(docSnap.get("longitude")).thenReturn(0.0);
        when(docSnap.get("latitude")).thenReturn(0.0);
        when(docSnap.get("emitter")).thenReturn("emit");
        when(docSnap.get("description")).thenReturn("descr");
        when(docSnap.get("nbPeopleNeeded")).thenReturn(1);
        when(docSnap.get("timeToLive")).thenReturn(1L);
        when(docSnap.get("category")).thenReturn("HELP");
        when(docSnap.get("participants")).thenReturn("benoitknuchel@gmail.com");
        when(docSnap.getReference()).thenReturn(docRef);


        Database.defineTaskAddParticipant(taskQuerySnapshot, new LatLng(0.0, 0.0));
    }

    @Test(expected = NullPointerException.class)
    public void testSetNeedFromSnapshotNull(){
        Database.setNeedFromSnapshot(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetNeedFromSnapshotNull2(){
        Database.setNeedFromSnapshot(docSnap);
    }

    @Test
    public void testSetNeedFromSnapshotNotNull(){


        when(docSnap.get("longitude")).thenReturn(1.0);
        when(docSnap.get("latitude")).thenReturn(1.0);
        when(docSnap.get("emitter")).thenReturn("emit");
        when(docSnap.get("description")).thenReturn("descr");
        when(docSnap.get("nbPeopleNeeded")).thenReturn(1);
        when(docSnap.get("timeToLive")).thenReturn(1L);
        when(docSnap.get("category")).thenReturn("HELP");
        when(docSnap.get("participants")).thenReturn("");
        when(docSnap.getReference()).thenReturn(docRef);

        Database.setNeedFromSnapshot(docSnap);
    }

}
