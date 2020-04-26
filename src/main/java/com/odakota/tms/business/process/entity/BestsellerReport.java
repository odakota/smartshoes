package com.odakota.tms.business.process.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "bestseller_report_tbl")
public class BestsellerReport implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "report_date")
    private String reportDate;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "total_sale")
    private long totalSale;

    @Column(name = "order_number")
    private long orderNumber;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @PrePersist
    public void prePersist(){
        createDate = new Date();
    }
}
