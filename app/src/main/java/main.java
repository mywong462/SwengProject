/**
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class main {

    public static void main(String[] args) throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("app/ServiceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://swengdatabase.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(options);
    }
}
*/