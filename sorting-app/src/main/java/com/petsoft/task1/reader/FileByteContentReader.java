package com.petsoft.task1.reader;

import com.petsoft.task1.base.Data;
import com.petsoft.task1.base.TwoDimensionalArrayData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.petsoft.task1.util.ConvertUtils.byteArrToIntArr;
import static com.petsoft.task1.util.FileUtils.createFile;

/**
 * Created by PetSoft on 01.08.2019.
 */

public class FileByteContentReader implements ContentReader {
    private final static Logger LOG = LoggerFactory.getLogger(FileByteContentReader.class);
    private int blockSize;

    public FileByteContentReader(int blockSize) {
        if ((blockSize % 4 > 0) | (blockSize < 0)) {
            throw new IllegalArgumentException("Incorrect size ");
        }
        this.blockSize = blockSize;
    }

    public int getSize() {
        return blockSize;
    }

    public Data read(String fileName) throws IOException {
        return read(createFile(fileName));
    }

    //Read data from file in Data object
    public Data read(File file) throws IOException {
        int arrLength = blockSize / 4;
        int total = 0;
        ByteBuffer buffer = ByteBuffer.allocate(blockSize);
        try (FileInputStream fis = new FileInputStream(file)) {
            final FileChannel inChannel = fis.getChannel();
            total = inChannel.read(buffer);
        } catch (FileNotFoundException e) {
            LOG.info(e.getMessage());
        }

        return (total < blockSize ? new TwoDimensionalArrayData(byteArrToIntArr(buffer.array()), total / 4)
                : new TwoDimensionalArrayData(byteArrToIntArr(buffer.array()), arrLength));
    }
}
