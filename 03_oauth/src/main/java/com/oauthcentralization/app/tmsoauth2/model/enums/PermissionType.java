package com.oauthcentralization.app.tmsoauth2.model.enums;

public enum PermissionType {
    CREATE_USER(1, "Create Users", 1),
    MOVE_USER_TO_GROUP(2, "Move Users To Group", 2),
    MOVE_USER_TO_TEAM(3, "Move Users To Team", 3),
    UPDATE_USER(4, "Update Users", 4),
    EXCLUDE_USER_GROUP(5, "Exclude Users Member", 5),
    EXCLUDE_USER_TEAM(6, "Exclude Users Member", 6),
    ADD_USER_TEAM(7, "Add Users Member", 7);


    private final int order;
    private final String name;
    private final int value;

    PermissionType(int order, String name, int value) {
        this.order = order;
        this.name = name;
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
