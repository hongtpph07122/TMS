package com.oauthcentralization.app.tmsoauth2.model.enums;

public enum TablesType {
    GROUP_AGENT_TABLE(1, "or_group_agent", 1),
    TEAM_MEMBERS_TABLE(2, "or_team_member", 2);

    private final int order;
    private final String name;
    private final int value;

    TablesType(int order, String name, int value) {
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
