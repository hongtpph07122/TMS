package com.tms.dao;

import com.tms.dto.DBResponse;
import com.tms.config.DBConfig;

import java.util.List;

public interface FastRead<Q, I> {

    DBResponse<I> memQuery(DBConfig config, Q param);
    DBResponse<List<I>> memQuerys(DBConfig config, Q param);
    DBResponse<I> dbQuery(DBConfig config, Q param);
    DBResponse<List<I>> dbQuerys(DBConfig config, Q param);

}
