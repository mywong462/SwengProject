package ch.epfl.sweng.swengproject;

import android.content.Context;

import org.junit.Test;

import ch.epfl.sweng.swengproject.storage.db.AppDatabase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppDatabaseUnitTest {


    @Test
    public void createInstanceTest(){

        Context c = mock(Context.class);
        when(c.getApplicationContext()).thenReturn(c);
        AppDatabase apDatabase = AppDatabase.getInMemoryDatabase(c);
        if(apDatabase == null){
            throw new NullPointerException();
        }
        AppDatabase.destroyInstance();

    }

    @Test(expected = NullPointerException.class)
    public void wrongInput(){
        AppDatabase apDatabase = AppDatabase.getInMemoryDatabase(null);
    }

}
