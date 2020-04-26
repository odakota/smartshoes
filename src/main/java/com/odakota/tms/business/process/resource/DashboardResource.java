package com.odakota.tms.business.process.resource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@AllArgsConstructor
public class DashboardResource {

    private String name;

    private long total;
}
