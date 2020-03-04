package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.PermissionRole;
import com.odakota.tms.business.auth.resource.PermissionRoleResource.PermissionRoleCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface PermissionRoleRepository extends BaseRepository<PermissionRole, PermissionRoleCondition> {

    List<PermissionRole> findByRoleIdAndDeletedFlagFalse(Long roleId);

    Optional<PermissionRole> findByRoleIdAndPermissionIdAndDeletedFlagFalse(Long roleId, Long permissionId);

    boolean existsByApiIdAndRoleIdInAndDeletedFlagFalse(String apiId, List<Long> roleIds);

    @Modifying
    @Transactional
    @Query("update PermissionRole set deletedFlag = true, updatedBy = :#{@userSession.userId}, " +
           "updatedDate = :#{T(java.time.LocalDateTime).now()} where roleId = ?1")
    void deleteByRoleId(Long roleId);
}
