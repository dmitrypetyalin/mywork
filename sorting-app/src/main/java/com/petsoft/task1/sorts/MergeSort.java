package com.petsoft.task1.sorts;

import com.petsoft.task1.base.Data;
import com.petsoft.task1.base.Sort;

/**
 * Created by PetSoft on 28.06.2019.
 */

public class MergeSort implements Sort {
    private Data data;
    private int[] helper = null;

    public MergeSort() {
    }

    public MergeSort(Data data) {
        this.data = data;
    }

    public MergeSort(Data data, int[] helper) {
        this.data = data;
        this.helper = helper;
    }

    public Data getSortData() {
        sort(data);
        return data;
    }

    @Override
    public void sort(Data data) {
        this.data = data;
        int length = data.length();
        if (this.helper == null) {
            this.helper = new int[length];
        }
        merge2dSort(0, length - 1);
    }

    private void merge2dSort(int low, int high) {
        if (low < high) {
            int middle = low + (high - low) / 2;
            merge2dSort(low, middle);
            merge2dSort(middle + 1, high);
            merge(low, middle, high);
        }
    }

    private void merge(int low, int middle, int high) {
        for (int i = low; i <= high; i++) {
            helper[i] = data.get(i);
        }
        int i = low;
        int j = middle + 1;
        int k = low;
        while (i <= middle && j <= high) {
            if (helper[i] <= helper[j]) {
                data.set(k, helper[i++]);
            } else {
                data.set(k, helper[j++]);
            }
            k++;
        }
        while (i <= middle) {
            data.set(k, helper[i]);
            k++;
            i++;
        }
    }

    private void merge2(int low, int middle, int high) {
        int i = low;
        int j = middle + 1;
        for (int k = low; k <= high; k++) {
            helper[k] = data.get(k);
        }
        for (int k = low; k <= high; k++) {
            if (i < middle) data.set(k, helper[j++]);
            else if (j > high) data.set(k, helper[i++]);
            else if (helper[j] < helper[i]) data.set(k, helper[j++]);
            else data.set(k, helper[i++]);
        }
    }
}
