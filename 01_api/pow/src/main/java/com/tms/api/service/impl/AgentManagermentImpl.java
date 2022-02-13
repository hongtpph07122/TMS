package com.tms.api.service.impl;
import com.tms.api.entity.OrRole;
import com.tms.api.entity.OrUser;
import com.tms.api.entity.OrUserDelete;
import com.tms.api.entity.OrUserRole;
import com.tms.api.repository.*;
import com.tms.api.repository.OrRoleRepository;
import com.tms.api.repository.OrUserDeleteRepository;
import com.tms.api.repository.OrUserRepository;
import com.tms.api.repository.OrUserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.tms.api.service.AgentManagermentService;
import java.util.List;

@Service
public class AgentManagermentImpl implements AgentManagermentService {
    @Autowired
    OrUserRepository userRepository;
    @Autowired
    OrRoleRepository roleRepository;
    @Autowired
    OrUserRoleRepository userRoleRepository;
    @Autowired
    OrUserDeleteRepository userDeleteRepository;

    @Override
    public List<OrUser> getUserByName(String userName) {
        List<OrUser> users = userRepository.getUserByName(userName);
        if(users.isEmpty()){
            return null;
        }
        return users;
    }

    @Override
    public List<OrUser> getUserById(Integer userId) {
        List<OrUser> users = userRepository.getUserById(userId);
        if(users.isEmpty()){
            return null;
        }
        return users;
    }

    @Override
    public List<OrUser> getUserByOrgId(Integer orgId) {
        List<OrUser> users = userRepository.getUserByOrgId(orgId);
        if(users.isEmpty()){
            return null;
        }
        return users;
    }

    @Override
    public void deleteById(Integer userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public List<OrRole> getRoleByName(String name) {
        List<OrRole> roles = roleRepository.getByName(name);
        if(roles.isEmpty()){
            return null;
        }
        return roles;
    }

    @Override
    public List<OrRole> getUserType() {
        List<OrRole> userTypes = roleRepository.getUserType();
        if(userTypes.isEmpty()){
            return null;
        }
        return userTypes;
    }

    @Override
    public OrUserRole saveUserRole(OrUserRole orUserRole) {
        OrUserRole userRole = userRoleRepository.save(orUserRole);
        return userRole;
    }

    @Override
    public OrUser saveUser(OrUser orUser){
        OrUser user = userRepository.save(orUser);
        return user;
    }

    @Override
    public OrUserRole getUserRoleByUserId(Integer userId) {
        OrUserRole orUserRole = userRoleRepository.getByUserId(userId);
        return orUserRole;
    }

    @Override
    public OrUserDelete saveUserDelete(OrUserDelete userDelete){
        return userDeleteRepository.save(userDelete);
    }

    @Override
    public void deleteUserRoleByUserId(Integer userId){
        userRoleRepository.deleteByUserId(userId);
    }
}