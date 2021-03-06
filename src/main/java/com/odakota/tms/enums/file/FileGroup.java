package com.odakota.tms.enums.file;

import lombok.Getter;

/**
 * @author haidv
 * @version 1.0
 */
public enum FileGroup {

    USER(1, "user", "JTN0001"),
    ROLE(2, "role", ""),
    BRANCH(3, "branch", ""),
    CUSTOMER(4, "customer", "");

    @Getter
    private final Integer value;

    @Getter
    private final String name;

    @Getter
    private final String jobTaskName;

    FileGroup(Integer value, String name, String jobTaskName) {
        this.value = value;
        this.name = name;
        this.jobTaskName = jobTaskName;
    }

    public static FileGroup of(Integer value) {
        for (FileGroup fileType : FileGroup.values()) {
            if (fileType.getValue().equals(value)) {
                return fileType;
            }
        }
        throw new IllegalArgumentException("Unknown file group with value:" + value);
    }
}
