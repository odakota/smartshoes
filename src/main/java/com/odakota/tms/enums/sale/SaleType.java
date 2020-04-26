package com.odakota.tms.enums.sale;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
public enum SaleType {

    RETAIL(1),
    WHOLESALE(2);

    @Getter
    private final Integer value;

    SaleType(Integer value) {
        this.value = value;
    }

    public static SaleType of(Integer value) {
        return value == null ? null : Arrays.stream(values())
                                            .filter(tmp -> tmp.value.equals(value)).findFirst()
                                            .orElseThrow(() -> new IllegalArgumentException(
                                                    "Unknown SaleType with value:" + value));
    }
}
