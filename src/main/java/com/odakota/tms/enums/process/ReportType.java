package com.odakota.tms.enums.process;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
public enum ReportType {

    INVENTORY(1),
    SALE_SUMMARY(2),
    REVENUE_BY_STAFF(3),
    BESTSELLERS(4),
    SALE_SUMMARY_OF_DAY(5),
    SALE_SUMMARY_OF_MONTH(6),
    SALE_SUMMARY_OF_YEAR(7),
    REVENUE_BY_STAFF_OF_MONTH(8),
    REVENUE_BY_STAFF_OF_YEAR(9),
    BESTSELLERS_OF_MONTH(10),
    BESTSELLERS_OF_YEAR(11);

    @Getter
    private final Integer value;

    ReportType(Integer value) {
        this.value = value;
    }

    public static ReportType of(Integer value) {
        return value == null ? null : Arrays.stream(values())
                                            .filter(tmp -> tmp.value.equals(value)).findFirst()
                                            .orElseThrow(() -> new IllegalArgumentException(
                                                    "Unknown ReportType with value:" + value));
    }
}
