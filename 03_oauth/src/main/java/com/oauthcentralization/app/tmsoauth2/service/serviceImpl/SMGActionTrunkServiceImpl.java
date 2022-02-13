package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.entities.SMGActionTrunkEntity;
import com.oauthcentralization.app.tmsoauth2.model.request.SMGActionTrunkRequest;
import com.oauthcentralization.app.tmsoauth2.repositories.SMGActionTrunkRepository;
import com.oauthcentralization.app.tmsoauth2.service.SMGActionTrunkService;
import com.oauthcentralization.app.tmsoauth2.utils.LoggerUtils;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings({"FieldCanBeLocal"})
@Service(value = "SMGActionTrunkService")
@Transactional
public class SMGActionTrunkServiceImpl implements SMGActionTrunkService {

    private static final Logger logger = LoggerFactory.getLogger(SMGActionTrunkServiceImpl.class);

    private final SMGActionTrunkRepository smgActionTrunkRepository;

    @Autowired
    public SMGActionTrunkServiceImpl(
            SMGActionTrunkRepository smgActionTrunkRepository) {
        this.smgActionTrunkRepository = smgActionTrunkRepository;
    }

    @Override
    public void saveAsPayload(SMGActionTrunkRequest smgActionTrunkRequest) {
        SMGActionTrunkEntity smgActionTrunkEntity = new SMGActionTrunkEntity();

        if (ObjectUtils.allNotNull(smgActionTrunkRequest.getActionType())) {
            smgActionTrunkEntity.setActionType(smgActionTrunkRequest.getActionType().getValue());
        }

        if (ObjectUtils.allNotNull(smgActionTrunkRequest.getModuleType())) {
            smgActionTrunkEntity.setModuleType(smgActionTrunkRequest.getModuleType().getValue());
        }

        if (ObjectUtils.allNotNull(smgActionTrunkRequest.getOnTable())) {
            smgActionTrunkEntity.setOnTable(smgActionTrunkRequest.getOnTable().getName());
        }

        if (ObjectUtils.allNotNull(smgActionTrunkRequest.getMyUserDetails())) {
            smgActionTrunkEntity.setUserId(smgActionTrunkRequest.getMyUserDetails().getUserId());
        }

        smgActionTrunkEntity.setLogJsonAfter(LoggerUtils.toJsonNode(smgActionTrunkRequest.getLogJsonAfter()));
        smgActionTrunkEntity.setLogJsonBefore(LoggerUtils.toJsonNode(smgActionTrunkRequest.getLogJsonBefore()));
        smgActionTrunkEntity.setRequestJson(LoggerUtils.toJsonNode(smgActionTrunkRequest.getRequestJson()));
        smgActionTrunkRepository.save(smgActionTrunkEntity);
    }
}
