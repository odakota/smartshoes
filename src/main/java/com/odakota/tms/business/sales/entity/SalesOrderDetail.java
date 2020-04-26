package com.odakota.tms.business.sales.entity;

import com.odakota.tms.system.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "sales_order_detail_tbl")
public class SalesOrderDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "pre_price")
    private Long prePrice;

    @Column(name = "lst_price")
    private Long lstPrice;

    @Column(name = "sales_order_id")
    private Long saleOrderId;

    @Column(name = "amount_product")
    private int amountProduct;

    @Column(name = "discount")
    private Long discount;

    @Column(name = "discount_reason")
    private String discountReason;

    @Column(name = "allocation_product_id")
    private Long allocationProductId;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_code")
    private String productCode;

    @Transient
    private String productName;

    public SalesOrderDetail() {
    }

    public SalesOrderDetail(String productCode, String productName, Long prePrice, Long lstPrice, int amountProduct) {
        this.prePrice = prePrice;
        this.lstPrice = lstPrice;
        this.productName = productName;
        this.productCode = productCode;
        this.amountProduct = amountProduct;
    }
}
