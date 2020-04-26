package com.odakota.tms.enums.process;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
public enum ProcessStatus {

    PAUSE(1),
    IN_PROGRESS(2);

    @Getter
    private Integer value;

    ProcessStatus(Integer value) {
        this.value = value;
    }

    public static ProcessStatus of(Integer value) {
        return value == null ? null : Arrays.stream(values())
                                            .filter(tmp -> tmp.value.equals(value)).findFirst()
                                            .orElseThrow(() -> new IllegalArgumentException(
                                                    "Unknown ProcessStatus with value:" + value));
    }
}
