package com.odakota.tms.enums.customer;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
public enum  Segment {

    SUPER_SMALL(1),
    SMALL(2),
    MEDIUM(3),
    BIG(4);

    @Getter
    private Integer value;

    Segment(Integer value) {
        this.value = value;
    }

    public static Segment of(Integer value) {
        return value == null ? null :Arrays.stream(values())
                     .filter(tmp -> tmp.value.equals(value)).findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Unknown Segment with value:" + value));
    }
}
