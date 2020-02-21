package com.petsoft.task1.callable;

import com.petsoft.task1.base.Data;
import com.petsoft.task1.base.TwoDimensionalArrayData;
import com.petsoft.task1.base.Entry;
import com.petsoft.task1.writer.ContentWriter;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.concurrent.*;

import static com.petsoft.task1.main.ExternalSort.sortTwoData;

/**
 * 15.01.2019 12:41
 *
 * @author PetSoft
 */

public class PairConsumer implements Callable<Data> {
    private BlockingQueue<Pair<Entry, Entry>> queue;
    private ContentWriter contentWriter;
    private int blockSize;

    public PairConsumer(BlockingQueue<Pair<Entry, Entry>> queue, ContentWriter contentWriter, int blockSize) {
        this.queue = queue;
        this.contentWriter = contentWriter;
        this.blockSize = blockSize;
    }

    @Override
    public Data call() throws Exception {
        Pair<Entry, Entry> pair;
        Entry leftEntry = null;
        Entry rightEntry;
        File leftFile;
        File rightFile = null;
        Data leftData;
        Data rightData = null;
        int[] helper = new int[blockSize * 2];
        while ((pair = queue.poll(5l, TimeUnit.SECONDS)) != null) {
            leftFile = pair.getLeft().getFile();
            rightFile = pair.getRight().getFile();
            leftEntry = pair.getLeft();
            rightEntry = pair.getRight();
            leftData = leftEntry.getData();
            rightData = rightEntry.getData();
            sortTwoData(leftData, rightData, helper);
            if (queue.peek().getLeft().getFile() != leftFile) {
                contentWriter.write(leftData, leftFile);
            }
        }
        contentWriter.write(rightData, rightFile);
        return new TwoDimensionalArrayData();
    }
}
