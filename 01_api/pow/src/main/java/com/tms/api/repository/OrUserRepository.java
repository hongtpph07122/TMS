package com.tms.api.repository;

import com.tms.api.entity.OrUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface OrUserRepository extends JpaRepository<OrUser, Long>{
    @Query(value = "from com.tms.api.entity.OrUser u where u.user_name = ?1 ")
     List<OrUser> getUserByName(String userName);

    @Query(value = "from com.tms.api.entity.OrUser u where u.user_id = ?1 ")
    List<OrUser> getUserById(Integer userId);

    @Query(value = "from com.tms.api.entity.OrUser u where u.org_id = ?1 ")
    List<OrUser> getUserByOrgId(Integer orgId);

    @Query(value = "from com.tms.api.entity.OrUser user where user.user_id = ?1 and user.user_lock <> 1")
    OrUser findOne(Integer userId);

    @Modifying
    @Transactional
    @Query(value = "delete from com.tms.api.entity.OrUser u where u.user_id = ?1 ")
    void deleteById(Integer userId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE com.tms.api.entity.OrUser SET last_status = ?1, last_status_code = ?2, last_status_message = ?3," +
            " modifydate = ?4 WHERE user_id = ?5")
    void updateStatusById(String status, Integer statusCode, String message, Date actionTime, Integer userId);

    @Query(value = "select 1 from com.tms.api.entity.OrUser")
    Integer testConnection();
}
