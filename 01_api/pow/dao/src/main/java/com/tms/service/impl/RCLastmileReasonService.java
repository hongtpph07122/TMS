package com.tms.service.impl;

import com.tms.config.DBConfig;
import com.tms.dao.impl.RCLastmileReasonDao;
import com.tms.dto.*;
import com.tms.entity.RCLastmileReason;
import com.tms.exception.ConfigNotFoundException;
import com.tms.service.BaseService;
import com.tms.utils.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RCLastmileReasonService extends BaseService {
    private final static String GET_RC_LASTMILE_REASON = "get_rc_lastmile_reason";

    private Logger logger = LoggerFactory.getLogger(RCLastmileReasonService.class);

    @Autowired
    private RCLastmileReasonDao rcLastmileReasonDao;

    public DBResponse<List<RCLastmileReason>> getRCLastmileReason(String _sessionId, RCLastmileReasonParams lastmileReasonParams) {
        DBResponse<List<RCLastmileReason>> response;
        try {
            final DBConfig config = getConfig(GET_RC_LASTMILE_REASON);
            AppUtils.printInput(logger, _sessionId, config, null, lastmileReasonParams);

            FutureTask<DBResponse<List<RCLastmileReason>>> memTask = new MemFutureTask<DBResponse<List<RCLastmileReason>>>() {

                @Override
                public DBResponse<List<RCLastmileReason>> get() {
                    return null;
                }
            };

            FutureTask<DBResponse<List<RCLastmileReason>>> dbTask = new DBFutureTask<DBResponse<List<RCLastmileReason>>>() {
                @Override
                public DBResponse<List<RCLastmileReason>> get() {
                    return rcLastmileReasonDao.dbGetRCLastmileReason(config, lastmileReasonParams);
                }
            };
            response = query(_sessionId, config, memTask, dbTask);

            AppUtils.printOutput(logger, _sessionId, config, null, response);

        } catch (ConfigNotFoundException e) {
            response = DBResponse.qConfigError(_sessionId, e.getMessage());
            logger.error(response.toString());
        }
        return response;
    }
}
