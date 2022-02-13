package com.oauthcentralization.app.tmsoauth2.model.enums;

import com.oauthcentralization.app.tmsoauth2.model.response.UsersTypeResponse;
import com.oauthcentralization.app.tmsoauth2.utilities.StringUtility;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

public enum UsersType {

    ADMIN(1, "admin", ""),
    MANAGER(2, "manager", ""),
    DIRECTOR(3, "director", ""),
    TEAM_LEADER(4, "team leader", ""),
    VALIDATOR(5, "validator", ""),
    ACCOUNTANT(6, "accountant", ""),
    AGENT(7, "agent", ""),
    CUSTOMER_SERVICE(8, "CS", ""),
    AGENT_MKT(9, "agent mkt", ""),
    LOGISTIC(10, "logistic", ""),
    LAST_MILE(11, "lastmile", ""),
    MANAGEMENT(12, "management", ""),
    MANIPULATE(13, "manipulate", ""),
    UNDEFINED(100, "undefined", "");

    private final int value;
    private final String name;
    private final String description;


    UsersType(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    public static UsersTypeResponse findBy(int value) {
        if (!ObjectUtils.allNotNull(value)) {
            return null;
        }

        for (UsersType usersType : UsersType.values()) {
            if (usersType.getValue() == value) {
                return new UsersTypeResponse(usersType.getValue(), StringUtility.capitalizeEachWords(usersType.getName()), usersType.toString());
            }
        }

        return null;
    }

    public static UsersType findBy(String value) {
        if (StringUtility.isEmpty(value)) {
            return UsersType.UNDEFINED;
        }

        for (UsersType usersType : UsersType.values()) {
            if (StringUtility.areEqualText(usersType.getName(), value)) {
                return usersType;
            }
        }

        return UsersType.UNDEFINED;
    }

    public static UsersType findByValue(int value) {
        if (!ObjectUtils.allNotNull(value)) {
            return UsersType.UNDEFINED;
        }

        for (UsersType usersType : UsersType.values()) {
            if (usersType.getValue() == value) {
                return usersType;
            }
        }

        return UsersType.UNDEFINED;
    }

    public static List<UsersTypeResponse> findAll() {
        List<UsersTypeResponse> list = new ArrayList<>();
        for (UsersType usersType : UsersType.values()) {
            if (usersType.getValue() != UsersType.UNDEFINED.getValue()) {
                list.add(findBy(usersType.getValue()));
            }
        }
        return list;
    }

    public static boolean isValid(UsersTypeResponse usersTypeResponse) {
        if (!ObjectUtils.allNotNull(usersTypeResponse)) {
            return false;
        }
        return findAll().stream()
                .anyMatch(p -> p.getValue() == usersTypeResponse.getValue());
    }

    public static UsersTypeResponse findByName(String name) {
        if (StringUtility.isEmpty(name)) {
            return null;
        }

        for (UsersType usersType : UsersType.values()) {
            if (StringUtility.areEqualText(usersType.getName(), name)) {
                return new UsersTypeResponse(usersType.getValue(), StringUtility.capitalizeEachWords(usersType.getName()), usersType.toString());
            }
        }

        return null;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
