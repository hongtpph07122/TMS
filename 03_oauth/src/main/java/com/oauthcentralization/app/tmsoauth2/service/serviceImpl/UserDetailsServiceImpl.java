package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersRoleResponse;
import com.oauthcentralization.app.tmsoauth2.service.UsersRoleService;
import com.oauthcentralization.app.tmsoauth2.service.UsersService;
import com.oauthcentralization.app.tmsoauth2.utils.DateUtils;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings({"all"})
@Service(value = "usersDetailService")
@javax.transaction.Transactional
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    public static final String USER_PREFIX = "USER";
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UsersRoleService usersRoleService;

    @Value("${tms.invoke-mechanism-system-change-password.no-days-expired:30}")
    private int noDaysExpired;

    @Value("${tms.invoke-mechanism-system-change-password.enable-deep-expire-password:false}")
    private boolean enableDeepExpiredPassword;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String passwordRaw = request.getParameter("password");

        /* 1. build user un-existed */
        UsersResponse usersResponse = usersService.findOne(StringUtils.trimAllWhitespace(username));

        if (!ObjectUtils.allNotNull(usersResponse)) {
            return MyUserDetailsImpl.buildUserUnExisted(); /* Bad credentials */
        }


        /* 2. build user is blocked */
        if (usersResponse.getActiveStandard().equals(1)) {
            return MyUserDetailsImpl.buildUserIsBlocked(); /* User account is locked */
        }

        /* 3. build user is logged in failed  */
        if (!passwordEncoder.matches(passwordRaw, usersResponse.getPassword())) {
            usersService.countUserSignInFailed(usersResponse.getUserId());
            return MyUserDetailsImpl.buildUserFailed(); /* Bad credentials, return MyUserDetailsImpl.buildUserFailed(); // User is disabled: wrong password */
        } else {

            if (enableDeepExpiredPassword) {
                if (!ObjectUtils.allNotNull(usersResponse.getExpired()) ||
                        !ObjectUtils.allNotNull(usersResponse.getPasswordUpdateTime())) {
                    return MyUserDetailsImpl.buildUserIsBlocked();
                }
                if (usersResponse.getExpired().equals(true) || (enableDeepExpiredPassword && ObjectUtils.allNotNull(usersResponse.getPasswordUpdateTime()) &&
                        DateUtils.snapDuration(usersResponse.getPasswordUpdateTime(), new Date(), "days") > this.noDaysExpired)) {
                    usersService.countUserSignInFailed(usersResponse.getUserId());
                    return MyUserDetailsImpl.buildUserFailedIsExpired(); /* User account has expired */
                }
            }
        }

        /* 4. build user valid */
        List<UsersRoleResponse> usersRoleResponses = usersRoleService.snagUsersRoles(usersResponse.getUserId());
        for (UsersRoleResponse usersRoleResponse : usersRoleResponses) {
            if (StringUtils.isEmpty(usersRoleResponse.getRoleSuffix())) {
                usersRoleResponse.setRoleSuffix(USER_PREFIX);
            }

            if (ObjectUtils.allNotNull(usersRoleResponse.getRoleId())) {
                usersResponse.setRolesId(Collections.singletonList(usersRoleResponse.getRoleId()));
            }
        }

        List<String> roles = usersRoleResponses.stream().map(UsersRoleResponse::getRoleSuffix).collect(Collectors.toList());
        usersService.updateOneByIsExpired(usersResponse.getUserId(), 0);
        if (CollectionUtils.isEmpty(roles)) {
            return MyUserDetailsImpl.buildUser(usersResponse, Collections.singletonList(USER_PREFIX));
        }
        return MyUserDetailsImpl.buildUser(usersResponse, roles);
    }
}
