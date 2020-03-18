package com.odakota.tms.business.product.resource;

import com.odakota.tms.business.product.entity.Color;
import com.odakota.tms.system.base.BaseResource;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@NoArgsConstructor
public class ColorResource extends BaseResource<Color> {

    private String name;

    private String code;

    private String sortOrder;
}
