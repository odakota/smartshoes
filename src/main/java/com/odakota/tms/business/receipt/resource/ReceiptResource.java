package com.odakota.tms.business.receipt.resource;

import com.odakota.tms.business.receipt.entity.Receipt;
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
public class ReceiptResource extends BaseResource<Receipt> {

    private String code;

    private Long branchId;

    private String filePath;

    private String note;

    private Date createdDate;

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceiptCondition extends BaseCondition {

        private Date createdDate;
    }
}
