package com.odakota.tms.business.sales.entity;

import com.odakota.tms.enums.sale.OrderStatus;
import com.odakota.tms.enums.sale.OrderType;
import com.odakota.tms.enums.sale.SaleType;
import com.odakota.tms.mapper.convert.OrderStatusConverter;
import com.odakota.tms.mapper.convert.OrderTypeConverter;
import com.odakota.tms.mapper.convert.SaleTypeConverter;
import com.odakota.tms.system.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "sales_order_tbl")
public class SalesOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "sale_type")
    @Convert(converter = SaleTypeConverter.class)
    private SaleType saleType;

    @Column(name = "order_type")
    @Convert(converter = OrderTypeConverter.class)
    private OrderType orderType;

    @Column(name = "tax")
    private Long tax;

    @Column(name = "total_amount")
    private Long totalAmount;

    @Column(name = "status")
    @Convert(converter = OrderStatusConverter.class)
    private OrderStatus status;

    @Column(name = "branch_id")
    private Long branchId;
}
