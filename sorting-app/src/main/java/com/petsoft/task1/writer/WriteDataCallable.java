package com.petsoft.task1.writer;

import com.petsoft.task1.base.Data;

import java.io.File;
import java.util.concurrent.Callable;

/**
 * 09.01.2019 18:16
 *
 * @author PetSoft
 */

public class WriteDataCallable implements Callable<File> {
    private Data data;
    private File file;

    public WriteDataCallable(Data data, File file) {
        this.data = data;
        this.file = file;
    }

    @Override
    public File call() throws Exception {
        new FileByteContentWriter().write(data, file);
        return null;
    }
}
