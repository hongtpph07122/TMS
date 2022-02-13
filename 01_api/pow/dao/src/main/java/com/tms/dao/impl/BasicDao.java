package com.tms.dao.impl;

import com.tms.config.DataSourceConfig;
import com.tms.config.DBConfigMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Repository
public abstract class BasicDao {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier(DataSourceConfig.DB_JDBC_TEMPLATE)
    protected JdbcTemplate dbTemplate;


   /* @Autowired
    @Qualifier(DataSourceConfig.LOG_DB_JDBC_TEMPLATE)
    protected JdbcTemplate logJdbcTemplate;*/

    @Autowired
    @Qualifier(DataSourceConfig.MM_JDBC_TEMPLATE)
    protected JdbcTemplate mmTemplate;


    @Autowired
    protected DBConfigMap configMap;

    protected String getPath(String funcName) {
        return configMap.getFilePath() + File.separator + getFileName(funcName);
    }

    private String getFileName(String fn) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String format = sf.format(new Date());
        return String.format("%s.csv.%s", fn, format);
    }
}
