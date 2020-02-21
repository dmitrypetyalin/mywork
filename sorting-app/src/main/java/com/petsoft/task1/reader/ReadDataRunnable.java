package com.petsoft.task1.reader;

import com.google.common.util.concurrent.AtomicLongMap;
import com.petsoft.task1.base.Data;
import com.petsoft.task1.base.Entry;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import static com.petsoft.task1.base.ExternalSortConstants.*;

/**
 * 19.01.2019 12:20
 *
 * @author PetSoft
 */

public class ReadDataRunnable implements Runnable {
    private final static Logger LOG = LoggerFactory.getLogger(ReadDataRunnable.class);
    private ContentReader contentReader;
    private BlockingQueue<Pair<Entry, Entry>> taskQueue;
    private BlockingQueue<Pair<Entry, Entry>> workingQueue;
    private AtomicLongMap<Integer> operationalMap;

    public ReadDataRunnable(ContentReader contentReader, BlockingQueue<Pair<Entry, Entry>> taskQueue,
                            BlockingQueue<Pair<Entry, Entry>> queue, AtomicLongMap<Integer> operationalMap) {
        this.contentReader = contentReader;
        this.taskQueue = taskQueue;
        this.workingQueue = queue;
        this.operationalMap = operationalMap;
    }

    @Override
    public void run() {
        Pair<Entry, Entry> pair;
        Pair<Entry, Entry> prevPair = null;
        Data leftData = null;
        boolean running = true;
        while (running && (pair = taskQueue.poll()) != null) {
            Entry left = pair.getLeft();
            Entry right = pair.getRight();
            File leftFile = left.getFile();
            if (prevPair == null || prevPair.getLeft().getFile() != leftFile) {
                try {
                    leftData = contentReader.read(leftFile);
                    left.setData(leftData);
                    if (prevPair != null) {
                        prevPair.getRight().setReadyToWriteInLargeFile(true);
                    }
                    operationalMap.put(left.getIndex(), left.getIndex() == 0 ? READY_TO_MERGE_SORT : READY_TO_TWO_DATA_SORT);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            } else {
                left.setData(leftData);
            }
            while (running && operationalMap.get(right.getIndex()) != READY_TO_READ) {
                try {
                    synchronized (workingQueue) {
                        workingQueue.wait();
                    }
                } catch (InterruptedException e) {
                    running = false;
                }
            }
            try {
                pair.getRight().setData(contentReader.read(right.getFile()));
                operationalMap.put(right.getIndex(), left.getIndex() == 0 ? READY_TO_MERGE_SORT : READY_TO_TWO_DATA_SORT);
                LOG.info("Pair: " + left.getIndex() + " " + right.getIndex() + ", status: " +
                        operationalMap.get(left.getIndex()) + "|" + operationalMap.get(right.getIndex()) + " was read");
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            try {
                workingQueue.put(pair);
                synchronized (workingQueue) {
                    workingQueue.notifyAll();
                }
            } catch (InterruptedException e) {
                running = false;
            }
            prevPair = pair;
        }
        LOG.info("ReadData has finished it's work!");
    }
}
