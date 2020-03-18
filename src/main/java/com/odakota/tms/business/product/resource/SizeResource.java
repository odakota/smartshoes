package com.odakota.tms.business.product.resource;

import com.odakota.tms.business.product.entity.Size;
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
public class SizeResource extends BaseResource<Size> {

    private String name;

    private String code;

    private Integer total;

    public SizeResource(Long id, String name, String code, Integer total) {
        super(id);
        this.name = name;
        this.code = code;
        this.total = total;
    }
}
