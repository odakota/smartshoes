package com.odakota.tms.enums.customer;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
public enum InputType {

    ONLINE(1),
    OFFLINE(2);

    @Getter
    private final Integer value;

    InputType(Integer value) {
        this.value = value;
    }

    public static InputType of(Integer value) {
        return Arrays.stream(values())
                     .filter(tmp -> tmp.value.equals(value)).findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Unknown InputType with value:" + value));
    }
}
