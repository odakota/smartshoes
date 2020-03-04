package com.odakota.tms.business.product.service;

import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.repository.ProductRepository;
import com.odakota.tms.business.product.resource.ProductResource;
import com.odakota.tms.business.product.resource.ProductResource.ProductCondition;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class ProductService extends BaseService<Product, ProductResource, ProductCondition> {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        super(productRepository);
        this.productRepository = productRepository;
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected ProductResource convertToResource(Product entity) {
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
    protected Product convertToEntity(Long id, ProductResource resource) {
        Product category = super.mapper.convertToEntity(resource);
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
    protected ProductCondition getCondition(FindCondition condition) {
        return new ProductCondition();
    }
}
