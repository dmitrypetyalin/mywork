package com.petsoft.task1.writer;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.petsoft.task1.base.Data;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.petsoft.task1.util.ConvertUtils.dataToByteArr;
import static com.petsoft.task1.util.FileUtils.createFile;

/**
 * Created by PetSoft on 01.08.2019.
 */

public class FileByteContentWriter implements ContentWriter {
    private static final Logger LOG = LoggerFactory.getLogger(FileByteContentWriter.class);

    public FileByteContentWriter() {
    }

    public File write(Data data, File file) throws IOException {
        byte[] byteArr = dataToByteArr(data);
        ByteBuffer buffer = ByteBuffer.allocate(byteArr.length);
        FileChannel fileChannel = null;
        buffer.put(byteArr);
        buffer.flip();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fileChannel = fos.getChannel();
            fileChannel.write(buffer);
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            try {
                assert fileChannel != null;
                fileChannel.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return file;
    }

    public File write(Data data, String fileName) throws IOException {
        return write(data, createFile(fileName));
    }
}
