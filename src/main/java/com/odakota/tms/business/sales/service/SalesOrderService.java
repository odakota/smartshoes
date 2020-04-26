package com.odakota.tms.business.sales.service;

import com.odakota.tms.business.customer.entity.Customer;
import com.odakota.tms.business.customer.repository.CustomerRepository;
import com.odakota.tms.business.product.entity.AllocationProduct;
import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.repository.AllocationProductRepository;
import com.odakota.tms.business.product.repository.ProductRepository;
import com.odakota.tms.business.sales.entity.Campaign;
import com.odakota.tms.business.sales.entity.SalesOrder;
import com.odakota.tms.business.sales.entity.SalesOrderDetail;
import com.odakota.tms.business.sales.repository.CampaignRepository;
import com.odakota.tms.business.sales.repository.PaymentRepository;
import com.odakota.tms.business.sales.repository.SalesOrderDetailRepository;
import com.odakota.tms.business.sales.repository.SalesOrderRepository;
import com.odakota.tms.business.sales.resource.SaleResource;
import com.odakota.tms.business.sales.resource.SalesOrderResource;
import com.odakota.tms.business.sales.resource.SalesOrderResource.SalesOrderCondition;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.enums.sale.DiscountType;
import com.odakota.tms.enums.sale.OrderStatus;
import com.odakota.tms.enums.sale.OrderType;
import com.odakota.tms.enums.sale.SaleType;
import com.odakota.tms.system.base.BaseParameter;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseResponse;
import com.odakota.tms.system.base.BaseService;
import com.odakota.tms.system.config.UserSession;
import com.odakota.tms.system.config.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class SalesOrderService extends BaseService<SalesOrder, SalesOrderResource, SalesOrderCondition> {

    private final SalesOrderRepository salesOrderRepository;

    private final CustomerRepository customerRepository;

    private final PaymentRepository paymentRepository;

    private final SalesOrderDetailRepository salesOrderDetailRepository;

    private final AllocationProductRepository allocationProductRepository;

    private final CampaignRepository campaignRepository;

    private final ProductRepository productRepository;

    private final UserSession userSession;

    @Autowired
    public SalesOrderService(SalesOrderRepository salesOrderRepository,
                             CustomerRepository customerRepository,
                             PaymentRepository paymentRepository,
                             SalesOrderDetailRepository salesOrderDetailRepository,
                             AllocationProductRepository allocationProductRepository,
                             CampaignRepository campaignRepository,
                             ProductRepository productRepository, UserSession userSession) {
        super(salesOrderRepository);
        this.salesOrderRepository = salesOrderRepository;
        this.customerRepository = customerRepository;
        this.paymentRepository = paymentRepository;
        this.salesOrderDetailRepository = salesOrderDetailRepository;
        this.allocationProductRepository = allocationProductRepository;
        this.campaignRepository = campaignRepository;
        this.productRepository = productRepository;
        this.userSession = userSession;
    }

    /**
     * Get a list of resources.
     *
     * @param baseReq request param
     * @return Resource search results are returned.
     */
    @Override
    public BaseResponse<SalesOrderResource> getResources(BaseParameter baseReq) {
        BaseResponse<SalesOrderResource> response = super.getResources(baseReq);
        List<SalesOrderResource> salesOrderResources = response.getData();
        List<Long> customerIds = salesOrderResources.stream().map(SalesOrderResource::getCustomerId)
                                                    .collect(Collectors.toList());
        if (!customerIds.isEmpty()) {
            List<Customer> customers = customerRepository.findByDeletedFlagFalseAndIdIn(customerIds);
            salesOrderResources = response.getData().stream()
                                          .peek(tmp -> customers.stream()
                                                                .filter(var -> tmp.getCustomerId().equals(var.getId()))
                                                                .findFirst().ifPresent(
                                                          customer -> tmp.setCustomerName(customer.getFullName())))
                                          .collect(Collectors.toList());
        }
        return new BaseResponse<>(response.getPagination(), salesOrderResources);
    }

    /**
     * Specify a resource identifier to get one resource.
     *
     * @param id Resource identifier
     * @return Resource search results are returned in {@link Optional}.
     */
    public List<SalesOrderDetail> salesOrderDetails(Long id) {
        return salesOrderDetailRepository.findByOrderId(id);
    }

    /**
     * Cancel order
     */
    public void cancelSalesOrder(Long id) {
        SalesOrder salesOrder = salesOrderRepository.findByIdAndDeletedFlagFalse(id)
                                                    .orElseThrow(() -> new CustomException(
                                                            MessageCode.MSG_ORDER_NOT_EXISTED, HttpStatus.NOT_FOUND));
        salesOrder.setStatus(OrderStatus.CANCEL);
        salesOrderRepository.save(salesOrder);

    }

    @Transactional
    public void sales(SaleResource saleResource) {

        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setStatus(OrderStatus.PAID);
        salesOrder.setCustomerId(saleResource.getCustomerId());
        salesOrder.setOrderType(OrderType.OFFLINE);
        salesOrder.setSaleType(SaleType.RETAIL);
        salesOrder.setTotalAmount(saleResource.getTotalAmount());
        salesOrder.setPaymentId(saleResource.getPaymentMethod());
        salesOrder.setBranchId(userSession.getBranchId());
        salesOrder = salesOrderRepository.save(salesOrder);
        for (SaleResource.SelectProductResource tmp : saleResource.getProductSelectedList()) {
            Product product = productRepository.findByIdAndDeletedFlagFalse(tmp.getProductId())
                                               .orElseThrow(
                                                       () -> new CustomException(MessageCode.MSG_RESOURCE_NOT_EXIST,
                                                                                 HttpStatus.NOT_FOUND));
            Campaign campaign = null;
            if (!product.getPrice().equals(tmp.getPrice())) {
                campaign = campaignRepository.findByItem(tmp.getProductId())
                                             .orElseThrow(() -> new CustomException(
                                                     MessageCode.MSG_INVALID_USERNAME_PASS,
                                                     HttpStatus.BAD_REQUEST));
            }
            SalesOrderDetail salesOrderDetail = new SalesOrderDetail();
            salesOrderDetail.setAmountProduct(tmp.getTotal());
            salesOrderDetail.setSaleOrderId(salesOrder.getId());
            Optional<AllocationProduct> opt = allocationProductRepository
                    .findByDeletedFlagFalseAndProductIdAndColorIdAndSizeIdAndBranchId(tmp.getProductId(),
                                                                                      Long.parseLong(tmp.getColorId()
                                                                                                        .split("#")[0]),
                                                                                      Long.parseLong(tmp.getSizeId()
                                                                                                        .split("#")[0]),
                                                                                      userSession.getBranchId());
            if (opt.isPresent()) {
                AllocationProduct allocationProduct = opt.get();
                salesOrderDetail.setAllocationProductId(allocationProduct.getId());
                if (allocationProduct.getTotal() < tmp.getTotal()){
                    throw new CustomException(MessageCode.MSG_PRODUCT_OUT_OF, HttpStatus.BAD_REQUEST);
                }
                allocationProduct.setTotal(allocationProduct.getTotal() - tmp.getTotal());
                allocationProductRepository.save(allocationProduct);
            } else {
                throw new CustomException(MessageCode.MSG_RESOURCE_NOT_EXIST, HttpStatus.BAD_REQUEST);
            }
            salesOrderDetail.setProductId(product.getId());
            salesOrderDetail.setProductCode(product.getCode());
            salesOrderDetail.setLstPrice(tmp.getPrice());
            salesOrderDetail.setPrePrice(product.getPrice());
            if (campaign != null) {
                if (campaign.getDiscountType().equals(DiscountType.AMOUNT)) {
                    salesOrderDetail.setDiscount(campaign.getDiscountValue());
                }
                if (campaign.getDiscountType().equals(DiscountType.PERCENTAGE)) {
                    salesOrderDetail.setDiscount(product.getPrice() * campaign.getDiscountValue());
                }
                salesOrderDetail.setDiscountReason(campaign.getName());
            }
            salesOrderDetailRepository.save(salesOrderDetail);
        }
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected SalesOrderResource convertToResource(SalesOrder entity) {
        return mapper.convertToResource(entity);
    }

    /**
     * Implement the process of converting resources to entities
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return entity
     */
    @Override
    protected SalesOrder convertToEntity(Long id, SalesOrderResource resource) {
        resource.setId(id);
        return null;//mapper.convertToEntity(resource);
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    @Override
    protected SalesOrderCondition getCondition(FindCondition condition) {
        SalesOrderCondition salesOrderCondition = condition.get(SalesOrderCondition.class);
        return salesOrderCondition == null ? new SalesOrderCondition() : salesOrderCondition;
    }

    public Object getPayment() {
        return paymentRepository.findAll();
    }
}
