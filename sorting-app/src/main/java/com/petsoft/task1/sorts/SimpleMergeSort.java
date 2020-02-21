package com.petsoft.task1.sorts;

/**
 * Created by PetSoft on 03.11.2019.
 */

public class SimpleMergeSort {
    private int[] array;
    private int[] helper;

    public int[] sort(int[] array) {
        this.array = array;
        int length = array.length;
        this.helper = new int[length];
        mergeSort(0, length - 1);
        return this.array;
    }

    private void mergeSort(int low, int high) {
        if (low < high) {
            int middle = low + (high - low) / 2;
            mergeSort(low, middle);
            mergeSort(middle + 1, high);
            merge(low, middle, high);
        }
    }

    private void merge(int low, int middle, int high) {
        for (int i = low; i <= high; i++) {
            helper[i] = array[i];
        }
        int i = low;
        int j = middle + 1;
        int k = low;
        while (i <= middle && j <= high) {
            if (helper[i] <= helper[j]) {
                array[k] = helper[i];
                i++;
            } else {
                array[k] = helper[j];
                j++;
            }
            k++;
        }
        while (i <= middle) {
            array[k] = helper[i];
            k++;
            i++;
        }
    }
}
