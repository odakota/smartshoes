package com.odakota.tms.business.receipt.resource;

import com.odakota.tms.business.receipt.entity.Receipt;
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
public class ReceiptResource extends BaseResource<Receipt> {

    private String code;

    private Long branchId;

    private String filePath;

    private String note;

    private String billCode;

    private String deliver;

    private Date receiptDate;

    private String warehouse;

    private Boolean approvedFlag = false;

    private List<ReceiptDetailResource> detail;

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiptCondition extends BaseCondition {

        private Date startDate;

        private Date endDate;

        private Long branchId;

        private Boolean approvedFlag;
    }
}
