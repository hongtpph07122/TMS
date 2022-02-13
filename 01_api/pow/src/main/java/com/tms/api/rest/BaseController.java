package com.tms.api.rest;

import com.tms.api.dto.UserCache;
import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.api.helper.Const;
import com.tms.api.helper.Helper;
import com.tms.api.helper.TMSValidation;
import com.tms.dto.DBResponse;
import com.tms.dto.GetUserParamsV5;
import com.tms.entity.User;
import com.tms.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by dinhanhthai on 22/04/2019.
 */
public abstract class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    protected static Map<String, UserCache> _MAP_USER = new HashMap<>();
    protected String SESSION_ID;
    protected User _curUser = null;
    @Value("${config.user-expiretime}")
    protected Integer USER_EXPIRE_TIME;
    @Autowired
    TMSValidation validation;
    @Autowired
    UserService userService;

    private String getTokenString() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    public User getCurrentUser() throws TMSException {
        return getCurrentUser(null);
    }

    protected int getCurOrgId() {
        try {
            return getCurOrgId(null);
        } catch (Exception e) {
            logger.error("GET CURRENT : {}", e.getMessage(), e);
            return -1;
        }
    }

    protected int getUserId(HttpServletRequest request){
        try {
            return getCurrentUser(request).getUserId();
        } catch (Exception e) {
            logger.error("GET CURRENT: {}", e.getMessage(), e);
            return -1;
        }
    }

    protected int getCurOrgId(HttpServletRequest request) {
        try {
            return getCurrentUser(request).getOrgId();
        } catch (Exception e) {
            logger.error("GET CURRENT: {}", e.getMessage(), e);
            return -1;
        }
    }

    public User getCurrentUser(HttpServletRequest request) throws TMSException {
        String userName = "";
        if (request != null) {
            OAuth2Authentication principal = (OAuth2Authentication) request.getUserPrincipal();
            userName = principal.getName();
        } else {
            userName = Helper.getUserName();
        }

        UserCache userCache = new UserCache();
        if (_MAP_USER.containsKey(userName)) {
            userCache = _MAP_USER.get(userName);
            if (!userCache.isExprire()) {
                return userCache.getUser();
            }
        }

        logger.info("[USER] Get user info from DB {}", userName);
        DBResponse<List<User>> user = null;
        try {
            Date now = new Date();
            GetUserParamsV5 userParamsV5 = new GetUserParamsV5();
            userParamsV5.setUserName(userName);
            user = userService.getUserV5(SESSION_ID, userParamsV5);
            if (user != null && !user.getResult().isEmpty()) {
                for (int i = 0; i < user.getResult().size(); i++) {
                    _curUser = user.getResult().get(i);
                    if (userName.equals(_curUser.getUserName())) {
                        Date newDate = new Date(now.getTime() + USER_EXPIRE_TIME * 60 * 1000);
                        userCache.setExprireAt(newDate);
                        userCache.setUser(_curUser);

                        _MAP_USER.put(userName, userCache);
                        return _curUser;
                    }
                }
            }
            return null;
        } catch (Exception e) {
            throw new TMSException(ErrorMessage.USER_NOT_FOUND);
        }
    }

    public Integer getCurrentOriganationId() throws TMSException {
        User curUser = getCurrentUser();
        return curUser != null ? curUser.getOrgId() : 0;
    }

    @ModelAttribute("BeforeRequest")
    public void BeforeRequest(HttpServletRequest request) throws TMSException {
        SESSION_ID = RequestContextHolder.currentRequestAttributes().getSessionId();
        _curUser = getCurrentUser(request);
    }

    private List<String> getAllowedMethods(List<Integer> permissionValues) {
        List<String> allowedMethods = new ArrayList<>();
        for (Integer value : permissionValues) {
            allowedMethods.addAll(getAllowedMethods(value));
        }
        return allowedMethods;
    }

    private List<String> getAllowedMethods(int permissionValue) {
        List<String> allowedMethods = new ArrayList<String>();
        List<Integer> methodValues = Arrays.asList(Const.GET_VALUE, Const.POST_VALUE, Const.PUT_VALUE,
                Const.DELETE_VALUE);
        Collections.sort(methodValues, Collections.reverseOrder());
        for (Integer methodValue : methodValues) {
            if (permissionValue <= 0) {
                return allowedMethods;
            }
            if (permissionValue >= methodValue) {
                allowedMethods.add(getMethodByPrivValue(methodValue));
                permissionValue -= methodValue;
            }
        }
        return allowedMethods;
    }

    private String getMethodByPrivValue(int permissionValue) {
        switch (permissionValue) {
            case Const.GET_VALUE:
                return HttpMethod.GET.toString();
            case Const.POST_VALUE:
                return HttpMethod.POST.toString();
            case Const.PUT_VALUE:
                return HttpMethod.PUT.toString();
            case Const.DELETE_VALUE:
                return HttpMethod.DELETE.toString();
            default:
                return "";
        }
    }
}
