package com.tms.api.service.impl;

import com.tms.api.dto.MktDataDto;
import com.tms.api.service.MktDataService;
import com.tms.api.utils.SqlUtils;
import com.tms.dto.DBResponse;
import com.tms.dto.GetMktData;
import com.tms.dto.GetMktDataParams;
import com.tms.dto.GetMktDataResp;
import com.tms.entity.CLFresh;
import com.tms.entity.log.InsMktData;
import com.tms.entity.log.UpdMktData;
import com.tms.service.impl.CLFreshService;
import com.tms.service.impl.LogService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class MktDataServiceImpl implements MktDataService {
    private final Logger logger = LoggerFactory.getLogger(MktDataServiceImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    private final ModelMapper modelMapper;

    private final LogService logService;

    private final CLFreshService clFreshService;

    @Autowired
    public MktDataServiceImpl(ModelMapper modelMapper, LogService logService, CLFreshService clFreshService) {
        this.modelMapper = modelMapper;
        this.logService = logService;
        this.clFreshService = clFreshService;
    }

    @Override
    public List<MktDataDto> get(GetMktData params) {
        StringBuilder getSql = new StringBuilder();
        getSql.append("select ")
                .append(SqlUtils.getParamsWithoutLimitOffSet(GetMktData.class))
                .append(" from mkt_data where 1=1")
                .append(SqlUtils.getWhereParamsSql(params));
        String count = "select count(*) from (" + getSql + ") as a";

        getSql.append(" order by id, lead_id desc");
        if (params.getLimit() != null)
            getSql.append(" limit :limit");
        if (params.getOffset() != null)
            getSql.append(" offset :offset");

        Query query = entityManager.createNativeQuery(getSql.toString());
        Query queryCount = entityManager.createNativeQuery(count);

        SqlUtils.setQueryParams(params, query, queryCount);

        List<Object[]> rows = query.getResultList();

        return getResultList(rows);
    }

    @Override
    public void save(String ssid, CLFresh clFresh, Integer userId) {
        String ssId = UUID.randomUUID().toString();

        GetMktDataParams params = new GetMktDataParams();
        params.setLeadId(clFresh.getLeadId()+"");
        DBResponse<List<GetMktDataResp>> dbResponse = clFreshService.getMktData(ssId, params);

        if (dbResponse != null && !dbResponse.getResult().isEmpty()) {
            UpdMktData updMktData = modelMapper.map(clFresh, UpdMktData.class);
            updMktData.setUpdateby(userId);
            logService.updMktData(ssid, updMktData);
            return;
        }

        InsMktData insMktData = modelMapper.map(clFresh, InsMktData.class);
        insMktData.setCreateby(userId);
        logService.insMktData(ssid, insMktData);
    }

    @Override
    public <T> void save(String ssid, T clFresh, Integer leadId, Integer userId) {
        InsMktData insMktData = modelMapper.map(clFresh, InsMktData.class);
        insMktData.setLeadId(leadId);
        insMktData.setCreateby(userId);
        logService.insMktData(ssid, insMktData);
    }

    public List<MktDataDto> getResultList(List<Object[]> rows) {
        List<MktDataDto> result = new ArrayList<>();
        for (Object[] row : rows) {
            MktDataDto config = new MktDataDto();
            Field[] fields = config.getClass().getDeclaredFields();
            int iterator = 0;

            try {
                for (Field field : fields) {
                    Method method = config.getClass().getMethod(
                            "set" + SqlUtils.upperCaseFirst(field.getName()), field.getType());
                    if (row[iterator] != null)
                        method.invoke(config, row[iterator]);
                    iterator++;
                }
                result.add(config);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return result;
    }
}
