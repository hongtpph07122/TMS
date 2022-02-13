package com.tms.api.entity;

import com.tms.api.entity.ChatId;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.ElementCollection;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import javax.persistence.*;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Entity
@DynamicUpdate
@Transactional
@Table(name = "or_user_deleted")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class OrUserDelete implements Serializable{
    private static final long serialVersionUID = -5688043643278441710L;

    @Id
//    @SequenceGenerator(name="seq_user_id", sequenceName = "seq_user_id", allocationSize=1)
//    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user_id")
    @Column(name = "user_id", updatable = false, nullable = false)
    private Integer user_id;
    @Column(name="org_id")
    private Integer org_id;
    @Column(name="user_type")
    private String user_type;
    @Column(name="user_name")
    private String user_name;
    @Column(name="password")
    private String password;
    @Column(name="user_lock")
    private Integer user_lock;
    @Column(name="fullname")
    private String fullname;
    @Column(name="email")
    private String email;
    @Column(name="phone")
    private String phone;
    @Column(name="birthday")
    private Date birthday;
    @Column(name="modifyby")
    private Integer modifyby;
    @Column(name="modifydate")
    private Timestamp modifydate;
    @Column(name="home_phone_1")
    private String home_phone_1;
    @Column(name="home_phone_2")
    private String home_phone_2;
    @Column (name="personal_phone_1")
    private String personal_phone_1;
    @Column (name="personal_phone_2")
    private String personal_phone_2;
    @Column (name="work_mail")
    private String work_mail;
    @Column (name="personal_mail")
    private String personal_mail;
    @Column (name="home_address")
    private String home_address;
    @Type(type = "jsonb")
    @Column (name="chat_id", columnDefinition = "jsonb")
//    @Basic(fetch = FetchType.EAGER)
    private List<ChatId> chat_id;
    @Column (name="force_change_password", columnDefinition = "boolean default false", nullable = true)
    private Boolean force_change_password;
    @Column (name="failed_login_count")
    private Integer failed_login_count;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getOrg_id() {
        return org_id;
    }

    public void setOrg_id(Integer org_id) {
        this.org_id = org_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUser_lock() {
        return user_lock;
    }

    public void setUser_lock(Integer user_lock) {
        this.user_lock = user_lock;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public Integer getModifyby() {
        return modifyby;
    }

    public void setModifyby(Integer modifyby) {
        this.modifyby = modifyby;
    }

    public Timestamp getModifydate() {
        return modifydate;
    }

    public void setModifydate(Timestamp modifydate) {
        this.modifydate = modifydate;
    }

    public String getHome_phone_1() {
        return home_phone_1;
    }

    public void setHome_phone_1(String home_phone_1) {
        this.home_phone_1 = home_phone_1;
    }

    public String getHome_phone_2() {
        return home_phone_2;
    }

    public void setHome_phone_2(String home_phone_2) {
        this.home_phone_2 = home_phone_2;
    }

    public String getPersonal_phone_1() {
        return personal_phone_1;
    }

    public void setPersonal_phone_1(String personal_phone_1) {
        this.personal_phone_1 = personal_phone_1;
    }

    public String getPersonal_phone_2() {
        return personal_phone_2;
    }

    public void setPersonal_phone_2(String personal_phone_2) {
        this.personal_phone_2 = personal_phone_2;
    }

    public String getWork_mail() {
        return work_mail;
    }

    public void setWork_mail(String work_mail) {
        this.work_mail = work_mail;
    }

    public String getPersonal_mail() {
        return personal_mail;
    }

    public void setPersonal_mail(String personal_mail) {
        this.personal_mail = personal_mail;
    }

    public String getHome_address() {
        return home_address;
    }

    public void setHome_address(String home_address) {
        this.home_address = home_address;
    }

    public List<ChatId> getChat_id() {
        return chat_id;
    }

    public void setChat_id(List<ChatId> chat_id) {
        this.chat_id = chat_id;
    }

    public boolean isForce_change_password() {
        return force_change_password;
    }

    public void setForce_change_password(Boolean force_change_password) {
        this.force_change_password = force_change_password;
    }

    public Integer getFailed_login_count() {
        return failed_login_count;
    }

    public void setFailed_login_count(Integer failed_login_count) {
        this.failed_login_count = failed_login_count;
    }

}