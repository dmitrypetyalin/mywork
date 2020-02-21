package com.petsoft.task1.main;

import com.google.common.util.concurrent.AtomicLongMap;
import com.petsoft.task1.callable.PairConsumer;
import com.petsoft.task1.callable.PairProducer;
import com.petsoft.task1.base.Data;
import com.petsoft.task1.base.Entry;
import com.petsoft.task1.reader.FileByteContentReader;
import com.petsoft.task1.reader.ReadDataCallable;
import com.petsoft.task1.reader.ReadDataRunnable;
import com.petsoft.task1.sorts.MergeSort;
import com.petsoft.task1.sorts.MergeSortRunnable;
import com.petsoft.task1.sorts.SortTwoDataRunnable;
import com.petsoft.task1.writer.FileByteContentWriter;
import com.petsoft.task1.writer.WriteDataCallable;
import com.petsoft.task1.writer.WriteDataRunnable;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.petsoft.task1.base.ExternalSortConstants.READY_TO_READ;
import static com.petsoft.task1.base.PartitionInfo.CONTENT_TWO_FILES;
import static com.petsoft.task1.util.FileUtils.*;

/**
 * Created by PetSoft on 16.08.2019.
 * <p>
 * 1 - ready to read
 * 2 - ready to first sort
 * 3 - ready to sort
 * 4 - ready to write
 */

public class ExternalSort {

    private final static Logger LOG = LoggerFactory.getLogger(ExternalSort.class);

    void doExternalSort(int blockSize) {
        try {
            sortFilesByMap(blockSize);
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void sortFilesByMap(int blockSize) throws InterruptedException {
        FileByteContentReader contentReader = new FileByteContentReader(blockSize);
        File sortedLargeFile = createFile(SORTED_FILE);
        int[] helper = new int[blockSize * 2];
        List<File> files = divideByteFile(blockSize);
        AtomicInteger sizeOfQueue = new AtomicInteger(sizeOfQueue(files.size()));
        BlockingQueue<Pair<Entry, Entry>> taskQueue = new LinkedBlockingQueue<>(sizeOfQueue(sizeOfQueue.get()));
        BlockingQueue<Pair<Entry, Entry>> queue = new LinkedBlockingQueue<>(10);
        AtomicLongMap<Integer> operationalMap = AtomicLongMap.create();
        for (int i = 0; i < files.size() - 1; i++) {
            operationalMap.put(i, READY_TO_READ);
            if(i == files.size() - 2)
                operationalMap.put(files.size() - 1, READY_TO_READ);
            Entry entry = new Entry(null, files.get(i), i, false);
            for (int j = i + 1; j < files.size(); j++) {
                taskQueue.put(new MutablePair<>(entry,
                        new Entry(null, files.get(j), j, false)));
            }
        }
        new Thread(new ReadDataRunnable(contentReader, taskQueue, queue, operationalMap), "ReadData").start();
        for (int i = 0; i < 5; i++) {
            new Thread(new MergeSortRunnable(queue, operationalMap), "MergeSort" + i).start();
        }
        SortTwoDataRunnable sortTwoDataRunnable = new SortTwoDataRunnable(queue, operationalMap, helper);
        Thread thirdThread = new Thread(sortTwoDataRunnable, "SortTwoData");
        Thread fourthThread = new Thread(new WriteDataRunnable(queue, operationalMap, sortedLargeFile, sizeOfQueue, files.size()), "WriteData1");
        Thread fifthThread = new Thread(new WriteDataRunnable(queue, operationalMap, sortedLargeFile, sizeOfQueue, files.size()), "WriteData2");
        thirdThread.start();
        fourthThread.start();
        fifthThread.start();
        fourthThread.join();
        fifthThread.join();
        sortTwoDataRunnable.stop();
        thirdThread.interrupt();
        fourthThread.interrupt();
        fifthThread.interrupt();
        System.gc();
        files.forEach(File::delete);
    }

    private void sortFiles(int blockSize) throws InterruptedException, ExecutionException {
        FileByteContentReader contentReader = new FileByteContentReader(blockSize);
        FileByteContentWriter contentWriter = new FileByteContentWriter();
        File sortedLargeFile = createFile(SORTED_FILE);
        File firstFile = null;
        File secondFile = null;
        Data tmpData = null;
        Data data = null;
        int[] helper = new int[blockSize * 2];

        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(4, 128,
                1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

        Future<Data> futureData = null;
        Future<Data> futureTmpData = null;
        BlockingQueue<Runnable> queue = poolExecutor.getQueue();
        List<Runnable> list = new ArrayList<Runnable>();

//        LinkedBlockingQueue<Future<TwoDimensionalArrayData>> queue = new LinkedBlockingQueue<>();

        List<File> files = divideByteFile(blockSize);

        // TODO Stack<Pair>, Pair
        for (int i = 0; i < files.size() - 1; i++) {
            firstFile = files.get(i);
            try {
                if (i == 0) {
                    futureTmpData = poolExecutor.submit(new ReadDataCallable(files.get(1), contentReader));
                    data = contentReader.read(firstFile);
                } else {
                    data = futureData == null ? null : futureData.get();
                }
                if (i < 1) {
                    new MergeSort().sort(data);
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }

            if (i < files.size() - 2) {
                futureData = poolExecutor.submit(new ReadDataCallable(files.get(i + 1), contentReader));
            }

            for (int j = i + 1; j < files.size(); j++) {
                secondFile = files.get(j);
                tmpData = futureTmpData.get();

                if (j < files.size() - 1) {
                    futureTmpData = poolExecutor.submit(new ReadDataCallable(files.get(j + 1), contentReader));
                }

                if (i < 1) {
                    new MergeSort().sort(tmpData);
                }
                sortTwoData(data, tmpData, helper);

//                    contentWriter.write(tmpData, secondFile);
                poolExecutor.submit(new WriteDataCallable(tmpData, secondFile));
            }

            writeFile(sortedLargeFile, data, true);

            try {
                contentWriter.write(data, firstFile); // write for info reason
                poolExecutor.submit(new WriteDataCallable(data, firstFile));
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    public void sortFilesByPair(int blockSize) {
        FileByteContentReader contentReader = new FileByteContentReader(blockSize);
        FileByteContentWriter contentWriter = new FileByteContentWriter();
        File sortedLargeFile = createFile(SORTED_FILE);

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        BlockingQueue<Pair<Entry, Entry>> queue = new LinkedBlockingQueue<>(3);
        List<File> files = divideByteFile(blockSize);

        try {
            executor.submit((Runnable) new PairProducer(queue, contentReader, files).call());
            executor.submit((Runnable) new PairConsumer(queue, contentWriter, blockSize).call());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    private void sortByteFiles(List<File> files, int blockSize) {
        FileByteContentReader contentReader = new FileByteContentReader(blockSize);
        FileByteContentWriter contentWriter = new FileByteContentWriter();
//        File sortedByteLargeFile = createFile(SORTED_BYTE_LARGE_FILE);
        File sortedLargeFile = createFile(SORTED_FILE); // TODO File.separator
        File firstFile = null;
        File secondFile = null;
        Data tmpData = null;
        Data data = null;
        int[] helper = new int[blockSize * 2];
        for (int i = 0; i < files.size() - 1; i++) {
            firstFile = files.get(i);
            try {
                data = contentReader.read(firstFile);
                if (i < 1) {
                    new MergeSort().sort(data);
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            for (int j = i + 1; j < files.size(); j++) {
                secondFile = files.get(j);
                try {
                    tmpData = contentReader.read(secondFile);
                    if (i < 1) {
                        new MergeSort().sort(tmpData);
                    }
                    sortTwoData(data, tmpData, helper);
                    contentWriter.write(tmpData, secondFile);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            writeFile(sortedLargeFile, data, true);
            try {
                contentWriter.write(data, firstFile);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    public static void sortTwoData(Data data1, Data data2, int[] helper) {
        // TODO optimize redundant exchange in memory
        int d1Length = data1.length();
        int d2Length = data2.length();
        int totalLength = d1Length + d2Length;
        int index1 = 0;
        int index2 = 0;
        int i = 0;
        while (index1 < d1Length && index2 < d2Length) {
            int value1 = data1.get(index1);
            int value2 = data2.get(index2);
            if (value1 < value2) {
                helper[i] = value1;
                index1++;
            } else {
                helper[i] = value2;
                index2++;
            }
            i++;
        }
        while (index1 < d1Length) {
            helper[i] = data1.get(index1);
            index1++;
            i++;
        }
        while (index2 < d2Length) {
            helper[i] = data2.get(index2);
            index2++;
            i++;
        }
        for (int k = 0; k < d1Length; k++) {
            data1.set(k, helper[k]);
        }
        int j = 0;
        for (int k = d1Length; k < totalLength; k++) {
            data2.set(j++, helper[k]);
        }
    }

    //Merge array of sorted files
    public void sortFiles(List<File> files, int blockSize) {
        FileByteContentReader reader = new FileByteContentReader(blockSize);
        FileByteContentWriter writer = new FileByteContentWriter();
        File sortedLargeFile = createFile(SORTED_FILE);
        int[] array = new int[CONTENT_TWO_FILES];
        Data tmpData = null;
        Data data = null;
        File tmpFile;
        File file;
        for (int i = 0; i < files.size() - 1; i++) {
            file = files.get(i);
            try {
                data = reader.read(file);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            for (int j = i + 1; j < files.size(); j++) {
                tmpFile = files.get(j);
                try {
                    tmpData = reader.read(tmpFile);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
                sortTwoData(data, tmpData, array);
                try {
                    writer.write(tmpData, tmpFile);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            try {
                writer.write(data, sortedLargeFile);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            if (i == files.size() - 2) {
                try {
                    writer.write(tmpData, sortedLargeFile);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
//            file.delete();
        }
    }

    private static int sizeOfQueue(int n) {
        n = n - 1;
        if (n == 1) {
            return 1;
        }
        return (n + sizeOfQueue(n));
    }
}
