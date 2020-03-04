package com.odakota.tms.business.auth.repository;

import com.odakota.tms.business.auth.entity.LoginFailed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface LoginFailedRepository extends JpaRepository<LoginFailed, Long> {

    long countByUsernameOrPhone(String username, String phone);

    @Modifying
    @Transactional
    @Query("delete from LoginFailed where (username = ?1 or phone = ?2) and isCustomer = ?3")
    void deleteByUsernameOrPhone(String username, String phone, boolean isCustomer);
}
