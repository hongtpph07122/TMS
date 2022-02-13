package com.oauthcentralization.app.tmsoauth2.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oauthcentralization.app.tmsoauth2.model.enums.TablesType;
import com.oauthcentralization.app.tmsoauth2.model.enums.TrunkModuleType;
import com.oauthcentralization.app.tmsoauth2.model.enums.TrunkType;
import com.oauthcentralization.app.tmsoauth2.service.serviceImpl.MyUserDetailsImpl;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SMGActionTrunkRequest {

    private TrunkType actionType;
    private TrunkModuleType moduleType;
    private Object logJsonAfter;
    private Object logJsonBefore;
    private Object requestJson;
    private TablesType onTable;
    private MyUserDetailsImpl myUserDetails;

    public SMGActionTrunkRequest() {
    }

    public TrunkType getActionType() {
        return actionType;
    }

    public void setActionType(TrunkType actionType) {
        this.actionType = actionType;
    }

    public TrunkModuleType getModuleType() {
        return moduleType;
    }

    public void setModuleType(TrunkModuleType moduleType) {
        this.moduleType = moduleType;
    }

    public Object getLogJsonAfter() {
        return logJsonAfter;
    }

    public void setLogJsonAfter(Object logJsonAfter) {
        this.logJsonAfter = logJsonAfter;
    }

    public Object getLogJsonBefore() {
        return logJsonBefore;
    }

    public void setLogJsonBefore(Object logJsonBefore) {
        this.logJsonBefore = logJsonBefore;
    }

    public Object getRequestJson() {
        return requestJson;
    }

    public void setRequestJson(Object requestJson) {
        this.requestJson = requestJson;
    }

    public TablesType getOnTable() {
        return onTable;
    }

    public void setOnTable(TablesType onTable) {
        this.onTable = onTable;
    }

    public MyUserDetailsImpl getMyUserDetails() {
        return myUserDetails;
    }

    public void setMyUserDetails(MyUserDetailsImpl myUserDetails) {
        this.myUserDetails = myUserDetails;
    }
}
