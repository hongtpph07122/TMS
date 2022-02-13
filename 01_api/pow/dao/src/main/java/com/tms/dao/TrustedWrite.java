package com.tms.dao;

import com.tms.dto.DBResponse;
import com.tms.config.DBConfig;

public interface TrustedWrite<T> {

    DBResponse fileInsert(DBConfig config, T ob) throws Exception ;

    DBResponse dbInsert(DBConfig config, T ob) throws Exception ;

}
