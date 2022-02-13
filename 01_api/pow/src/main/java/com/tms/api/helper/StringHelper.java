package com.tms.api.helper;


/**
 * Created by dinhanhthai on 05/02/2020.
 */
public class StringHelper {
    public static int createHashkeyFromString(String str) {
        int hash = 7;
        for (int i = 0; i < str.length(); i++) {
            hash = hash * 31 + str.charAt(i);
        }
        return hash;
    }

    public static String createHashkey(String prodName, String name, String phone) {
//        return String.format("%s-%s-%s", prodName, name, phone);
        return String.format("%s-%s", prodName, phone);
    }

    public static String createHashkey(com.tms.entity.CLBasket basket) {
//        return String.format("%s-%s-%s", basket.getProdName(), basket.getName().trim(), basket.getPhone().trim());
        return createHashkey(basket.getProdName(), basket.getName().trim(), basket.getPhone().trim());
    }

    public static String createHashkey(com.tms.entity.CLFresh clFresh) {
        //return String.format("%s-%s-%s", clFresh.getProdName(), clFresh.getName().trim(), clFresh.getPhone().trim());
        return createHashkey(clFresh.getProdName(), clFresh.getName().trim(), clFresh.getPhone().trim());
    }

    public static boolean isNullOrEmpty(String str){
        return (str == null || str.isEmpty());
    }

    public static String createContact(com.tms.entity.CLBasket basket){
        return String.format("%s", basket.getPhone().trim());
    }
}
