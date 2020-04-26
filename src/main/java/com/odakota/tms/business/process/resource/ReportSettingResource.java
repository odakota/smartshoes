package com.odakota.tms.business.process.resource;

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
@NoArgsConstructor
@AllArgsConstructor
public class ReportSettingResource {

    private String daily1;

    private List<Integer> receiver1;

    private Integer status1;

    private String daily2;

    private String monthlyTime2;

    private Integer monthlyDay2;

    private String yearlyTime2;

    private Integer yearlyDay2;

    private Integer yearlyMonth2;

    private List<Integer> receiver2;

    private Integer status2;

    private String monthlyTime3;

    private Integer monthlyDay3;

    private String yearlyTime3;

    private Integer yearlyDay3;

    private Integer yearlyMonth3;

    private List<Integer> receiver3;

    private Integer status3;

    private String monthlyTime4;

    private Integer monthlyDay4;

    private String yearlyTime4;

    private Integer yearlyDay4;

    private Integer yearlyMonth4;

    private List<Integer> receiver4;

    private Integer status4;
}
