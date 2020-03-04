package com.odakota.tms.business.product.service;

import com.odakota.tms.business.product.entity.Category;
import com.odakota.tms.business.product.repository.CategoryRepository;
import com.odakota.tms.business.product.resource.CategoryResource;
import com.odakota.tms.business.product.resource.CategoryResource.CategoryCondition;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class CategoryService extends BaseService<Category, CategoryResource, CategoryCondition> {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected CategoryResource convertToResource(Category entity) {
        return super.mapper.convertToResource(entity);
    }

    /**
     * Implement the process of converting resources to entities
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return entity
     */
    @Override
    protected Category convertToEntity(Long id, CategoryResource resource) {
        Category category = super.mapper.convertToEntity(resource);
        category.setId(id);
        return category;
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    @Override
    protected CategoryCondition getCondition(FindCondition condition) {
        return new CategoryCondition();
    }
}
