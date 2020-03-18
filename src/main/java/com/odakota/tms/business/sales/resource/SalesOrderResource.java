package com.odakota.tms.business.sales.resource;

import com.odakota.tms.business.sales.entity.SalesOrder;
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
public class SalesOrderResource extends BaseResource<SalesOrder> {

    private Long customerId;

    private String customerName;

    private Integer saleType;

    private Integer orderType;

    private Long tax;

    private Long totalAmount;

    private Integer status;

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalesOrderCondition extends BaseCondition {

        private Date startDate;

        private Date endDate;

        private Long branchId;
    }
}
