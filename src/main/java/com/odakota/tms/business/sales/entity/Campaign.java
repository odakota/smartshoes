package com.odakota.tms.business.sales.entity;

import com.odakota.tms.enums.sale.DiscountType;
import com.odakota.tms.mapper.convert.DiscountTypeConverter;
import com.odakota.tms.system.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "campaign_tbl")
public class Campaign extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;

    @Column(name = "branch_id")
    private Long branchId;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "description")
    private String description;

    @Column(name = "discount_type")
    @Convert(converter = DiscountTypeConverter.class)
    private DiscountType discountType;

    @Column(name = "discount_value")
    private Long discountValue;
}
