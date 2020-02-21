package com.petsoft.task1.sorts;

import com.petsoft.task1.base.Data;
import com.petsoft.task1.base.Sort;

import static com.petsoft.task1.util.SortUtils.exch;
import static com.petsoft.task1.util.SortUtils.less;

/**
 * Created by PetSoft on 03.11.2019.
 */

public class BubbleSort implements Sort {
    @Override
    public void sort(Data data) {
        int length = data.length() - 1;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length - i; j++) {
                if (less(data, j + 1, j)) {
                    exch(data, j, j + 1);
                }
            }
        }
    }
}
