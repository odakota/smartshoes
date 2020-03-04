package com.odakota.tms.business.auth.entity;

import com.odakota.tms.enums.auth.Gender;
import com.odakota.tms.mapper.convert.GenderConverter;
import com.odakota.tms.system.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "user_tbl")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "user_name")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "sex")
    @Convert(converter = GenderConverter.class)
    private Gender sex;

    @Column(name = "birth_date")
    private Date birthDay;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "province_id")
    private Long provinceId;

    @Column(name = "district_id")
    private Long districtId;

    @Column(name = "ward_id")
    private Long wardId;

    @Column(name = "street")
    private String street;

    @Column(name = "lock_flag")
    private boolean lockFlag;
}
