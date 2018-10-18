package ch.epfl.sweng.swengproject;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MockFirebase {

    //All mocked objects
    @Mock
    private FirebaseFirestore mockDB;
    @Mock
    private FirebaseAuth authDB;
    @Mock
    private CollectionReference refDB;


    //Empty constructor needed
    private MockFirebase(){}

    private FirebaseFirestore instantiateMock(){
        MockitoAnnotations.initMocks(this);
        instantiateFirebaseFirestore();
        instantiateTasks();
        instantiateDataSnapshot();
        instantiateDatabaseError();
        return mockDB;
    }

    private void instantiateFirebaseFirestore(){
        when(mockDB.getReference)
    }

}
