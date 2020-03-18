package com.odakota.tms.business.product.entity;

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
@Table(name = "product_tbl")
public class Product extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "price")
    private Long price;

    @Column(name = "company_sales_price")
    private Long companySalesPrice;

    @Column(name = "sale_start_at")
    private Date saleStartAt;

    @Column(name = "sale_end_at")
    private Date saleEndAt;

    @Column(name = "description")
    private Date description;

    @Column(name = "is_company_sales")
    private boolean isCompanySales;

    @Column(name = "path")
    private String path;
}
