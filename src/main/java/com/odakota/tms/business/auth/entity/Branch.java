package com.odakota.tms.business.auth.entity;

import com.odakota.tms.system.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "branch_tbl")
public class Branch extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "branch_code", updatable = false)
    private String branchCode;

    @Column(name = "branch_phone")
    private String branchPhone;

    @Column(name = "province_id")
    private Long provinceId;

    @Column(name = "district_id")
    private Long districtId;

    @Column(name = "ward_id")
    private Long wardId;

    @Column(name = "street")
    private String street;
}
