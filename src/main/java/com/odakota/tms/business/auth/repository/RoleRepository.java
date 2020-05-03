package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.Role;
import com.odakota.tms.business.auth.resource.RoleResource.RoleCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface RoleRepository extends BaseRepository<Role, RoleCondition> {

    /**
     * String build = "where b.deletedFlag = false";
     * <p>
     * if(condition.roleName != null){ build = build + "r.roleName like '%var%' or r.roleCode like '%var%'"; }
     * <p>
     * if(userSession.branchId != null){ build = build + "b.branchId = condition.branchId"; }
     */
    @Query("select r from Role r where r.deletedFlag = false and (:#{#condition.roleName} is null " +
           "or (r.roleName like %:#{#condition.roleName}% or r.roleCode like %:#{#condition.roleName}%))" +
           "and ((:#{@userSession.branchId} is null and (:#{#condition.branchId} is null " +
           "or r.branchId = :#{#condition.branchId})) " +
           "or r.branchId = :#{@userSession.branchId})")
    Page<Role> findByCondition(@Param("condition") RoleCondition condition, Pageable pageable);

    long countByBranchIdAndDeletedFlagFalse(Long branchId);
}
