package com.odakota.tms.business.process.entity;

import com.odakota.tms.enums.process.ProcessStatus;
import com.odakota.tms.enums.process.ReportType;
import com.odakota.tms.mapper.convert.ProcessStatusConverter;
import com.odakota.tms.mapper.convert.ReportTypeConverter;
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
@Table(name = "report_setting_tbl")
public class ReportSetting extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "report_type")
    @Convert(converter = ReportTypeConverter.class)
    private ReportType type;

    @Column(name = "daily_time")
    private String dailyTime;

    @Column(name = "monthly_time")
    private String monthlyTime;

    @Column(name = "monthly_day")
    private Integer monthlyDay;

    @Column(name = "yearly_time")
    private String yearlyTime;

    @Column(name = "yearly_day")
    private Integer yearlyDay;

    @Column(name = "yearly_month")
    private Integer yearlyMonth;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "process_status")
    @Convert(converter = ProcessStatusConverter.class)
    private ProcessStatus status;

    @Column(name = "branch_id")
    private Long branchId;
}
