package com.oauthcentralization.app.tmsoauth2.model.enums;

public enum TrunkModuleType {
    OAUTH2_MODULE(1, "Oauth2 Module", 3);


    private final int order;
    private final String name;
    private final int value;

    TrunkModuleType(int order, String name, int value) {
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
