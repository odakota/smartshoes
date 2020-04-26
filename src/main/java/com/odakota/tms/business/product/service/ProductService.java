package com.odakota.tms.business.product.service;

import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.repository.ColorRepository;
import com.odakota.tms.business.product.repository.ProductRepository;
import com.odakota.tms.business.product.repository.SizeRepository;
import com.odakota.tms.business.product.resource.ColorResource;
import com.odakota.tms.business.product.resource.ProductResource;
import com.odakota.tms.business.product.resource.ProductResource.ProductCondition;
import com.odakota.tms.business.product.resource.SizeResource;
import com.odakota.tms.business.sales.repository.CampaignRepository;
import com.odakota.tms.enums.sale.DiscountType;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseResponse;
import com.odakota.tms.system.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class ProductService extends BaseService<Product, ProductResource, ProductCondition> {

    private final ProductRepository productRepository;

    private final ColorRepository colorRepository;

    private final SizeRepository sizeRepository;

    private final CampaignRepository campaignRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ColorRepository colorRepository,
                          SizeRepository sizeRepository,
                          CampaignRepository campaignRepository) {
        super(productRepository);
        this.productRepository = productRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.campaignRepository = campaignRepository;
    }

    public Object getProducts(Long categoryId, String productName) {
        List<Product> products = productRepository.findAllProductSale(categoryId, productName);
        List<ProductResource> productResources = super.convertToResource(products);
        productResources.forEach(tmp -> campaignRepository.findByItem(tmp.getId())
                                                          .ifPresent(var -> {
            if (var.getDiscountType().equals(DiscountType.AMOUNT)) {
                tmp.setPrice(tmp.getPrice() - var.getDiscountValue());
            }
            if (var.getDiscountType().equals(DiscountType.PERCENTAGE)) {
                tmp.setPrice(tmp.getPrice() - tmp.getPrice() * (var.getDiscountValue()));
            }
        }));
        return productResources;
    }

    /**
     * Get list color resource with product id
     *
     * @param id productId
     * @return list color resource
     */
    public BaseResponse<ColorResource> getProductColor(Long id) {
        return new BaseResponse<>(colorRepository.findByProductIdAndBranchId(id)
                                                 .stream().map(tmp -> mapper.convertToResource(tmp))
                                                 .collect(Collectors.toList()));
    }

    /**
     * Get list size resource with product id
     *
     * @param productId productId
     * @return list size resource
     */
    public BaseResponse<SizeResource> getProductSize(Long productId, Long colorId) {
        return new BaseResponse<>(sizeRepository.findByProductIdAndBranchIdAndColorId(productId, colorId));
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
