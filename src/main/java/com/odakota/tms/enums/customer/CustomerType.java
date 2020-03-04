package com.odakota.tms.enums.customer;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
public enum CustomerType {

    INDIVIDUAL(1),
    CORPORATE(2);

    @Getter
    private Integer value;

    CustomerType(Integer value) {
        this.value = value;
    }

    public static CustomerType of(Integer value) {
        return value == null ? null : Arrays.stream(values())
                                            .filter(tmp -> tmp.value.equals(value)).findFirst()
                                            .orElseThrow(() -> new IllegalArgumentException(
                                                    "Unknown CustomerType with value:" + value));
    }
}
