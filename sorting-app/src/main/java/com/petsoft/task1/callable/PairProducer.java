package com.petsoft.task1.callable;

import com.petsoft.task1.base.Data;
import com.petsoft.task1.base.Entry;
import com.petsoft.task1.reader.ContentReader;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.List;
import java.util.concurrent.*;

/**
 * 15.01.2019 11:50
 *
 * @author PetSoft
 */

public class PairProducer implements Callable<Pair<Entry, Entry>> {
    private BlockingQueue<Pair<Entry, Entry>> queue;
    private ContentReader contentReader;
    List<File> files;

    public PairProducer(BlockingQueue<Pair<Entry, Entry>> queue, ContentReader contentReader, List<File> files) {
        this.queue = queue;
        this.contentReader = contentReader;
        this.files = files;
    }

    @Override
    public Pair<Entry, Entry> call() throws Exception {
        Pair<Entry, Entry> pair;
        File firstFile;
        File secondFile;
        Data leftData;
        Data rightData;
        for (int i = 0; i < files.size() - 1; i++) {
            firstFile = files.get(i);
            leftData = contentReader.read(firstFile);
            for (int j = 1; j < files.size(); j++) {
                secondFile = files.get(j);
                rightData = contentReader.read(secondFile);
                queue.put(new MutablePair<>(new Entry(leftData, firstFile), new Entry(rightData, secondFile)));
            }
        }
        return new MutablePair<>();
    }
}
