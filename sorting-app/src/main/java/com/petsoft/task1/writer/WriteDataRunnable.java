package com.petsoft.task1.writer;

import com.google.common.util.concurrent.AtomicLongMap;
import com.petsoft.task1.base.Entry;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import static com.petsoft.task1.base.ExternalSortConstants.*;
import static com.petsoft.task1.util.FileUtils.writeFile;

/**
 * 19.01.2019 12:22
 *
 * @author PetSoft
 */

public class WriteDataRunnable implements Runnable {
    private final static Logger LOG = LoggerFactory.getLogger(WriteDataRunnable.class);
    private ContentWriter contentWriter;
    private BlockingQueue<Pair<Entry, Entry>> workingQueue;
    private AtomicLongMap<Integer> operationalMap;
    private File sortedLargeFile;
    private AtomicInteger sizeOfQueue;
    private int lastLeftElement;

    public WriteDataRunnable(BlockingQueue<Pair<Entry, Entry>> workingQueue, AtomicLongMap<Integer> operationalMap,
                             File sortedLargeFile, AtomicInteger sizeOfQueue, int listSize) {
        this.contentWriter = new FileByteContentWriter();
        this.workingQueue = workingQueue;
        this.operationalMap = operationalMap;
        this.sortedLargeFile = sortedLargeFile;
        this.sizeOfQueue = sizeOfQueue;
        this.lastLeftElement = listSize - 2;
    }

    @Override
    public void run() {
        boolean running = true;
        Pair<Entry, Entry> pair = null;
        Entry left;
        Entry right;
        while (workingQueue.size() < 2) {
            try {
                workingQueue.wait();
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        LOG.info("SizeOfQueue: " + sizeOfQueue);
        while (running && sizeOfQueue.getAndDecrement() > 0) {
            synchronized (this) {
                while ((pair = workingQueue.peek()) == null) {
                    try {
                        synchronized (workingQueue) {
                            workingQueue.wait();
                        }
                    } catch (InterruptedException e) {
                        running = false;
                    }
                }
                while (operationalMap.get(pair.getRight().getIndex()) != READY_TO_WRITE) {
                    try {
                        synchronized (workingQueue) {
                            workingQueue.wait();
                        }
                    } catch (InterruptedException e) {
                        LOG.info("Waiting for the next element");
                        running = false;
                    }
                }
                pair = workingQueue.poll();
            }
            left = pair.getLeft();
            right = pair.getRight();
            try {
                contentWriter.write(right.getData(), right.getFile());
                operationalMap.put(right.getIndex(), READY_TO_READ);
                synchronized (workingQueue) {
                    workingQueue.notifyAll();
                }
                LOG.info("Element: " + right.getIndex() + " : " + " was rewritten" +
                        " from pair: " + left.getIndex() + " " + right.getIndex() +
                        " status: " + operationalMap.get(right.getIndex()));
                LOG.info("Size of queue: " + workingQueue.size());
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            if (right.getReadyToWriteInLargeFile()) {
                operationalMap.put(left.getIndex(), WROTE_IN_LARGE_FILE);
                writeFile(sortedLargeFile, left.getData(), true);
                LOG.info("Element: " + left.getIndex() + " was written in largeFile from pair: " + left.getIndex() +
                        " " + right.getIndex() + " status of left pair: " + operationalMap.get(left.getIndex()));
            }
            if (left.getIndex() == lastLeftElement) {
                writeFile(sortedLargeFile, left.getData(), true);
                LOG.info("Element: " + left.getIndex() + " was written in largeFile from pair: " + left.getIndex() +
                        " " + right.getIndex() + " status of left pair: " + operationalMap.get(left.getIndex()));
                writeFile(sortedLargeFile, right.getData(), true);
                LOG.info("Element: " + right.getIndex() + " was written in largeFile from pair: " + left.getIndex() +
                        " " + right.getIndex() + " status of left pair: " + operationalMap.get(right.getIndex()));
            }
            LOG.info("Remains elements: " + sizeOfQueue.get());
        }
        LOG.info("WriteData has finished it's work!");
    }
}
