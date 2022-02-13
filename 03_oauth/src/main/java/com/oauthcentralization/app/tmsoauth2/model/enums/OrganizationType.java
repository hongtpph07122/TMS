package com.oauthcentralization.app.tmsoauth2.model.enums;

import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;

public enum OrganizationType {
    VIETNAM(1, "Vietnam", 4),
    INDONESIA(2, "Indonesia", 9),
    THAILAND(3, "Thailand", 10),
    UNDEFINE(100, "Undefine", 100);

    private final int order;
    private final String name;
    private final int value;

    OrganizationType(int order, String name, int value) {
        this.order = order;
        this.name = name;
        this.value = value;
    }

    public static OrganizationType findByValue(Integer value) {
        if (!ObjectUtils.allNotNull(value)) {
            return OrganizationType.UNDEFINE;
        }

        for (OrganizationType organizationType : OrganizationType.values()) {
            if (organizationType.getValue() == value) {
                return organizationType;
            }
        }

        return OrganizationType.UNDEFINE;
    }

    public int getOrder() {
        return order;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
