package com.petsoft.task1.base;

import org.junit.Test;

import java.io.File;

import static com.petsoft.task1.util.FileUtils.UNSORTED_FILE;
import static com.petsoft.task1.util.FileUtils.createFile;
import static org.junit.Assert.assertEquals;


/**
 * 20.08.2018 18:29
 *
 * @author PetSoft
 */


public class EntryTest {

    @Test
    public void constructorTest(){
        int[] array = new int[]{1, 2, 3, 4};
        OneDimensionalArrayData arrayData = new OneDimensionalArrayData(array, array.length);
        File file = createFile(UNSORTED_FILE);
        Entry entry = new Entry(arrayData, file);


        assertEquals(arrayData, entry.getData());
        assertEquals(file, entry.getFile());
    }

    @Test
    public void constructorSecondTest() {

    }
}
