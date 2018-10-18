package ch.epfl.sweng.swengproject;

import android.content.Context;

import org.junit.Test;

import java.io.File;

import ch.epfl.sweng.swengproject.storage.filesystem.Generic;

import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericUnitTest {

    @Test
    public void canDeleteFile(){

        File test = new File("./app/file");

        Context c = mock(Context.class);

        when(c.getFilesDir()).thenReturn(test);

        Generic.setContext(c);

        assertTrue(Generic.deleteFile("./app/file"));

    }


}
