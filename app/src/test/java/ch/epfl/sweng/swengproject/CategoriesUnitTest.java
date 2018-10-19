package ch.epfl.sweng.swengproject;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class CategoriesUnitTest {


    @Test
    public void canCreateAndConvertCat(){


        CategoriesInfo catInf = new CategoriesInfo();

        assertEquals(CategoriesInfo.size -1 ,CategoriesInfo.convert(Categories.ALL));
    }
}
