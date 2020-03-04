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
@Table(name = "product_assert_tbl")
public class ProductAssert extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "type")
    private Integer type;

    @Column(name = "path")
    private String path;

    @Column(name = "sort_order")
    private Integer sortOrder;
}
