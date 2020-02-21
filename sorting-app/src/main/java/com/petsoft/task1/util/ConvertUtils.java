package com.petsoft.task1.util;

import com.petsoft.task1.base.Data;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by PetSoft on 02.01.2019.
 */

public class ConvertUtils {
    private static ThreadLocal<byte[]> bufferThreadLocal = new ThreadLocal<>();

    public static byte[] dataToByteArr(Data data) {
        int length = data.length() * 4;
        byte[] byteArr = new byte[length];
        int num;
        int j = 0;
        for (int i = 0; i < data.length(); i++) {
            num = data.get(i);
            byteArr[j++] = (byte) (num >> 24);
            byteArr[j++] = (byte) (num >> 16);
            byteArr[j++] = (byte) (num >> 8);
            byteArr[j++] = (byte) num;
        }
        return byteArr;
    }

    public static int[] byteArrToIntArr(byte[] bytes) {
        int lengthByteArr = bytes.length;
        if ((lengthByteArr % 4 > 0) || (lengthByteArr < 0)) {
            throw new IllegalArgumentException("byteArr !!!!!");
        }
        int lengthIntArr = lengthByteArr / 4;
        int[] arr = new int[lengthIntArr];
        int j = 0;
        for (int i = 0; i < bytes.length; ) {
            int num = (bytes[i++]) | (bytes[i++] << 8) | (bytes[i++] << 16) | (bytes[i++] << 32);
            arr[j++] = num;
        }
        return arr;
    }

    public static int parseBytesToInt(byte[] bytes, int offset) {
        int num = 0;
        if ((offset + 4) < bytes.length) {
            num = (bytes[offset++] << 0) | (bytes[offset++] << 8) | (bytes[offset++] << 16) | (bytes[offset] << 32);
        }
        return num;
    }

    public static int parseBytesToInt(byte[] bytes) {
        return parseBytesToInt(bytes, 0);
    }

    public static byte[] parseIntToBytes(int n) { // TODO optimize using ThreadLocal
        byte[] buf = bufferThreadLocal.get();
        if (buf == null) {
            bufferThreadLocal.set(buf = new byte[4]);
        }
        int i = 0;
        buf[i++] = (byte) (n >> 24);
        buf[i++] = (byte) (n >> 16);
        buf[i++] = (byte) (n >> 8);
        buf[i] = (byte) n;
        return buf;
    }

    public static int readInt(InputStream in) throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4 << 0));
    }
}