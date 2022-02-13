package com.tms.api.utils;

import java.util.AbstractCollection;
import java.util.List;

public class ObjectUtils {

    public static boolean isNull(Object object){
        return object == null;
    }

    public static boolean allNotNull(final Object... values) {
        if (values == null) {
            return false;
        }
        for (final Object val : values) {
            if (val == null) {
                return false;
            }
        }
        return true;
    }


    public static boolean isContainsIn(AbstractCollection<Object> objects, Object keys) {
        if (!allNotNull(objects, keys)) {
            return false;
        }
        return objects.contains(keys);
    }

    public static boolean isContainsIn(List<Integer> objects, Integer keys) {
        if (!allNotNull(objects, keys)) {
            return false;
        }
        return objects.contains(keys);
    }

}
