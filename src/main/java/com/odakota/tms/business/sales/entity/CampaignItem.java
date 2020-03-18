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
@Table(name = "campaign_item_tbl")
public class CampaignItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "campaign_id")
    private long campaignId;

    @Column(name = "product_id")
    private long productId;
}
