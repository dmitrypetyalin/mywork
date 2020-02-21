package com.petsoft.task1.util;

import com.petsoft.task1.base.Data;
import com.petsoft.task1.base.TwoDimensionalArrayData;
import com.petsoft.task1.sorts.MergeSort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.petsoft.task1.base.PartitionInfo.*;
import static com.petsoft.task1.base.DataConstants.COL_COUNT;
import static com.petsoft.task1.base.DataConstants.ROW_COUNT;

/**
 * Created by PetSoft on 03.11.2019.
 */

public class FileUtils {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
    private static String fileSeparator = File.separator;
    public static final String UNSORTED_FILE = "D:" + fileSeparator + "Test" + fileSeparator + "unsortedFile    .txt";
    public static final String SORTED_FILE = "D:" + fileSeparator + "Test" + fileSeparator + "sortedFile.txt";
    public static final String TMP_FILE = "D:" + fileSeparator + "Test" + fileSeparator + "temp" + fileSeparator + "tmp";
    private static AtomicInteger partNumber = new AtomicInteger(1);

    //Создание нового файла, если такового не существует
    public static File createFile(String fileName) {
        File file = null;
        try {
            file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return file;
    }

    public static List<File> divideByteFile(int blockSize) {
        File file = createFile(UNSORTED_FILE);
        List<File> list = new LinkedList<>();
        byte[] bytes = new byte[blockSize];
        int total = 0;
        try (FileInputStream fis = new FileInputStream(file)) {
            while ((total = fis.read(bytes, 0, blockSize)) >= 0) {
                String fileName = (TMP_FILE + partNumber.toString() + ".txt");
                list.add(total == blockSize ? writeByteFile(fileName, bytes) :
                        writeByteFile(fileName, bytes, total));
                partNumber.incrementAndGet();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return list;
    }

    //Load and sorts parts of big file and writing these parts on the disk
    public static List<File> divideFile() {
        List<File> files = new ArrayList<>();
        int[][] array = new int[Q_ARRAYS][];
        array[0] = new int[Q_ELEMENTS];
        int total = 0;
        int i = 0;
        int j = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(UNSORTED_FILE))) {
            int number;
            String digit = "";
            while ((number = reader.read()) != -1) {
                try {
                    if (number == 32) {
                        if (j >= Q_ELEMENTS && i < Q_ARRAYS - 1) {
                            i++;
                            j = 0;
                            array[i] = new int[Q_ELEMENTS];
                        }
                        array[i][j++] = Integer.parseInt(digit);
                        total++;
                        digit = "";
                        if (j >= Q_ELEMENTS && i >= Q_ARRAYS - 1) {
                            j = 0;
                            i = 0;
                            files.add(createSortedPartFile(TMP_FILE + partNumber.toString() + ".txt",
                                    new TwoDimensionalArrayData(array, total)));
                            total = 0;
                            partNumber.incrementAndGet();
                        }
                    } else {
                        char c = (char) number;
                        digit += c;
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            if (j > 0) {
                files.add(createSortedPartFile(TMP_FILE + partNumber.toString() + ".txt",
                        new TwoDimensionalArrayData(array, total)));
                partNumber.incrementAndGet();
            }
            partNumber.decrementAndGet();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return files;
    }

    private static File writeByteFile(String fileName, byte[] bytes, int sizeBlock) {
        File file = createFile(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes, 0, sizeBlock);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return file;
    }

    private static File writeByteFile(String fileName, byte[] bytes) {
        File file = createFile(fileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return file;
    }

    //Creating if there aren't and writing files Создание, если таковго не было, и запись файла
    public static File writeFile(String fileName, Data data) {
        File file = createFile(fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (int i = 0; i < data.length(); i++) {
                writer.write(Integer.toString(data.get(i)) + " ");
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return file;
    }

    //Rewrite existing file
    public static void writeFile(File file, Data data, boolean isAppend) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, isAppend))) {
            for (int i = 0; i < data.length(); i++) {
                writer.write(Integer.toString(data.get(i)) + " ");
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    public static void writeFile(File file, Data data) {
        writeFile(file, data, false);
    }

    public static void writeFile(File file, int[] array, int arrayLength) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (int i = 0; i < arrayLength; i++) {
                writer.write(Integer.toString(array[i]) + " ");
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    //Creating sorted files specified size
    private static File createSortedPartFile(String filename, Data data) {
        new MergeSort().sort(data);
        return writeFile(filename, data);
    }

    //Conversion array of strings to array of numbers
    private static int[] parseInts(String[] arr) {
        int[] intArr = new int[arr.length];
        for (int i = 0; i < intArr.length; i++) {
            try {
                intArr[i] = Integer.parseInt(arr[i]);
            } catch (NumberFormatException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return intArr;
    }

    //Load file into two dimensional array
    public static Data getContent(File file) {
        int[][] array = new int[ROW_COUNT][];
        array[0] = new int[COL_COUNT];
        int total = 0;
        int i = 0;
        int j = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                int[] arr = parseInts(line.split(" "));
                for (int k = 0; k < arr.length; k++) {
                    if (j >= COL_COUNT) {
                        i++;
                        j = 0;
                        array[i] = new int[COL_COUNT];
                    }
                    array[i][j++] = arr[k];
                    total++;
                }
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        return new TwoDimensionalArrayData(array, total);
    }
}
