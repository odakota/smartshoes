package com.odakota.tms.business.sales.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.odakota.tms.business.sales.entity.Campaign;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.system.base.BaseCondition;
import com.odakota.tms.system.base.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignResource extends BaseResource<Campaign> {

    private String name;

    private Long branchId;

    @JsonFormat(pattern = Constant.YYYY_MM_DD_HH_MM_SS, timezone = "GMT+7")
    private Date startDate;

    @JsonFormat(pattern = Constant.YYYY_MM_DD_HH_MM_SS, timezone = "GMT+7")
    private Date endDate;

    private String description;

    private Integer discountType;

    private Long discountValue;

    private Boolean selectAll;

    private List<Long> selectProduct;

    private List<Long> selectCategory;


    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CampaignCondition extends BaseCondition {

        private String name;

        private Date startDate;

        private Date endDate;

        private Long branchId;
    }
}
