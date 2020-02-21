package com.petsoft.task1.base;

/**
 * Created by PetSoft on 30.06.2019
 */

public class OneDimensionalArrayData implements Data {
    private int[] array;
    private int length;

    public OneDimensionalArrayData() {
    }

    public OneDimensionalArrayData(int[] array, int length) {
        this.array = array;
        this.length = length;
    }

    @Override
    public int get(int i) {
        return array[i];
    }

    @Override
    public void set(int i, int value) {
        array[i] = value;
    }

    @Override
    public int length() {
        return length;
    }
}
