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
@Table(name = "payment_detail_tbl")
public class PaymentDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "bank")
    private String bank;

    @Column(name = "account_number")
    private String accountNumber;
}
