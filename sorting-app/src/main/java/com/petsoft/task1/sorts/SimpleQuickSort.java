package com.petsoft.task1.sorts;

/**
 * Created by PetSoft on 03.11.2019.
 */

public class SimpleQuickSort {
    private int[] array;

    public int[] sort(int[] array) {
        this.array = array;
        int startIndex = 0;
        int endIndex = array.length - 1;
        doSort(startIndex, endIndex);
        return this.array;
    }

    private void doSort(int low, int high) {
        if (low >= high) {
            return;
        }
        int i = low, j = high;
        int middle = i - (i - j) / 2;
        int middleValue = array[middle];
        while (i < j) {
            while (i < middle && (array[i] <= middleValue)) i++;
            while (j > middle && (middleValue <= array[j])) j--;
            if (i < j) {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                if (i == middle) {
                    middle = j;
                } else if (j == middle) {
                    middle = i;
                }
            }
        }
        doSort(low, middle);
        doSort(middle + 1, high);
    }
}
