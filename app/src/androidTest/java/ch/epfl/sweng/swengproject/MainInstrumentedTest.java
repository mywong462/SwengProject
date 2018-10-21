package ch.epfl.sweng.swengproject;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

public class MainInstrumentedTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void logoWork() throws InterruptedException {

        Thread.sleep(10000);
    }
}
