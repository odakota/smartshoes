package com.odakota.tms.business.product.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.system.base.BaseCondition;
import com.odakota.tms.system.base.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResource extends BaseResource<Product> {

    private Long branchId;

    private Long categoryId;

    private String name;

    private String code;

    private Long price;

    private Long companySalesPrice;

    @JsonFormat(pattern = Constant.YYYY_MM_DD_HH_MM_SS, timezone = "GMT+7")
    private Date saleStartAt;

    @JsonFormat(pattern = Constant.YYYY_MM_DD_HH_MM_SS, timezone = "GMT+7")
    private Date saleEndAt;

    private String description;

    private boolean isCompanySales;

    private String path;

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCondition extends BaseCondition {

        private Long categoryId;
    }
}
