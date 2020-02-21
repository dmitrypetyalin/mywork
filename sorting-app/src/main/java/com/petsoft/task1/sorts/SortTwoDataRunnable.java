package com.petsoft.task1.sorts;

import com.google.common.util.concurrent.AtomicLongMap;
import com.petsoft.task1.base.Entry;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;

import static com.petsoft.task1.base.ExternalSortConstants.READY_TO_TWO_DATA_SORT;
import static com.petsoft.task1.base.ExternalSortConstants.READY_TO_WRITE;
import static com.petsoft.task1.main.ExternalSort.sortTwoData;

/**
 * 23.01.2019 21:08
 *
 * @author PetSoft
 */

public class SortTwoDataRunnable implements Runnable {
    private final static Logger LOG = LoggerFactory.getLogger(SortTwoDataRunnable.class);
    private BlockingQueue<Pair<Entry, Entry>> workingQueue;
    private AtomicLongMap<Integer> operationalMap;
    private int[] helper;
    private volatile boolean running;

    public SortTwoDataRunnable(BlockingQueue<Pair<Entry, Entry>> workingQueue, AtomicLongMap<Integer> operationalMap, int[] helper) {
        this.workingQueue = workingQueue;
        this.operationalMap = operationalMap;
        this.helper = helper;
    }

    public synchronized void stop() {
        this.running = false;
    }

    @Override
    public void run() {
        running = true;
        Pair<Entry, Entry> pair = null;
        Iterator<Pair<Entry, Entry>> iterator = workingQueue.iterator();
        while (running) {
            try {
                if (!iterator.hasNext()) {
                    try {
                        synchronized (workingQueue) {
                            workingQueue.wait();
                        }
                    } catch (InterruptedException e) {
                        this.running = false;
                    }
                    iterator = workingQueue.iterator();
                }
                pair = iterator.next();
            } catch (NoSuchElementException e) {
                LOG.info("No such elements in workingQueue");
            }
            Entry right = pair.getRight();
            Entry left = pair.getLeft();
            if (operationalMap.get(right.getIndex()) == READY_TO_TWO_DATA_SORT) {
                sortTwoData(left.getData(), right.getData(), helper);
                operationalMap.put(right.getIndex(), READY_TO_WRITE);
                synchronized (workingQueue) {
                    workingQueue.notify();
                }
            }
        }
    }
}
