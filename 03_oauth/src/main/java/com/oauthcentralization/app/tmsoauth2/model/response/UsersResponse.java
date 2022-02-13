package com.oauthcentralization.app.tmsoauth2.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;

import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersResponse {

    private Integer userId;
    private Integer organizationId;
    private String userType;
    private String username;
    private String password;
    private boolean active;
    @JsonIgnore
    private Integer activeStandard;
    private String fullName;
    private String email;
    private String phone;
    private Date birthday;
    private Integer modifiedBy;
    private Date modifiedDate;
    private String homePhone_1;
    private String homePhone_2;
    private String personalPhone_1;
    private String personalPhone_2;
    private String workMail;
    private String personalMail;
    private String homeAddress;
    private Boolean forceChangePassword;
    private Integer failedLoginCount;
    private Date passwordUpdateTime;
    private Boolean expired;
    private List<Integer> rolesId;
    private List<RolesResponse> roles;
    private Boolean overridePassword;
    private List<GroupResponse> groupsDetail;
    private List<TeamsResponse> teamsDetail;
    private String groupsName;
    private String teamsName;
    private String groupsId;
    private String teamsId;

    public UsersResponse() {
        setOverridePassword(false);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer orgId) {
        this.organizationId = orgId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getActiveStandard() {
        return activeStandard;
    }

    public void setActiveStandard(Integer activeStandard) {
        setActive(!ObjectUtils.allNotNull(activeStandard) || activeStandard != 1);
        this.activeStandard = activeStandard;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getHomePhone_1() {
        return homePhone_1;
    }

    public void setHomePhone_1(String homePhone_1) {
        this.homePhone_1 = homePhone_1;
    }

    public String getHomePhone_2() {
        return homePhone_2;
    }

    public void setHomePhone_2(String homePhone_2) {
        this.homePhone_2 = homePhone_2;
    }

    public String getPersonalPhone_1() {
        return personalPhone_1;
    }

    public void setPersonalPhone_1(String personalPhone_1) {
        this.personalPhone_1 = personalPhone_1;
    }

    public String getPersonalPhone_2() {
        return personalPhone_2;
    }

    public void setPersonalPhone_2(String personalPhone_2) {
        this.personalPhone_2 = personalPhone_2;
    }

    public String getWorkMail() {
        return workMail;
    }

    public void setWorkMail(String workMail) {
        this.workMail = workMail;
    }

    public String getPersonalMail() {
        return personalMail;
    }

    public void setPersonalMail(String personalMail) {
        this.personalMail = personalMail;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Boolean getForceChangePassword() {
        return forceChangePassword;
    }

    public void setForceChangePassword(Boolean forceChangePassword) {
        this.forceChangePassword = forceChangePassword;
    }

    public Integer getFailedLoginCount() {
        return failedLoginCount;
    }

    public void setFailedLoginCount(Integer failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    public Date getPasswordUpdateTime() {
        return passwordUpdateTime;
    }

    public void setPasswordUpdateTime(Date passwordUpdateTime) {
        this.passwordUpdateTime = passwordUpdateTime;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public List<Integer> getRolesId() {
        return rolesId;
    }

    public void setRolesId(List<Integer> rolesId) {
        this.rolesId = rolesId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<RolesResponse> getRoles() {
        return roles;
    }

    public void setRoles(List<RolesResponse> roles) {
        this.roles = roles;
    }

    public Boolean getOverridePassword() {
        return overridePassword;
    }

    public void setOverridePassword(Boolean overridePassword) {
        this.overridePassword = overridePassword;
    }

    public List<GroupResponse> getGroupsDetail() {
        return groupsDetail;
    }

    public void setGroupsDetail(List<GroupResponse> groupsDetail) {
        this.groupsDetail = groupsDetail;
    }

    public List<TeamsResponse> getTeamsDetail() {
        return teamsDetail;
    }

    public void setTeamsDetail(List<TeamsResponse> teamsDetail) {
        this.teamsDetail = teamsDetail;
    }

    public String getGroupsName() {
        return groupsName;
    }

    public void setGroupsName(String groupsName) {
        this.groupsName = groupsName;
    }

    public String getTeamsName() {
        return teamsName;
    }

    public void setTeamsName(String teamsName) {
        this.teamsName = teamsName;
    }

    public String getGroupsId() {
        return groupsId;
    }

    public void setGroupsId(String groupsId) {
        this.groupsId = groupsId;
    }

    public String getTeamsId() {
        return teamsId;
    }

    public void setTeamsId(String teamsId) {
        this.teamsId = teamsId;
    }
}
