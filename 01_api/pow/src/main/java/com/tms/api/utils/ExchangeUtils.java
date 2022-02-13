package com.tms.api.utils;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExchangeUtils {

    public static int[] exchangeStringArrayToIntegerArray(String[] arrays) {
        return Stream.of(arrays)
                .mapToInt(Integer::parseInt).toArray();
    }

    public static String[] exchangeIntegerArrayToStringArray(int[] arrays) {
        return Arrays.stream(arrays)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);
    }

    public static String exchangeListStringToStringWithJoins(String separator, List<String> list) {
        if (StringUtils.isEmpty(separator)) {
            separator = ",";
        }

        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        return String.join(separator, list); /* item_1 separator item_2 separator ... */
    }

    public static List<String> exchangeListIntegerToListString(List<Integer> list) {
        return list.stream().map(String::valueOf).collect(Collectors.toList());
    }
}
