package ch.epfl.sweng.swengproject;

import org.junit.Test;

import java.util.Date;

import ch.epfl.sweng.swengproject.storage.db.Converters;

import static junit.framework.TestCase.assertTrue;

public class ConvertersUnitTest {


    @Test
    public void canConvertToDate(){

        Date test = new Date();

        long now = System.currentTimeMillis();

        test.setTime(now);

        assertTrue(Converters.fromTimestamp(now).equals(test));

        assertTrue(Converters.dateToTimestamp(test) == now);

    }

}
