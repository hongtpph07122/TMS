package com.oauthcentralization.app.tmsoauth2.entities.baseEntity;

import javax.persistence.*;
import java.util.Date;

import static com.oauthcentralization.app.tmsoauth2.variables.ColumnsVariable.*;


@MappedSuperclass
public class UsersBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = USER_ID, nullable = false)
    private Integer userId;

    @Column(name = ORG_ID)
    private Integer orgId;

    @Column(name = USER_TYPE)
    private String userType;

    @Column(name = USER_NAME)
    private String username;

    @Column(name = PASSWORD)
    private String password;

    @Column(name = USER_LOCK)
    private Integer isActive;

    @Column(name = FULL_NAME)
    private String fullName;

    @Column(name = EMAIL)
    private String email;

    @Column(name = PHONE)
    private String phone;

    @Column(name = BIRTHDAY)
    private Date birthday;

    @Column(name = MODIFY_BY)
    private Integer modifiedBy;

    @Column(name = MODIFY_DATE)
    private Date modifiedDate;

    @Column(name = HOME_PHONE_1)
    private String homePhone_1;

    @Column(name = HOME_PHONE_2)
    private String homePhone_2;

    @Column(name = PERSONAL_PHONE_1)
    private String personalPhone_1;

    @Column(name = PERSONAL_PHONE_2)
    private String personalPhone_2;

    @Column(name = WORK_MAIL)
    private String workMail;

    @Column(name = PERSONAL_MAIL)
    private String personalMail;

    @Column(name = HOME_ADDRESS)
    private String homeAddress;

    @Column(name = FORCE_CHANGE_PASSWORD)
    private Boolean forceChangePassword;

    @Column(name = FAILED_LOGIN_COUNT)
    private Integer failedLoginCount;

    @Column(name = PASSWORD_UPDATE_TIME)
    private Date passwordUpdateTime;

    @Column(name = IS_EXPIRED)
    private Boolean isExpired;

    public UsersBaseEntity() {
        setIsActive(0);
        setIsExpired(false);
        setForceChangePassword(false);
        setModifiedDate(new Date());
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
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

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
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

    public void setPersonalPhone_2(String getPersonalPhone_2) {
        this.personalPhone_2 = getPersonalPhone_2;
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

    public Boolean getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(Boolean isExpired) {
        this.isExpired = isExpired;
    }
}
