package com.odakota.tms.enums.sale;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
public enum DiscountType {

    PERCENTAGE(1),
    AMOUNT(2);

    @Getter
    private Integer value;

    DiscountType(Integer value) {
        this.value = value;
    }

    public static DiscountType of(Integer value) {
        return value == null ? null : Arrays.stream(values())
                                            .filter(tmp -> tmp.value.equals(value)).findFirst()
                                            .orElseThrow(() -> new IllegalArgumentException(
                                                    "Unknown DiscountType with value:" + value));
    }
}
