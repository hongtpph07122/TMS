package com.oauthcentralization.app.tmsoauth2.model.enums;

import com.oauthcentralization.app.tmsoauth2.model.response.UsersTypeResponse;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;

public enum RoleType {

    ROLE_ADMIN(1, "admin", "ROLE_ADMIN", UsersType.ADMIN.getValue(), 11),
    ROLE_VALIDATION(2, "validation", "ROLE_VALIDATION", UsersType.VALIDATOR.getValue(), 14),
    ROLE_TEAM_LEADER(3, "team_leader", "ROLE_TEAM_LEADER", UsersType.TEAM_LEADER.getValue(), 15),
    ROLE_MANAGEMENT(4, "management", "ROLE_MANAGEMENT", UsersType.MANAGEMENT.getValue(), 16),
    ROLE_AGENT(5, "agent", "ROLE_AGENT", UsersType.AGENT.getValue(), 12),
    ROLE_LASTMILE(6, "lastmile", "ROLE_LASTMILE", UsersType.LAST_MILE.getValue(), 17),
    ROLE_LOGISTIC(7, "logistic", "ROLE_LOGISTIC", UsersType.LOGISTIC.getValue(), 18),
    ROLE_TL_EFA(8, "tl_efa", "ROLE_TL_EFA", UsersType.TEAM_LEADER.getValue(), 19),
    ROLE_ACCOUNTANT(9, "accountant", "ROLE_ACCOUNTANT", UsersType.ACCOUNTANT.getValue(), 20),
    ROLE_CS(10, "customer_service", "ROLE_CS", UsersType.CUSTOMER_SERVICE.getValue(), 21),
    ROLE_AGENT_MKT(11, "agent mkt", "ROLE_AGENT_MKT", UsersType.AGENT_MKT.getValue(), 22),
    ROLE_DIRECTOR(12, "director", "ROLE_DIRECTOR", UsersType.DIRECTOR.getValue(), 23),
    ROLE_MANAGER(13, "manager", "ROLE_MANAGER", UsersType.MANAGER.getValue(), 26),
    ROLE_MANIPULATE(14, "manipulate", "ROLE_MANIPULATE", UsersType.MANIPULATE.getValue(), 25),
    ROLE_ALL_SUPER(100, "all", "ROLE_ALL", UsersType.UNDEFINED.getValue(), 100);

    private final int value;
    private final String name;
    private final String label;
    private final int userTypeId;
    private final int roleId;

    RoleType(int value, String name, String label, int userTypeId, int roleId) {
        this.value = value;
        this.name = name;
        this.label = label;
        this.userTypeId = userTypeId;
        this.roleId = roleId;
    }

    public static RoleType findBy(UsersTypeResponse userType) {
        if (!ObjectUtils.allNotNull(userType)) {
            return RoleType.ROLE_AGENT;
        }

        for (RoleType roleType : RoleType.values()) {
            if (roleType.getUserTypeId() == userType.getValue()) {
                return roleType;
            }
        }

        return RoleType.ROLE_AGENT;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public int getRoleId() {
        return roleId;
    }
}
