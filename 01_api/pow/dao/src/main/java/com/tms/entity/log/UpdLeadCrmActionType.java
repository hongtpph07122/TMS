package com.tms.entity.log;

public class UpdLeadCrmActionType {
    private String leadId;
    private Integer crmActionTYpe;
    private Integer modifiedBy;

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public Integer getCrmActionTYpe() {
        return crmActionTYpe;
    }

    public void setCrmActionTYpe(Integer crmActionTYpe) {
        this.crmActionTYpe = crmActionTYpe;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
