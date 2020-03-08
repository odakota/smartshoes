package com.odakota.tms.business.product.entity;

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
@Table(name = "allocation_product_tbl")
public class AllocationProduct extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "color_id")
    private Long colorId;

    @Column(name = "size_id")
    private Long sizeId;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "total")
    private Integer total;
}
