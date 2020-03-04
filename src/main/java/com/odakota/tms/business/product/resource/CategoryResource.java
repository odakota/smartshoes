package com.odakota.tms.business.product.resource;

import com.odakota.tms.business.product.entity.Category;
import com.odakota.tms.system.base.BaseCondition;
import com.odakota.tms.system.base.BaseResource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author haidv
 * @version 1.0
 */
@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResource extends BaseResource<Category> {

    private Long parentId;

    private String name;

    /**
     * @author haidv
     * @version 1.0
     */
    @Setter @Getter
    public static class CategoryCondition extends BaseCondition {
    }
}
