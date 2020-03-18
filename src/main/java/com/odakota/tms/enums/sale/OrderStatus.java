package com.odakota.tms.enums.sale;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
public enum  OrderStatus {

    UNPAID(1),
    PAID(2),
    CANCEL(3);

    @Getter
    private Integer value;

    OrderStatus(Integer value) {
        this.value = value;
    }

    public static OrderStatus of(Integer value) {
        return value == null ? null : Arrays.stream(values())
                                            .filter(tmp -> tmp.value.equals(value)).findFirst()
                                            .orElseThrow(() -> new IllegalArgumentException(
                                                    "Unknown OrderStatus with value:" + value));
    }
}
