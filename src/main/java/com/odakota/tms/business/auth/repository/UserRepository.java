package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.resource.UserResource.UserCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface UserRepository extends BaseRepository<User, UserCondition> {

    @Query("select e from User e where e.deletedFlag = false " +
           "and (:#{#condition.email} is null or e.email like %:#{#condition.email}%) " +
           "and (:#{#condition.phone} is null or e.phone like %:#{#condition.phone}%) " +
           "and (:#{#condition.lockFlag} is null or e.lockFlag = :#{#condition.lockFlag}) " +
           "and (:#{#condition.username} is null or e.username like %:#{#condition.username}% " +
           "or e.fullName like %:#{#condition.username}%) " +
           "and ((:#{@userSession.branchId} is null and (:#{#condition.branchId} is null " +
           "or e.branchId = :#{#condition.branchId})) or e.branchId = :#{@userSession.branchId}) " +
           "and (:#{#condition.sex} is null or e.sex = :#{T(com.odakota.tms.enums.auth.Gender).of(#condition.sex)}) ")
    Page<User> findByCondition(UserCondition condition, Pageable pageable);

    Optional<User> findByUsernameAndDeletedFlagFalse(String username);

    Optional<User> findByUsernameOrPhoneAndDeletedFlagFalse(String username, String phone);

    long countByBranchIdAndDeletedFlagFalse(Long branchId);

    @Query(value = "select u.* from user_tbl u where u.deleted_flag = false " +
                   "and date_part('day', u.birth_date) = date_part('day', CURRENT_DATE)" +
                   "and date_part('month', u.birth_date) = date_part('month', CURRENT_DATE) ", nativeQuery = true)
    List<User> findByBirthDayToDay();

    @Query("select u.id from User u where u.deletedFlag = false")
    List<Long> findAllUserId();
}
