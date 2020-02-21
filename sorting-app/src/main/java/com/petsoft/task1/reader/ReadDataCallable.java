package com.petsoft.task1.reader;

import com.petsoft.task1.base.Data;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * 09.01.2018 18:06
 *
 * @author PetSoft
 */

public class ReadDataCallable implements Callable<Data> {
    private File file;
    private FileByteContentReader contentReader;

    public ReadDataCallable(File file, FileByteContentReader contentReader) {
        this.file = file;
        this.contentReader = contentReader;
    }

    @Override
    public Data call() throws Exception {
        return contentReader.read(file);
    }
}
