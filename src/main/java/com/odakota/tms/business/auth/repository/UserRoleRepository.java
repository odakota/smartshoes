package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.UserRole;
import com.odakota.tms.business.auth.resource.UserRoleResource.UserRoleCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface UserRoleRepository extends BaseRepository<UserRole, UserRoleCondition> {

    List<UserRole> findByUserIdAndDeletedFlagFalse(Long userId);

    @Modifying
    @Transactional
    @Query("update UserRole set deletedFlag = true, updatedBy = :#{@userSession.userId}, " +
           "updatedDate = :#{T(java.time.LocalDateTime).now()} where roleId = ?1")
    void deleteByRoleId(Long roleId);

    @Modifying
    @Transactional
    @Query("update UserRole set deletedFlag = true, updatedBy = :#{@userSession.userId}, " +
           "updatedDate = :#{T(java.time.LocalDateTime).now()} where userId = ?1")
    void deleteByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("update UserRole set deletedFlag = true, updatedBy = :#{@userSession.userId}, " +
           "updatedDate = :#{T(java.time.LocalDateTime).now()} where roleId in ?1 and " +
           "roleId <> :#{T(com.odakota.tms.constant.Constant).ROLE_ID_DEFAULT}")
    void deleteByRoleId(List<Long> userId);

    List<UserRole> findByRoleIdAndDeletedFlagFalse(Long roleId);
}
