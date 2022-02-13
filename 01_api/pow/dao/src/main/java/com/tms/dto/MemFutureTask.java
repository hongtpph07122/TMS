package com.tms.dto;

public abstract class MemFutureTask<T> extends FutureTask<T> {
    public String type(){return "Mem";}
}
