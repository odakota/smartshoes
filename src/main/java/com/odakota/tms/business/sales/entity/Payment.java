package com.odakota.tms.business.sales.entity;

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
@Table(name = "payment_tbl")
public class Payment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;

    @Column(name = "fee")
    private int fee;

    @Column(name = "lower_limit")
    private long lowerLimit;

    @Column(name = "upper_limit")
    private long upperLimit;
}
