package com.odakota.tms.business.receipt.entity;

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
@Table(name = "receipt_detail_tbl")
public class ReceiptDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "receipt_id")
    private Long receiptId;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "detail")
    private String detail;

    @Column(name = "note")
    private String note;
}
