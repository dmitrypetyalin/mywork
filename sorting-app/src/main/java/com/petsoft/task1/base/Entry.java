package com.petsoft.task1.base;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 15.01.2019 12:42
 *
 * @author PetSoft
 */
public class Entry {
    private Data data;
    private File file;
    private final int index;
    private AtomicBoolean processing;
    private AtomicBoolean readyToWriteInLargeFile;

    public Entry(Data data, File file, int index, boolean processing) {
        this.data = data;
        this.file = file;
        this.index = index;
        this.processing = new AtomicBoolean(processing);
        this.readyToWriteInLargeFile = new AtomicBoolean(false);
    }

    public Entry(Data data, File file) {
        this.data = data;
        this.file = file;
        this.index = 0;
        this.readyToWriteInLargeFile = new AtomicBoolean(false);
    }

    public Data getData() { return data; }

    public File getFile() {
        return file;
    }

    public int getIndex() {
        return index;
    }

    public boolean tryToProcess() {
        return processing.compareAndSet(false, true);
    }

    public boolean getReadyToWriteInLargeFile() {
        return readyToWriteInLargeFile.get();
    }

    public void setReadyToWriteInLargeFile(boolean value) {
        this.readyToWriteInLargeFile.set(value);
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setProcessing(boolean processing) {
        this.processing.set(processing);
    }
}
