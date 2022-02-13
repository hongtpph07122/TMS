package com.oauthcentralization.app.tmsoauth2.model.enums;

public enum TrunkType {
    EXCLUDE_GROUP_MEMBERS(1, "Exclude Group Members", 1),
    EXCLUDE_TEAM_MEMBERS(2, "Exclude Team Members", 2),
    MOVE_GROUP_MEMBERS(3, "Move Group Members", 3),
    MOVE_TEAM_MEMBERS(4, "Move Team Members", 4),
    ADD_TEAM_MEMBERS(5, "Add Team Members", 5),
    ADD_GROUP_MEMBERS(6, "Add Group Members", 6);


    private final int order;
    private final String name;
    private final int value;


    TrunkType(int order, String name, int value) {
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
