package com.odakota.tms.business.product.service;

import com.odakota.tms.business.product.entity.Category;
import com.odakota.tms.business.product.repository.CategoryRepository;
import com.odakota.tms.business.product.repository.ColorRepository;
import com.odakota.tms.business.product.repository.ProductRepository;
import com.odakota.tms.business.product.repository.SizeRepository;
import com.odakota.tms.business.product.resource.CategoryResource;
import com.odakota.tms.business.product.resource.CategoryResource.CategoryCondition;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseService;
import com.odakota.tms.system.config.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class CategoryService extends BaseService<Category, CategoryResource, CategoryCondition> {

    private final CategoryRepository categoryRepository;

    private final SizeRepository sizeRepository;

    private final ColorRepository colorRepository;

    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository,
                           SizeRepository sizeRepository,
                           ColorRepository colorRepository,
                           ProductRepository productRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
        this.sizeRepository = sizeRepository;
        this.colorRepository = colorRepository;
        this.productRepository = productRepository;
    }

    /**
     * Specify a resource identifier and delete the resource.
     *
     * @param id Resource identifier
     */
    @Override
    public void deleteResource(Long id) {
        if (productRepository.existsByCategoryIdAndDeletedFlagFalse(id)){
            throw new CustomException(MessageCode.MSG_CATEGORY_NOT_DELETED, HttpStatus.CONFLICT);
        }
        super.deleteResource(id);
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

    public Object getColors(){
        return colorRepository.findAll();
    }

    public Object getSizes() {
        return sizeRepository.findAll();
    }
}
