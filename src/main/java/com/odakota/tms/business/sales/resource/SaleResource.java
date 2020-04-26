package com.odakota.tms.business.sales.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaleResource {

    private Long customerId;

    private Long paymentMethod;

    private Long totalAmount;

    private List<SelectProductResource> productSelectedList;

    @Setter @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelectProductResource {

        private Long productId;

        private Long price;

        private String colorId;

        private String sizeId;

        private Integer total;
    }
}
