package com.petsoft.task1.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static com.petsoft.task1.util.ConvertUtils.parseIntToBytes;
import static com.petsoft.task1.util.FileUtils.*;

/**
 * Created by PetSoft on 28.10.2019.
 */

public class FileGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(FileGenerator.class);

    public static void main(String[] args) {
        FileGenerator fileGenerator = new FileGenerator();
        fileGenerator.generateFile(UNSORTED_FILE, 1024); //size 1 = 1mB
    }

    /**
     * create file and fills in the file by integers
     *
     * @param size size is considered in megabytes
     *             Maximal size of file 2047 terabytes
     */
    public File generateFile(String fileName, int size) {
        File file = createFile(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            for (int i = 0; i < size; i++) {
                writeByteFile(generateOneMegaByte(), fos);
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        return file;
    }

    /**
     * 1 megaByte = 1024 kByte= 1048567 Byte
     * 1 megaByte = 262144 integers
     */
    private int[] generateOneMegaByte() {
        int[] array = new int[262144];
        for (int i = 0; i < array.length; i++) {
            array[i] = generateIntNumber();
        }
        return array;
    }

    //Generation single integer
    private int generateIntNumber() {
        Random random = new Random();
        return random.nextInt(100);
    }

    //Writing byte file
    private void writeByteFile(int[] array, FileOutputStream fos) {
        try {
            for (int i = 0; i < array.length; i++) {
                fos.write(parseIntToBytes(array[i]));
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
