package com.odakota.tms.business.receipt.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptDetailResource {

    private Long id;

    private String amount;

    private String productCode;

    private String detail;

    private Boolean isNew;

    private Boolean editable;
}
