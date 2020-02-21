package com.petsoft.task1.base;

import com.petsoft.task1.exceptions.DataOverloadException;

import static com.petsoft.task1.base.DataConstants.COL_COUNT;
import static com.petsoft.task1.base.DataConstants.ROW_COUNT;

/**
 * Created by PetSoft on 27.06.2019.
 */

public class TwoDimensionalArrayData implements Data {
    private int[][] array;
    private int length;

    public TwoDimensionalArrayData() {
    }

    public TwoDimensionalArrayData(int[][] array, int length) {
        this.array = array;
        this.length = length;
    }

    public TwoDimensionalArrayData(int[] array, int arrLength) throws DataOverloadException {
        int[][] arr2D = new int[ROW_COUNT][];
        arr2D[0] = new int[COL_COUNT];
        int i = 0;
        int j = 0;
        for (int k = 0; k < arrLength; k++) {
            if (i < ROW_COUNT && j < COL_COUNT) {
                arr2D[i][j] = array[k];
                j++;
            } else if (j == COL_COUNT) {
                j = 0;
                arr2D[++i] = new int[COL_COUNT];
                arr2D[i][j] = array[k];
            } else if (i == ROW_COUNT) {
                throw new DataOverloadException();
            }
        }
        this.array = arr2D;
        this.length = arrLength;
    }

    public int get(int i) {
        int row = i / COL_COUNT;
        int col = i % COL_COUNT;
        return array[row][col];
    }

    public void set(int i, int value) {
        int row = i / COL_COUNT;
        int col = i % COL_COUNT;
        this.array[row][col] = value;
    }

    public int length() {
        return length;
    }
}
