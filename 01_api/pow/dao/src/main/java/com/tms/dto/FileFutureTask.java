package com.tms.dto;

public abstract class FileFutureTask<T> extends FutureTask<T> {
    public String type(){return "FILE";}
}
