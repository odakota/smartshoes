package com.odakota.tms.enums.file;

import lombok.Getter;

import java.util.Arrays;

/**
 * @author haidv
 * @version 1.0
 */
public enum FileType {

    AVATAR(1);

    @Getter
    private final Integer value;

    FileType(Integer value) {
        this.value = value;
    }

    /**
     * Get file path from string
     *
     * @param value a string indicating the file path
     * @return file path instance
     */
    public static FileType of(Integer value) {
        return Arrays.stream(values()).filter(v -> v.value.equals(value)).findFirst().orElseThrow(
                () -> new IllegalArgumentException(String.format("ImageType = '%s' is not supported.", value)));
    }
}
