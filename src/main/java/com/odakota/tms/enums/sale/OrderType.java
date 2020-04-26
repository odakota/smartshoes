package com.odakota.tms.enums.sale;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
public enum OrderType {

    ONLINE(1),
    OFFLINE(2);

    @Getter
    private final Integer value;

    OrderType(Integer value) {
        this.value = value;
    }

    public static OrderType of(Integer value) {
        return value == null ? null : Arrays.stream(values())
                                            .filter(tmp -> tmp.value.equals(value)).findFirst()
                                            .orElseThrow(() -> new IllegalArgumentException(
                                                    "Unknown OrderType with value:" + value));
    }
}
