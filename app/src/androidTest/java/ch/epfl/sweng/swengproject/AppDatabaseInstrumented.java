package ch.epfl.sweng.swengproject;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.swengproject.storage.db.AppDatabase;

public class AppDatabaseInstrumented {



    @Rule
    public final ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);



    @Test
    public void AppDBgetInstance(){

        AppDatabase.getInHDInstance();

    }
}
