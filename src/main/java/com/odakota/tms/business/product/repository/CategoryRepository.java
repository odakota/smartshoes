package com.odakota.tms.business.product.repository;

import com.odakota.tms.business.product.entity.Category;
import com.odakota.tms.business.product.resource.CategoryResource.CategoryCondition;
import com.odakota.tms.system.base.BaseRepository;
import org.springframework.stereotype.Repository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CategoryRepository extends BaseRepository<Category, CategoryCondition> {
}
