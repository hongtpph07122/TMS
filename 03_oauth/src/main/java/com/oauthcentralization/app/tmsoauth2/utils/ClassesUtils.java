package com.oauthcentralization.app.tmsoauth2.utils;


import com.oauthcentralization.app.tmsoauth2.utilities.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class ClassesUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassesUtils.class);


    public static List<Field> snagFields(Class<?> clazz) {
        if (!ObjectUtils.allNotNull(clazz)) {
            return Collections.emptyList();
        }

        List<Field> result = new ArrayList<>(snagFields(clazz.getSuperclass()));
        List<Field> filteredFields = Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> Modifier.isPublic(f.getModifiers()) || Modifier.isProtected(f.getModifiers()) || Modifier.isPrivate(f.getModifiers()))
                .collect(Collectors.toList());
        result.addAll(filteredFields);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> c, T[] a) {
        return c.size() > a.length ?
                c.toArray((T[]) Array.newInstance(a.getClass().getComponentType(), c.size())) :
                c.toArray(a);
    }

    /**
     * The collection CAN be empty
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(Collection<T> c, Class<?> klass) {
        return toArray(c, (T[]) Array.newInstance(klass, c.size()));
    }

    /**
     * The collection CANNOT be empty!
     */
    public static <T> T[] toArray(Collection<T> c) {
        return toArray(c, c.iterator().next().getClass());
    }


    public static List<Field> snapFieldsIncludingSuperclasses(Class<?> clazz) {
        List<Field> fields = new ArrayList<>(Arrays.asList(clazz.getDeclaredFields()));

        Class<?> superclass = clazz.getSuperclass();

        if (ObjectUtils.allNotNull(superclass)) {
            fields.addAll(snapFieldsIncludingSuperclasses(superclass));
        }

        return fields;
    }

    public static Field[] snapFieldsIncludingSuperClasses(Class<?> clazz) {
        return toArray(snapFieldsIncludingSuperclasses(clazz));
    }

    public static List<String> snapFields(Field[] fields) {
        List<String> fieldsOf = new ArrayList<>();
        for (Field field : fields) {
            fieldsOf.add(field.getName());
        }
        return fieldsOf;
    }

    public static List<String> snapFieldsIncludingSuperClassesQuick(Class<?> clazz) {
        return snapFields(snapFieldsIncludingSuperClasses(clazz));
    }

    public static String snagOneField(Class<?> clazz, String fieldName) {
        Field[] fieldsOfClass = snapFieldsIncludingSuperClasses(clazz);
        List<String> fieldsOf = snapFields(fieldsOfClass);
        logger.info("Searched all fields on this class: {} \n {} \n [field size: {}]", clazz, fieldsOf, fieldsOf.size());
        for (String field : fieldsOf) {
            if (field.toLowerCase().contains(StringUtility.trimAllWhitespace(fieldName).toLowerCase())) {
                return field;
            }
        }
        return "";
    }
}

