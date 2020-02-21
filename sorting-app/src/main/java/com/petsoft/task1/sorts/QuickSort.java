package com.petsoft.task1.sorts;

import com.petsoft.task1.base.Data;
import com.petsoft.task1.base.Sort;

/**
 * Created by PetSoft on 28.06.2019.
 */

public class QuickSort implements Sort {

    public void sort(Data data) {
        int startIndex = 0;
        int endIndex = data.length() - 1;
        doSort(startIndex, endIndex, data);
    }

    private void doSort(int low, int high, Data data) {
        if (low >= high)
            return;
        int i = low, j = high;
        int middle = i - (i - j) / 2;
        int middleValue = data.get(middle);
        while (i < j) {
            data.get(i);
            while (i < middle && (data.get(i) <= middleValue)) i++;
            while (j > middle && (middleValue <= data.get(j))) j--;
            if (i < j) {
                int temp = data.get(i);
                data.set(i, data.get(j));
                data.set(j, temp);
                if (i == middle) {
                    middle = j;
                } else if (j == middle) {
                    middle = i;
                }
            }
        }
        doSort(low, middle, data);
        doSort(middle + 1, high, data);
    }
}
