package com.tms.api.service.impl;

import com.tms.api.dto.Request.UserActiveRequestDTO;
import com.tms.api.entity.OrUser;
import com.tms.api.helper.LogHelper;
import com.tms.api.repository.OrUserRepository;
import com.tms.api.response.TMSResponse;
import com.tms.api.service.UserActionsService;
import com.tms.api.utils.ValidationUtils;
import com.tms.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActionsServiceImpl implements UserActionsService {

    private static final Logger logger = LoggerFactory.getLogger(UserActionsServiceImpl.class);

    private final OrUserRepository orUserRepository;

    @Autowired
    public UserActionsServiceImpl(OrUserRepository orUserRepository) {
        this.orUserRepository = orUserRepository;
    }


    @Override
    public TMSResponse<?> updateOne(User currentUser, UserActiveRequestDTO userActiveRequestDTO) {
        TMSResponse<User> userTMSResponse = new TMSResponse<>();
        logger.info("current user id: {}", currentUser.getUserId());
        logger.info("current org id: {}", currentUser.getOrgId());
        logger.info("body user request: {}", LogHelper.toJson(userActiveRequestDTO));

        if (!ValidationUtils.isVerifiedAsEmail(userActiveRequestDTO.getEmail())) {
            userTMSResponse.setCode(400);
            userTMSResponse.setMessage("Email is invalid");
            userTMSResponse.setData(null);
            return userTMSResponse;
        }

        OrUser userEntity = orUserRepository.findOne(currentUser.getUserId());
        userEntity.setPersonal_mail(userActiveRequestDTO.getEmail());
        orUserRepository.save(userEntity);

        userTMSResponse.setCode(200);
        userTMSResponse.setMessage("Personal email is updated successfully");
        userTMSResponse.setData(findOne(userEntity.getUser_id()));
        return userTMSResponse;
    }

    @Override
    public User findOne(Integer id) {
        OrUser userEntity = orUserRepository.findOne(id);
        User user = new User();
        user.setUserId(userEntity.getUser_id());
        user.setUserType(userEntity.getUser_type());
        user.setUserName(userEntity.getUser_name());
        user.setUserLock(userEntity.getUser_lock());
        user.setFullname(userEntity.getFullname());
        user.setEmail(userEntity.getEmail());
        user.setPhone(userEntity.getPhone());
        user.setBirthday(userEntity.getBirthday());
        user.setModifyby(userEntity.getModifyby());
        user.setModifydate(userEntity.getModifydate());
        user.setOrgId(userEntity.getOrg_id());
        user.setHomePhone1(userEntity.getHome_phone_1());
        user.setHomePhone2(userEntity.getHome_phone_2());
        user.setPersonalPhone1(userEntity.getPersonal_phone_1());
        user.setPersonalPhone2(userEntity.getPersonal_phone_2());
        user.setWorkMail(userEntity.getWork_mail());
        user.setPersonalMail(userEntity.getPersonal_mail());
        user.setHomeAddress(userEntity.getHome_address());
        user.setForceChangePassword(userEntity.isForce_change_password());
        user.setFailedLoginCount(userEntity.getFailed_login_count());
        return user;
    }
}
