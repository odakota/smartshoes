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
@Table(name = "sales_order_detail_tbl")
public class SalesOrderDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "price")
    private Long price;

    @Column(name = "amount_product")
    private int amountProduct;

    @Column(name = "discount")
    private Long discount;

    @Column(name = "discount_reason")
    private String discountReason;

    @Column(name = "allocation_product_id")
    private Long allocationProductId;
}
