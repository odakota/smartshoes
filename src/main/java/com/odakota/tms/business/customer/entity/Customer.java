package com.odakota.tms.business.customer.entity;

import com.odakota.tms.enums.auth.Gender;
import com.odakota.tms.enums.customer.CustomerType;
import com.odakota.tms.enums.customer.InputType;
import com.odakota.tms.mapper.convert.CustomerTypeConverter;
import com.odakota.tms.mapper.convert.GenderConverter;
import com.odakota.tms.mapper.convert.InputTypeConverter;
import com.odakota.tms.system.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author haidv
 * @version 1.0
 */
@Entity
@Setter @Getter
@Table(name = "customer_tbl")
public class Customer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "customer_type")
    @Convert(converter = CustomerTypeConverter.class)
    private CustomerType customerType;

    @Column(name = "sex")
    @Convert(converter = GenderConverter.class)
    private Gender sex;

    @Column(name = "birth_date")
    private Date birthDay;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "province_id")
    private Long provinceId;

    @Column(name = "district_id")
    private Long districtId;

    @Column(name = "ward_id")
    private Long wardId;

    @Column(name = "street")
    private String street;

    @Column(name = "customer_segment")
    private String customerSegment;

    @Column(name = "is_mail_magazine_receipt")
    private boolean isMailMagazineReceipt;

    @Column(name = "disable_flag")
    private boolean disableFlag;

    @Column(name = "input_type")
    @Convert(converter = InputTypeConverter.class)
    private InputType inputType;
}
