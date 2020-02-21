package com.petsoft.task1.util;

import com.petsoft.task1.base.Data;

/**
 * Created by PetSoft on 05.07.2019.
 */

public class SortUtils {
    //Check data array for sorted state
    public static boolean isSorted(Data data) {
        for (int i = 0; i < data.length() - 1; i++) {
            if (data.get(i) > data.get(i + 1)) {
                return false;
            }
        }
        return true;
    }

    //Swap two values between each other
    public static void exch(Data data, int i, int j) {
        int temp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, temp);
    }

    //Check on less value
    public static boolean less(Data data, int i, int j) {
        return data.get(i) < data.get(j);
    }

    public static boolean less(int i, int j) {
        return i < j;
    }

}
