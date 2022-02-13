package com.tms.api.service;

import com.tms.api.entity.OrRole;
import com.tms.api.entity.OrUser;
import com.tms.api.entity.OrUserDelete;
import com.tms.api.entity.OrUserRole;

import java.util.List;

public interface AgentManagermentService {
    OrUser saveUser(OrUser orUser);
    List<OrUser> getUserByName(String userName);
    List<OrUser> getUserById(Integer userId);
    List<OrUser> getUserByOrgId(Integer orgId);
    void deleteById(Integer userId);
    List<OrRole> getRoleByName(String name);
    List<OrRole> getUserType();
    OrUserRole saveUserRole(OrUserRole orUserRole);
    OrUserRole getUserRoleByUserId(Integer userId);
    OrUserDelete saveUserDelete(OrUserDelete userDelete);
    void deleteUserRoleByUserId(Integer userId);
}