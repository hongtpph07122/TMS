package com.tms.api.entity;

public class SearchCriteria {
    private String key;
    private String operator;
    private Object value;
    
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operation) {
        this.operator = operation;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    
}
