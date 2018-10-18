package ch.epfl.sweng.swengproject;

import android.content.Context;

import org.junit.Test;

import java.io.File;

import ch.epfl.sweng.swengproject.storage.filesystem.Generic;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
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

        assertTrue(Generic.deleteFile(".app/doesnt exist"));

    }

    @Test
    public void canDeleteFolderFile(){

        new File("./app/file").mkdir();

        Context c = mock(Context.class);

        when(c.getFilesDir()).thenReturn(new File("./app/file"));

        Generic.setContext(c);

        assertTrue(Generic.deleteFolder("./app/file"));

        assertTrue(Generic.deleteFolder(".app/doesnt exist"));

    }

    @Test(expected = NullPointerException.class)
    public void wrongInput(){

        Generic.deleteFile("");

    }

    @Test(expected = NullPointerException.class)
    public void wringInput2(){
        Generic.deleteFolder(null);
    }

    @Test(expected = NullPointerException.class)
    public void wrongInput3(){
        Generic.setContext(null);
    }


}
