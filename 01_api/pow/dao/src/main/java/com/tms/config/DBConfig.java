package com.tms.config;

import java.util.List;

public class DBConfig {

    private final static String MEM = "mem";
    private final static String DB = "db";
    private final static int DEFAULT_TIMEOUT = 60; //1MIN

    private String funcName;
    private String qMode;
    private List<Long> qTimeout;
    private Boolean qFallback;
    private Boolean alwaysWriteFile;

    public boolean isMemoryQuery() {
        return MEM.equals(qMode);
    }

    public boolean isDBQuery() {
        return DB.equals(qMode);
    }

    public boolean isFallBack() {
        return qFallback != null && qFallback;
    }


    public long getMemTimeout(){
        return qTimeout.size() > 0 ? qTimeout.get(0)* 1000: DEFAULT_TIMEOUT * 1000;
    }

    public long getDBTimeout(){
        return qTimeout.size() > 1 ? qTimeout.get(1) * 1000: DEFAULT_TIMEOUT * 1000;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getqMode() {
        return qMode;
    }

    public void setqMode(String qMode) {
        this.qMode = qMode;
    }


    public void setqTimeout(List<Long> qTimeout) {
        this.qTimeout = qTimeout;
    }

    public Boolean getqFallback() {
        return qFallback;
    }

    public void setqFallback(Boolean qFallback) {
        this.qFallback = qFallback;
    }

    public List<Long> getqTimeout() {
        return qTimeout;
    }

    public Boolean getAlwaysWriteFile() {
        return alwaysWriteFile;
    }

    public void setAlwaysWriteFile(Boolean alwaysWriteFile) {
        this.alwaysWriteFile = alwaysWriteFile;
    }
}
