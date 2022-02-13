package com.tms.api.dto;

import java.util.Date;

/**
 * Created by dinhanhthai on 16/11/2019.
 */
public class ObjExpire<T> {
    private T value;
    private Date exprireAt;
    private static ObjExpire _instance;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Date getExprireAt() {
        return exprireAt;
    }

    public void setExprireAt(Date exprireAt) {
        this.exprireAt = exprireAt;
    }

    public boolean isExprire() {
        if (exprireAt == null)
            return true;
        return (exprireAt.compareTo(new Date()) < 0);
    }
}
