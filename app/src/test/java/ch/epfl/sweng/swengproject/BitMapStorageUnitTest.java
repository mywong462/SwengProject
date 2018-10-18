package ch.epfl.sweng.swengproject;


import android.content.Context;
import android.graphics.Bitmap;
import org.junit.Test;
import java.io.File;
import ch.epfl.sweng.swengproject.storage.filesystem.BitMapStorage;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BitMapStorageUnitTest {


    @Test
    public void canSaveImage(){

        Context mockC = mock(Context.class);
        when(mockC.getFilesDir()).thenReturn(new File("./app"));
        BitMapStorage.setContext(mockC);

        Bitmap b = mock(Bitmap.class);


        BitMapStorage.saveImage(b,"/");
        BitMapStorage.getImage("/");

    }

    @Test(expected = NullPointerException.class)
    public void badInputs(){

        Context mockC = mock(Context.class);
        when(mockC.getFilesDir()).thenReturn(new File("./app"));
        BitMapStorage.setContext(mockC);

        Bitmap b = mock(Bitmap.class);


        BitMapStorage.saveImage(null,"/");
        BitMapStorage.getImage("");

    }

    @Test(expected = NullPointerException.class)
    public void badInputs2(){

        Context mockC = mock(Context.class);
        when(mockC.getFilesDir()).thenReturn(new File("./app"));
        BitMapStorage.setContext(mockC);

        Bitmap b = mock(Bitmap.class);


        assertFalse(BitMapStorage.saveImage(b,"doesn't exist"));
        BitMapStorage.getImage("");

    }


}
