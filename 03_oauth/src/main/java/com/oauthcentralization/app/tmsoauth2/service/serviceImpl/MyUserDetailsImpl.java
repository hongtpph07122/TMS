package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.variables.RolesVariable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsImpl extends UsersResponse implements UserDetails {
    @JsonIgnore
    private String password;
    private String username;
    private Collection<? extends GrantedAuthority> authorities;

    public MyUserDetailsImpl() {
        super();
    }

    public static MyUserDetailsImpl buildUser(UsersResponse usersResponse, List<String> roles) {
        MyUserDetailsImpl myUserDetailsImpl = new MyUserDetailsImpl();
        myUserDetailsImpl.setUserId(usersResponse.getUserId());
        myUserDetailsImpl.setOrganizationId(usersResponse.getOrganizationId());
        myUserDetailsImpl.setUserType(usersResponse.getUserType());
        myUserDetailsImpl.setUsername(usersResponse.getUsername());
        myUserDetailsImpl.setActiveStandard(usersResponse.getActiveStandard());
        myUserDetailsImpl.setFullName(usersResponse.getFullName());
        myUserDetailsImpl.setEmail(usersResponse.getEmail());
        myUserDetailsImpl.setPhone(usersResponse.getPhone());
        myUserDetailsImpl.setBirthday(usersResponse.getBirthday());
        myUserDetailsImpl.setModifiedBy(usersResponse.getModifiedBy());
        myUserDetailsImpl.setModifiedDate(usersResponse.getModifiedDate());
        myUserDetailsImpl.setHomePhone_1(usersResponse.getHomePhone_1());
        myUserDetailsImpl.setHomePhone_2(usersResponse.getHomePhone_2());
        myUserDetailsImpl.setWorkMail(usersResponse.getWorkMail());
        myUserDetailsImpl.setPersonalMail(usersResponse.getPersonalMail());
        myUserDetailsImpl.setHomeAddress(usersResponse.getHomeAddress());
        myUserDetailsImpl.setPersonalPhone_1(usersResponse.getPersonalPhone_1());
        myUserDetailsImpl.setPersonalPhone_2(usersResponse.getPersonalPhone_2());
        myUserDetailsImpl.setForceChangePassword(usersResponse.getForceChangePassword());
        myUserDetailsImpl.setPasswordUpdateTime(usersResponse.getPasswordUpdateTime());
        myUserDetailsImpl.setExpired(usersResponse.getExpired());
        myUserDetailsImpl.setPassword(usersResponse.getPassword());
        myUserDetailsImpl.setRolesId(usersResponse.getRolesId());
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(StringUtils.trimAllWhitespace("ROLE_".concat(role).toUpperCase())))
                .collect(Collectors.toList());
        myUserDetailsImpl.setAuthorities(authorities);
        return myUserDetailsImpl;
    }

    public static User buildUserIsBlocked() {
        return new User("tms-bot-available", "tms@2021", false, true, false, false, Collections.singletonList(new SimpleGrantedAuthority(RolesVariable.ROLE_ANONYMOUS)));
    }

    public static User buildUserFailed() {
        return new User("tms-bot-failed", "tms@2021", false, true, false, true, Collections.singletonList(new SimpleGrantedAuthority(RolesVariable.ROLE_ANONYMOUS)));
    }

    public static User buildUserFailedIsExpired() {
        return new User("tms-bot-failed", "tms@2021", true, false, true, true, Collections.singletonList(new SimpleGrantedAuthority(RolesVariable.ROLE_ANONYMOUS)));
    }

    public static User buildUserUnExisted() {
        return new User("tms-bot-available", "tms@2021", true, true, false, true, Collections.singletonList(new SimpleGrantedAuthority(RolesVariable.ROLE_ANONYMOUS)));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
