package com.petsoft.task1.base;

/**
 * 05.02.2019 22:48
 *
 * @author PetSoft
 */

public interface ExternalSortConstants { // TODO upper case + underscore
    long NO_ACTION = 0;
    long READY_TO_READ = 1;
    long READY_TO_MERGE_SORT = 2;
    long READY_TO_TWO_DATA_SORT = 3;
    long READY_TO_WRITE_TO_LARGE_FILE = 4;
    long READY_TO_WRITE = 5;
    long WROTE_IN_LARGE_FILE = 6;
}
