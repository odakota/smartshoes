package com.odakota.tms.business.customer.resource;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.odakota.tms.business.customer.entity.Customer;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.base.BaseCondition;
import com.odakota.tms.system.base.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResource extends BaseResource<Customer> {

    @NotBlank(message = MessageCode.MSG_REQUIRED)
    private String fullName;

    private Integer customerType;

    private Integer sex;

    @JsonFormat(pattern = Constant.YYYY_MM_DD)
    private Date birthDay;

    private String avatar;

    private String email;

    private String phone;

    private String identifier;

    private Long provinceId;

    private Long districtId;

    private Long wardId;

    private String street;

    private Integer customerSegment;

    private boolean isMailMagazineReceipt;

    private boolean disableFlag;

    public CustomerResource(Long id) {
        super(id);
    }

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomerCondition extends BaseCondition {

        private String name;

        private Integer customerSegment;

        private Integer customerType;
    }
}
