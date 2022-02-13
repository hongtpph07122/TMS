package com.tms.dto;

public class DBResponse<T> {

    public static final String DB_TIME_OUT_N = "N";
    public static final String DB_TIME_OUT_Y = "Y";
    public static final String DB_TIME_OUT_SRC_CONF = "CONF";
    public static final String DB_TIME_OUT_SRC_JDBC = "JDBC";
    public static final String DB_CONN_TYPE_DB = "DB";
    public static final String DB_CONN_TYPE_FILE = "FILE";
    public static final String DB_CONN_TYPE_MEMORY = "MEMORY";

    public static final String ACTION_QUERY = "QUERY";
    public static final String ACTION_INSERT = "INSERT";

    private String errorMsg;
    private String action;
    private String sessionId;
    private int errorCode;
    private int rowCount;
    private T result;

    private String dbTimeout = DB_TIME_OUT_N;
    private String dbTimeoutSrc;
    private String dbConnType;

    public DBResponse() {
    }

    public DBResponse(String conn) {
        this.dbConnType = conn;
    }

    public DBResponse dbTimeout(){
        this.dbTimeout = DB_TIME_OUT_Y;
        return this;
    }

    public DBResponse dbNotTimeout(){
        this.dbTimeout = DB_TIME_OUT_N;
        return this;
    }

    public DBResponse dbTimeoutConf(){
        this.dbTimeoutSrc = DB_TIME_OUT_SRC_CONF;
        return this;
    }

    public DBResponse dbTimeoutJdbc(){
        this.dbTimeoutSrc = DB_TIME_OUT_SRC_JDBC;
        return this;
    }

    public DBResponse dbConDB(){
        this.dbConnType = DB_CONN_TYPE_DB;
        return this;
    }

    public DBResponse dbConFile(){
        this.dbConnType = DB_CONN_TYPE_FILE;
        return this;
    }

    public DBResponse dbConMEMORY(){
        this.dbConnType = DB_CONN_TYPE_MEMORY;
        return this;
    }

    public static DBResponse qConfigError(String sessionId,String msg){
        DBResponse response = new DBResponse();
        response.action = ACTION_QUERY;
        response.errorCode = 2;
        response.errorMsg = msg;
        response.sessionId = sessionId;
        return response;
    }

    public static DBResponse iConfigError(String sessionId, String msg){
        DBResponse response = new DBResponse();
        response.action = ACTION_INSERT;
        response.errorCode = 2;
        response.errorMsg = msg;
        response.sessionId = sessionId;
        return response;
    }


    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getDbTimeout() {
        return dbTimeout;
    }

    public void setDbTimeout(String dbTimeout) {
        this.dbTimeout = dbTimeout;
    }

    public String getDbTimeoutSrc() {
        return dbTimeoutSrc;
    }

    public void setDbTimeoutSrc(String dbTimeoutSrc) {
        this.dbTimeoutSrc = dbTimeoutSrc;
    }

    public String getDbConnType() {
        return dbConnType;
    }

    public void setDbConnType(String dbConnType) {
        this.dbConnType = dbConnType;
    }

    public boolean isWrite2File() {
        return DBResponse.DB_CONN_TYPE_FILE.equals(this.dbConnType);
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    @Override
    public String toString() {
        StringBuilder bf = new StringBuilder();
        if (errorCode == 0) {
            bf.append(sessionId + ":" + action + ": Successful");
        } else {
            bf.append(sessionId + ":" + action + ":" + errorMsg);
        }
        return bf.toString();
    }
}
