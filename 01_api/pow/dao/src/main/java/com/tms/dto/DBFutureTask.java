package com.tms.dto;

public abstract class DBFutureTask<T> extends FutureTask<T> {
    public String type(){return "DB";}
}
