package com.odakota.tms.business.receipt.entity;

import com.odakota.tms.system.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "receipt_tbl")
public class Receipt extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "code")
    private String code;

    @Column(name = "bill_code")
    private String billCode;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "deliver")
    private String deliver;

    @Column(name = "receipt_date")
    private Date receiptDate;

    @Column(name = "warehouse")
    private String warehouse;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "note")
    private String note;

    @Column(name = "approved_flag")
    private Boolean approvedFlag;
}
