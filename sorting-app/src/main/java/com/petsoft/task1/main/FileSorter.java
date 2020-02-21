package com.petsoft.task1.main;

import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.petsoft.task1.base.PartitionInfo.BLOCK_SIZE;

/**
 * Created by PetSoft on 28.10.2019.
 */

public class FileSorter {
    private static final Logger LOG = LoggerFactory.getLogger(FileSorter.class);

    public static void main(String[] args) {
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        stopwatch.start();
        new ExternalSort().doExternalSort(BLOCK_SIZE);
        stopwatch.stop();
        long spentTime = stopwatch.elapsed(TimeUnit.SECONDS);
        LOG.info("Elapsed time is: " + spentTime + " seconds");
    }
}
