package com.tms.api.utils;

public class MathUtils {

    public static int[] add(int[] first, int[] second) {
        int length = Math.min(first.length, second.length);
        int[] result = new int[length];

        for (int i = 0; i < length; i++) {
            result[i] = first[i] + second[i];
        }

        return result;
    }
}
