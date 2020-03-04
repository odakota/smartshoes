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
@Table(name = "category_tbl")
public class Category extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name")
    private String name;
}
