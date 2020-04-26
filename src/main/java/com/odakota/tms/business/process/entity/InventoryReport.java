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
@Table(name = "inventory_report_tbl")
public class InventoryReport implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "report_date")
    @Temporal(TemporalType.DATE)
    private Date reportDate;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "pre_total")
    private long preTotal;

    @Column(name = "input_total")
    private long inputTotal;

    @Column(name = "output_total")
    private long outputTotal;

    @Column(name = "final_total")
    private long finalTotal;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @PrePersist
    public void prePersist(){
        createDate = new Date();
    }
}
