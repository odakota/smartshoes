package com.odakota.tms.business.product.service;

import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.notification.entity.Notification;
import com.odakota.tms.business.notification.entity.NotificationUser;
import com.odakota.tms.business.notification.repository.NotificationRepository;
import com.odakota.tms.business.notification.repository.NotificationUserRepository;
import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.repository.ColorRepository;
import com.odakota.tms.business.product.repository.ProductRepository;
import com.odakota.tms.business.product.repository.SizeRepository;
import com.odakota.tms.business.product.resource.ColorResource;
import com.odakota.tms.business.product.resource.ProductResource;
import com.odakota.tms.business.product.resource.ProductResource.ProductCondition;
import com.odakota.tms.business.product.resource.SizeResource;
import com.odakota.tms.business.sales.repository.CampaignRepository;
import com.odakota.tms.constant.*;
import com.odakota.tms.enums.notify.MsgType;
import com.odakota.tms.enums.notify.Priority;
import com.odakota.tms.enums.notify.SendStatus;
import com.odakota.tms.enums.sale.DiscountType;
import com.odakota.tms.system.base.BaseParameter;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseResponse;
import com.odakota.tms.system.base.BaseService;
import com.odakota.tms.system.config.exception.CustomException;
import com.odakota.tms.system.service.websocket.WebSocket;
import com.odakota.tms.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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

    private final NotificationRepository notificationRepository;

    private final NotificationUserRepository notificationUserRepository;

    private final WebSocket webSocket;

    private final UserRepository userRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ColorRepository colorRepository,
                          SizeRepository sizeRepository,
                          CampaignRepository campaignRepository,
                          NotificationRepository notificationRepository,
                          NotificationUserRepository notificationUserRepository,
                          WebSocket webSocket, UserRepository userRepository) {
        super(productRepository);
        this.productRepository = productRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.campaignRepository = campaignRepository;
        this.notificationRepository = notificationRepository;
        this.notificationUserRepository = notificationUserRepository;
        this.webSocket = webSocket;
        this.userRepository = userRepository;
    }

    @Override
    public BaseResponse<ProductResource> getResources(BaseParameter baseReq) {
        ProductCondition condition = this.getCondition(baseReq.getFindCondition());
        Pageable pageRequest = BaseParameter.getPageable(baseReq.getSort(), baseReq.getPage(), baseReq.getLimit());
        List<Product> products;
        Page<Product> page = null;
        if (pageRequest == null) {
            products = productRepository.findByCondition(condition);
        } else {
            page = productRepository.findByCondition(condition, pageRequest);
            products = page.getContent();
        }
        List<ProductResource> resources = productRepository.countByBranch(condition.getBranchId());
        List<ProductResource> response = new ArrayList<>();
        for (Product product : products) {
            ProductResource productResource = super.mapper.convertToResource(product);
            ProductResource r2 = resources.stream().filter(tmp -> tmp.getId().equals(productResource.getId()))
                                          .findFirst().orElse(null);
            if (r2 != null) {
                productResource.setTotal(r2.getTotal());
            } else {
                productResource.setTotal(0L);
            }
            response.add(productResource);
        }
        return new BaseResponse<>(response, page);
    }

    @Transactional
    @Override
    public ProductResource createResource(ProductResource resource) {
        // check duplicate code
        if (productRepository.isExistedResource(null, FieldConstant.PRODUCT_CODE, resource.getCode())) {
            throw new CustomException(MessageCode.MSG_PRODUCT_CODE_EXISTED, HttpStatus.CONFLICT);
        }
        if (resource.getPath() == null) {
            resource.setPath(FilePath.PROD_IMAGE_PATH_DEFAULT);
        }
        ProductResource productResource = super.createResource(resource);
        Notification notification = new Notification();
        notification.setPriority(Priority.HIGH);
        notification.setType(MsgType.SYSTEM);
        notification.setTitle(NotificationConstant.TITLE_NEW_PRODUCT);
        notification.setContent(String.format(NotificationConstant.CONTENT_NEW_PRODUCT, resource.getName(),
                                              resource.getSaleStartAt()));
        notification.setSendStatus(SendStatus.PUBLISHED);
        notification.setStartDate(new Date());
        notification.setSendTime(new Date());
        notification = notificationRepository.save(notification);
        Long notifyId = notification.getId();
        List<Long> id = userRepository.findAllUserId();
        id.forEach(tmp -> {
            NotificationUser notificationUser = new NotificationUser();
            notificationUser.setNotificationId(notifyId);
            notificationUser.setRead(false);
            notificationUser.setUserId(tmp);
            notificationUserRepository.save(notificationUser);
        });
        webSocket.onMessage(id, Utils.toJson(mapper.convertToResource(notification)));
        return productResource;
    }

    @Override
    protected ProductResource updateResource(Long id, ProductResource resource) {
        // check role default
        if (Constant.NUMBER_OF_ROLE_DEFAULT >= id) {
            throw new CustomException(MessageCode.MSG_ROLE_NOT_UPDATED, HttpStatus.BAD_REQUEST);
        }
        return super.updateResource(id, resource);
    }


    public Object getProducts(Long categoryId, String productName) {
        List<Product> products = productRepository.findAllProductSale(categoryId, productName);
        List<ProductResource> productResources = super.convertToResource(products);
        productResources.forEach(tmp -> campaignRepository.findByItem(tmp.getId())
                                                          .ifPresent(var -> {
                                                              if (var.getDiscountType().equals(DiscountType.AMOUNT)) {
                                                                  tmp.setPrice(tmp.getPrice() - var.getDiscountValue());
                                                              }
                                                              if (var.getDiscountType()
                                                                     .equals(DiscountType.PERCENTAGE)) {
                                                                  tmp.setPrice(tmp.getPrice() - tmp.getPrice() *
                                                                                                (var.getDiscountValue()));
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
        ProductCondition productCondition = condition.get(ProductCondition.class);
        return productCondition == null ? new ProductCondition() : productCondition;
    }
}
