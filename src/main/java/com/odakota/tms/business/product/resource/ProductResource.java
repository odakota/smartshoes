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

    private Long categoryId;

    private String name;

    private String code;

    private Long price;

    private Long lstPrice;

    private Long companySalesPrice = 0L;

    @JsonFormat(pattern = Constant.YYYY_MM_DD)
    private Date saleStartAt;

    @JsonFormat(pattern = Constant.YYYY_MM_DD)
    private Date saleEndAt;

    private String description;

    private boolean isCompanySales;

    private String path;

    private Long total;

    public ProductResource(Long id, Long total) {
        super(id);
        this.total = total;
    }

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCondition extends BaseCondition {

        private Long categoryId;

        private Long branchId;

        private Date saleStartAt;

        private Date saleEndAt;
    }
}
