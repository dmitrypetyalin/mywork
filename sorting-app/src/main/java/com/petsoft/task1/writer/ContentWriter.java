package com.petsoft.task1.writer;

import com.petsoft.task1.base.Data;

import java.io.File;
import java.io.IOException;

/**
 * Created by PetSoft on 01.08.2019.
 */

@FunctionalInterface
public interface ContentWriter {
    File write(Data data, File file) throws IOException;
}
