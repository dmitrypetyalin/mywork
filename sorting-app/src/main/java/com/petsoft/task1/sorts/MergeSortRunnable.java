package com.petsoft.task1.sorts;

import com.google.common.util.concurrent.AtomicLongMap;
import com.petsoft.task1.base.Entry;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;

import static com.petsoft.task1.base.ExternalSortConstants.READY_TO_MERGE_SORT;
import static com.petsoft.task1.base.ExternalSortConstants.READY_TO_TWO_DATA_SORT;

/**
 * 22.01.2019 15:02
 *
 * @author PetSoft
 */

public class MergeSortRunnable implements Runnable {
    private final static Logger LOG = LoggerFactory.getLogger(MergeSortRunnable.class);
    private BlockingQueue<Pair<Entry, Entry>> workingQueue;
    private AtomicLongMap<Integer> operationalMap;
    private boolean running = true;

    public MergeSortRunnable(BlockingQueue<Pair<Entry, Entry>> queue, AtomicLongMap<Integer> operationalMap) {
        this.workingQueue = queue;
        this.operationalMap = operationalMap;
    }

    @Override
    public void run() {
        MergeSort mergeSort = new MergeSort();
        Pair<Entry, Entry> pair = null;

        while (workingQueue.size() < 2) {
            try {
                synchronized (workingQueue) {
                    workingQueue.wait();
                }
            } catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        Iterator<Pair<Entry, Entry>> iterator = workingQueue.iterator();
        while (running && !workingQueue.isEmpty()) {
            try {
                if (!iterator.hasNext()) {
                    try {
                        synchronized (workingQueue) {
                            workingQueue.wait();
                        }
                    } catch (InterruptedException e) {
                        running = false;
                    }
                    iterator = workingQueue.iterator();
                }
                pair = iterator.next();
            } catch (NoSuchElementException e) {
                LOG.error(e.getMessage(), e);
            }

            if (pair.getLeft().getIndex() > 0) {
                running = false;
                break;
            }

            Entry right = pair.getRight();
            if (operationalMap.get(right.getIndex()) == READY_TO_MERGE_SORT && right.tryToProcess()) {
                if (right.getIndex() == 1) {
                    mergeSort.sort(pair.getLeft().getData());
                    operationalMap.put(pair.getLeft().getIndex(), READY_TO_TWO_DATA_SORT);
                }
                mergeSort.sort(right.getData());
                operationalMap.put(right.getIndex(), READY_TO_TWO_DATA_SORT);
                synchronized (workingQueue) {
                    workingQueue.notify();
                }
            }
        }
    }
}
