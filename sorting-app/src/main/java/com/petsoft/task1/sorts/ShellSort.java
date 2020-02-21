package com.petsoft.task1.sorts;

import com.petsoft.task1.base.Data;
import com.petsoft.task1.base.Sort;

import static com.petsoft.task1.util.SortUtils.exch;
import static com.petsoft.task1.util.SortUtils.less;

/**
 * Created by PetSoft on 06.07.2019.
 */

public class ShellSort implements Sort {
    @Override
    public void sort(Data data) {
        int length = data.length();
        int h = 1;
        while (h < length / 3) {
            h = h * 3 + 1;
        }
        while (h >= 1) {
            for (int i = h; i < length; i++) {
                for (int j = i; j >= h && less(data, j, j - h); j -= h) {
                    exch(data, j, j - h);
                }
            }
            h = h / 3;
        }
    }
}
