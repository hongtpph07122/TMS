package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.entities.UsersEntity;
import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.dto.HttpStatusCodesResponseDTO;
import com.oauthcentralization.app.tmsoauth2.model.enums.*;
import com.oauthcentralization.app.tmsoauth2.model.filters.RolesFilter;
import com.oauthcentralization.app.tmsoauth2.model.request.UsersRequest;
import com.oauthcentralization.app.tmsoauth2.model.request.UsersRoleRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.RolesResponse;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.repositories.UsersRepository;
import com.oauthcentralization.app.tmsoauth2.service.CommonsService;
import com.oauthcentralization.app.tmsoauth2.service.RolesBaseService;
import com.oauthcentralization.app.tmsoauth2.service.UsersRoleService;
import com.oauthcentralization.app.tmsoauth2.service.UsersService;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utilities.StringUtility;
import com.oauthcentralization.app.tmsoauth2.utils.DateUtils;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import com.oauthcentralization.app.tmsoauth2.utils.ValidationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "usersService")
@Transactional
public class UsersServiceImpl implements UsersService {

    private static final Logger logger = LoggerFactory.getLogger(UsersServiceImpl.class);

    private static final String PHONE_SIP_PREFIX = "SIP/";
    private final BCryptPasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final RolesBaseService rolesBaseService;
    private final UsersRoleService usersRoleService;
    private final CommonsService commonsService;

    @Value("${tms.geo.identify}")
    private int organizationId;

    @Value(("${tms.invoke-mechanism-system-change-password.no-days-expired}"))
    private int noOfDays;

    @Autowired
    public UsersServiceImpl(
            BCryptPasswordEncoder passwordEncoder,
            UsersRepository usersRepository,
            RolesBaseService rolesBaseService,
            UsersRoleService usersRoleService,
            CommonsService commonsService) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.rolesBaseService = rolesBaseService;
        this.usersRoleService = usersRoleService;
        this.commonsService = commonsService;
    }


    @Override
    public UsersResponse findOne(String username) {
        UsersEntity usersEntity = usersRepository.findUsersEntity(username);
        if (ObjectUtils.allNotNull(usersEntity)) {
            return findOne(usersEntity.getUserId());
        }
        return null;
    }

    @Override
    public UsersResponse findOne(Integer userId) {
        UsersEntity usersEntity = usersRepository.getOne(userId);
        UsersResponse usersResponse = new UsersResponse();
        usersResponse.setUserId(usersEntity.getUserId());
        usersResponse.setOrganizationId(usersEntity.getOrgId());
        usersResponse.setUserType(usersEntity.getUserType());
        usersResponse.setUsername(usersEntity.getUsername());
        usersResponse.setActiveStandard(usersEntity.getIsActive());
        usersResponse.setFullName(usersEntity.getFullName());
        usersResponse.setEmail(usersEntity.getEmail());
        usersResponse.setPhone(usersEntity.getPhone());
        usersResponse.setBirthday(usersEntity.getBirthday());
        usersResponse.setModifiedBy(usersEntity.getModifiedBy());
        usersResponse.setModifiedDate(usersEntity.getModifiedDate());
        usersResponse.setHomePhone_1(usersEntity.getHomePhone_1());
        usersResponse.setHomePhone_2(usersEntity.getHomePhone_2());
        usersResponse.setWorkMail(usersEntity.getWorkMail());
        usersResponse.setPersonalMail(usersEntity.getPersonalMail());
        usersResponse.setHomeAddress(usersEntity.getHomeAddress());
        usersResponse.setPersonalPhone_1(usersEntity.getPersonalPhone_1());
        usersResponse.setPersonalPhone_2(usersEntity.getPersonalPhone_2());
        usersResponse.setForceChangePassword(usersEntity.getForceChangePassword());
        usersResponse.setPasswordUpdateTime(usersEntity.getPasswordUpdateTime());
        usersResponse.setExpired(usersEntity.getIsExpired());
        usersResponse.setPassword(usersEntity.getPassword());
        return usersResponse;
    }

    @Override
    public List<UsersResponse> findUsersActive() {
        List<UsersEntity> usersEntities = usersRepository.findUsersEntities(0);
        return usersEntities.stream().map(usersEntity -> findOne(usersEntity.getUserId())).collect(Collectors.toList());
    }

    @Override
    public void updateOneByIsExpired(Integer userId, Integer isExpired) {
        UsersEntity usersEntity = usersRepository.getOne(userId);
        usersEntity.setUserId(usersEntity.getUserId());
        usersEntity.setIsExpired(ObjectUtils.allNotNull(isExpired) && isExpired == 1);
        usersRepository.save(usersEntity);
    }

    /**
     * @param userId   - get user by userId
     * @param password - update with new password req
     * @apiNote - update on 3 fields : password, password_update_time, is_expired = 1
     */
    @Override
    public void updateOneByPWDIsExpired(Integer userId, String password) {
        UsersEntity usersEntity = usersRepository.getOne(userId);
        usersEntity.setUserId(usersEntity.getUserId());
        usersEntity.setPassword(passwordEncoder.encode(password));
        usersEntity.setPasswordUpdateTime(new Date());
        usersEntity.setIsExpired(true);
        usersRepository.save(usersEntity);
    }

    /**
     * @param userId - get user by userId
     * @apiNote - update on 1 fields : password_update_time with back to yesterday
     */
    @Override
    public void updatePWDUpdateTimeIsNull(Integer userId, int days) {
        UsersEntity usersEntity = usersRepository.getOne(userId);
        usersEntity.setUserId(usersEntity.getUserId());
        usersEntity.setPasswordUpdateTime(DateUtils.subtractDays(new Date(), days - 1));
        usersRepository.save(usersEntity);
    }

    /**
     * @param userId - get user by userId
     * @apiNote - update on fields : failed_login_count , to count logged in failed
     */
    @Override
    public void countUserSignInFailed(Integer userId) {
        UsersEntity usersEntity = usersRepository.getOne(userId);
        BigDecimal valueOfFailed = usersRepository.findMaxLoggedFailed(userId);
        usersEntity.setUserId(usersEntity.getUserId());
        if (ObjectUtils.allNotNull(valueOfFailed)) {
            usersEntity.setFailedLoginCount(valueOfFailed.intValue() + 1);
        } else {
            usersEntity.setFailedLoginCount(1);
        }
        usersRepository.save(usersEntity);
    }

    @Override
    public boolean existsByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            return false;
        }
        return usersRepository.existsByUsername(StringUtils.trimAllWhitespace(username));
    }

    @Override
    public TMSResponse<?> validateCreateUser(UsersRequest usersRequest, UsersResponse usersResponse) {
        boolean hasPermissions = commonsService.hasRole(usersResponse, RoleType.ROLE_ALL_SUPER);

        if (!hasPermissions) {
            return TMSResponse.buildTMSResponse("You don't have permissions", null, HttpStatusCodesResponseDTO.NOT_ACCEPTABLE);
        }

        OrganizationType organizationType = OrganizationType.findByValue(organizationId);
        List<DomainType> domainTypes = DomainType.findByOrganizationType(organizationType);

        if (StringUtility.isAnyEmpty(usersRequest.getUsername())) {
            return TMSResponse.buildTMSResponse("Username is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!ValidationUtils.isVerifiedAsEmail(usersRequest.getUsername())) {
            return TMSResponse.buildTMSResponse("Invalid username (as email)", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!DomainType.isDomainNameEndWiths(domainTypes, usersRequest.getUsername())) {
            return TMSResponse.buildTMSResponse("Unsupported username domain", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (existsByUsername(usersRequest.getUsername())) {
            return TMSResponse.buildTMSResponse("Username already exists", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (StringUtility.isAnyEmpty(usersRequest.getPassword())) {
            return TMSResponse.buildTMSResponse("Password is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!ValidationUtils.isVerifiedAsPassword(usersRequest.getPassword())) {
            return TMSResponse.buildTMSResponse(
                    "Password contains at least: " +
                            "8 characters, " +
                            "one digit, " +
                            "one upper case alphabet, " +
                            "one lower case alphabet, " +
                            "one special character which includes !@#$%&*()-+=^. " +
                            "It does not contain any white space",
                    null,
                    HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (StringUtility.isAnyEmpty(usersRequest.getFullName())) {
            return TMSResponse.buildTMSResponse("Full Name is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (StringUtility.isAnyEmpty(usersRequest.getEmail())) {
            return TMSResponse.buildTMSResponse("Email is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (StringUtility.isAnyEmpty(usersRequest.getPhone())) {
            return TMSResponse.buildTMSResponse("Phone is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!ValidationUtils.isVerifiedAsEmail(usersRequest.getEmail())) {
            return TMSResponse.buildTMSResponse("Invalid Email", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!UsersType.isValid(usersRequest.getUserType())) {
            return TMSResponse.buildTMSResponse("Invalid User Type", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!commonsService.hasPermission(usersResponse, PermissionType.CREATE_USER, Collections.singletonList(UsersType.findByValue(usersRequest.getUserType().getValue())))) {
            return TMSResponse.buildTMSResponse(String.format("You do not have the authority to create this user type: %s ", UsersType.findBy(usersRequest.getUserType().getName()).getName()), null, HttpStatusCodesResponseDTO.METHOD_NOT_ALLOWED);
        }

        return TMSResponse.buildTMSResponse("Satisfactory conditions for creating User", null, HttpStatusCodesResponseDTO.OK);
    }

    @Override
    public TMSResponse<?> saveAsPayloads(UsersRequest usersRequest, UsersResponse usersResponse) {

        TMSResponse<?> validatePayloads = validateCreateUser(usersRequest, usersResponse);

        if (!validatePayloads.getHeader().equals(HttpStatusCodesResponseDTO.OK)) {
            return validatePayloads;
        }

        /* begin::Handle SIP */
        if (!usersRequest.getPhone().toLowerCase().startsWith(PHONE_SIP_PREFIX.toLowerCase())) {
            usersRequest.setPhone(StringUtility.trimSingleWhitespace(PHONE_SIP_PREFIX.concat(usersRequest.getPhone())).toUpperCase());
        } else {
            usersRequest.setPhone(StringUtility.trimSingleWhitespace(usersRequest.getPhone()).toUpperCase());
        }
        /* begin::Handle SIP */

        UsersRoleRequest usersRoleRequest = new UsersRoleRequest();
        RolesFilter rolesFilter = new RolesFilter();
        UsersEntity usersEntity = new UsersEntity();

        rolesFilter.setPageIndex(1);
        rolesFilter.setPageSize(2);

        usersEntity.setOrgId(organizationId);
        usersEntity.setUserType(UsersType.findBy(usersRequest.getUserType().getName()).getName());
        usersEntity.setUsername(StringUtility.trimSingleWhitespace(usersRequest.getUsername()));
        usersEntity.setPassword(passwordEncoder.encode(usersRequest.getPassword()));
        usersEntity.setFullName(StringUtility.capitalizeEachWords(StringUtility.trimSingleWhitespace(usersRequest.getFullName())));
        usersEntity.setPhone(usersRequest.getPhone());
        usersEntity.setEmail(StringUtility.trimSingleWhitespace(usersRequest.getEmail()));
        usersEntity.setBirthday(usersRequest.getBirthday());
        usersEntity.setPasswordUpdateTime(DateUtils.addDay(new Date(), this.noOfDays - 1));

        if (com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils.allNotNull(usersResponse.getUserId())) {
            usersEntity.setModifiedBy(usersResponse.getUserId());
        }

        /* begin::Find role */
        RoleType roleType = RoleType.findBy(usersRequest.getUserType());
        rolesFilter.setListId(Collections.singletonList(roleType.getRoleId()));
        List<RolesResponse> rolesResponses = rolesBaseService.findRolesBy(rolesFilter);
        /* end::Find role */

        if (CollectionsUtility.isEmpty(rolesResponses)) {
            logger.error("{}", "Role not found on database.");
            return TMSResponse.buildTMSResponse("Role invalid.", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        /* begin::Save user */
        usersRepository.save(usersEntity);
        /* end::Save user */

        /* begin::Save user role */
        usersRoleRequest.setRoleId(rolesResponses.get(0).getRoleId());
        usersRoleRequest.setUserId(usersEntity.getUserId());
        /* end::Save user role */

        try {
            boolean isCreatedUsersRole = usersRoleService.saveAsPayload(usersRoleRequest);
            if (!isCreatedUsersRole) {
                return TMSResponse.buildTMSResponse("Can't create new user", null, HttpStatusCodesResponseDTO.SERVICE_UNAVAILABLE);
            }
        } catch (Exception e) {
            logger.error("{}", "New user role can not create on table or_user_role.");
            logger.error(e.getMessage(), e);
        }

        return TMSResponse.buildTMSResponse("User created successfully", null, HttpStatusCodesResponseDTO.CREATED);
    }

    @Override
    public TMSResponse<?> updateAsPayloads(Integer userId, UsersResponse usersRequest, MyUserDetailsImpl myUserDetails) {

        boolean hasPermissions = commonsService.hasRole(myUserDetails, RoleType.ROLE_ALL_SUPER);

        if (!hasPermissions) {
            return TMSResponse.buildTMSResponse("You don't have permissions", null, HttpStatusCodesResponseDTO.NOT_ACCEPTABLE);
        }

        if (!ObjectUtils.allNotNull(userId)) {
            return TMSResponse.buildTMSResponse("User not found", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        OrganizationType organizationType = OrganizationType.findByValue(organizationId);
        List<DomainType> domainTypes = DomainType.findByOrganizationType(organizationType);

        UsersEntity usersEntity = usersRepository.getOne(userId);

        if (!ObjectUtils.allNotNull(usersEntity)) {
            return TMSResponse.buildTMSResponse("User not found", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (StringUtility.isAnyEmpty(usersRequest.getUsername())) {
            return TMSResponse.buildTMSResponse("Username is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!ValidationUtils.isVerifiedAsEmail(usersRequest.getUsername())) {
            return TMSResponse.buildTMSResponse("Invalid Username", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!DomainType.isDomainNameEndWiths(domainTypes, usersRequest.getUsername())) {
            return TMSResponse.buildTMSResponse("Unsupported Username domain", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (usersRequest.getOverridePassword() && StringUtility.isEmpty(usersRequest.getPassword())) {
            return TMSResponse.buildTMSResponse("Password is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (usersRequest.getOverridePassword() && !ValidationUtils.isVerifiedAsPassword(usersRequest.getPassword())) {
            return TMSResponse.buildTMSResponse(
                    "Password contains at least: " +
                            "8 characters, " +
                            "one digit, " +
                            "one upper case alphabet, " +
                            "one lower case alphabet, " +
                            "one special character which includes !@#$%&*()-+=^. " +
                            "It does not contain any white space",
                    null,
                    HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (StringUtility.isAnyEmpty(usersRequest.getEmail())) {
            return TMSResponse.buildTMSResponse("Email is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (!ValidationUtils.isVerifiedAsEmail(usersRequest.getEmail())) {
            return TMSResponse.buildTMSResponse("Invalid Email", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (StringUtility.isAnyEmpty(usersRequest.getFullName())) {
            return TMSResponse.buildTMSResponse("Full Name is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        if (StringUtility.isAnyEmpty(usersRequest.getPhone())) {
            return TMSResponse.buildTMSResponse("Phone is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
        }

        /* begin::Handle SIP */
        if (!usersRequest.getPhone().toLowerCase().startsWith(PHONE_SIP_PREFIX.toLowerCase())) {
            usersRequest.setPhone(StringUtility.trimSingleWhitespace(PHONE_SIP_PREFIX.concat(usersRequest.getPhone())).toUpperCase());
        } else {
            usersRequest.setPhone(StringUtility.trimSingleWhitespace(usersRequest.getPhone()).toUpperCase());
        }
        /* begin::Handle SIP */

        usersEntity.setUserType(UsersType.findBy(usersRequest.getUserType()).getName());
        usersEntity.setFullName(StringUtility.trimSingleWhitespace(StringUtility.capitalizeEachWords(usersRequest.getFullName())));
        usersEntity.setEmail(StringUtility.trimSingleWhitespace(usersRequest.getEmail()));
        usersEntity.setPhone(usersRequest.getPhone());
        usersEntity.setBirthday(usersRequest.getBirthday());
        usersEntity.setHomeAddress(usersRequest.getHomeAddress());
        usersEntity.setHomePhone_1(usersRequest.getHomePhone_1());
        usersEntity.setHomePhone_2(usersRequest.getHomePhone_2());
        usersEntity.setPersonalMail(usersRequest.getPersonalMail());
        usersEntity.setWorkMail(usersRequest.getWorkMail());
        usersEntity.setPersonalPhone_1(usersRequest.getPersonalPhone_1());
        usersEntity.setPersonalPhone_2(usersRequest.getPersonalPhone_2());
        usersEntity.setUsername(StringUtility.trimSingleWhitespace(usersRequest.getUsername()));

        if (usersRequest.getOverridePassword()) {
            usersEntity.setPassword(passwordEncoder.encode(usersRequest.getPassword()));
        }

        if (ObjectUtils.allNotNull(usersRequest.getPasswordUpdateTime())) {
            usersEntity.setPasswordUpdateTime(usersRequest.getPasswordUpdateTime());
        }

        if (usersRequest.isActive()) {
            usersEntity.setIsActive(0);
        } else {
            usersEntity.setIsActive(1);
        }

        usersRepository.save(usersEntity);
        return TMSResponse.buildTMSResponse("User updated successfully", null, HttpStatusCodesResponseDTO.OK);
    }


}
