package com.odakota.tms.business.receipt.service;

import com.odakota.tms.business.product.entity.AllocationProduct;
import com.odakota.tms.business.product.entity.Color;
import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.entity.Size;
import com.odakota.tms.business.product.repository.AllocationProductRepository;
import com.odakota.tms.business.product.repository.ColorRepository;
import com.odakota.tms.business.product.repository.ProductRepository;
import com.odakota.tms.business.product.repository.SizeRepository;
import com.odakota.tms.business.receipt.entity.Receipt;
import com.odakota.tms.business.receipt.entity.ReceiptDetail;
import com.odakota.tms.business.receipt.repository.ReceiptDetailRepository;
import com.odakota.tms.business.receipt.repository.ReceiptRepository;
import com.odakota.tms.business.receipt.resource.ReceiptDetailResource;
import com.odakota.tms.business.receipt.resource.ReceiptResource;
import com.odakota.tms.business.receipt.resource.ReceiptResource.ReceiptCondition;
import com.odakota.tms.constant.FieldConstant;
import com.odakota.tms.constant.MessageCode;
import com.odakota.tms.system.base.BaseParameter.FindCondition;
import com.odakota.tms.system.base.BaseService;
import com.odakota.tms.system.config.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class ReceiptService extends BaseService<Receipt, ReceiptResource, ReceiptCondition> {

    private final ReceiptRepository receiptRepository;

    private final ProductRepository productRepository;

    private final ReceiptDetailRepository receiptDetailRepository;

    private final ColorRepository colorRepository;

    private final SizeRepository sizeRepository;

    private final AllocationProductRepository allocationProductRepository;

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository,
                          ProductRepository productRepository,
                          ReceiptDetailRepository receiptDetailRepository,
                          ColorRepository colorRepository,
                          SizeRepository sizeRepository,
                          AllocationProductRepository allocationProductRepository) {
        super(receiptRepository);
        this.receiptRepository = receiptRepository;
        this.productRepository = productRepository;
        this.receiptDetailRepository = receiptDetailRepository;
        this.colorRepository = colorRepository;
        this.sizeRepository = sizeRepository;
        this.allocationProductRepository = allocationProductRepository;
    }

    /**
     * Create a new resource.
     *
     * @param resource resource
     * @return The created resource is returned.
     */
    @Transactional
    @Override
    public ReceiptResource createResource(ReceiptResource resource) {
        // check duplicate code
        if (receiptRepository.isExistedResource(null, FieldConstant.RECEIPT_CODE, resource.getCode())) {
            throw new CustomException(MessageCode.MSG_RECEIPT_CODE_EXISTED, HttpStatus.CONFLICT);
        }
        // check duplicate billCode
        if (receiptRepository.isExistedResource(null, FieldConstant.RECEIPT_BILL_NAME, resource.getBillCode())) {
            throw new CustomException(MessageCode.MSG_RECEIPT_BILL_CODE_EXISTED, HttpStatus.CONFLICT);
        }
        ReceiptResource receiptResource = super.createResource(resource);
        // save receipt detail
        resource.getDetail().forEach(tmp -> {
            ReceiptDetail receiptDetail = new ReceiptDetail();
            Optional<Product> optionalProduct = productRepository.findByCodeAndDeletedFlagFalse(tmp.getProductCode());
            if (optionalProduct.isPresent()) {
                receiptDetail.setProductCode(tmp.getProductCode());
                receiptDetail.setProductId(optionalProduct.get().getId());
                receiptDetail.setAmount(Integer.parseInt(tmp.getAmount()));
                receiptDetail.setDetail(tmp.getDetail());
                receiptDetail.setReceiptId(receiptResource.getId());
                receiptDetailRepository.save(receiptDetail);
            }
        });
        return receiptResource;
    }

    /**
     * Change status receipt
     *
     * @param id resource id
     */
    public void approveReceipt(Long id) {
        Receipt receipt = receiptRepository.findByIdAndDeletedFlagFalse(id).orElseThrow(
                () -> new CustomException(MessageCode.MSG_RECEIPT_NOT_EXISTED, HttpStatus.NOT_FOUND));
        if (!receipt.getApprovedFlag()) {
            List<ReceiptDetail> receiptDetails = receiptDetailRepository
                    .findByDeletedFlagFalseAndReceiptId(receipt.getId());
            receiptDetails.forEach(tmp -> {
                // tmp.getDetail() = "red: 36-250,37-200,38-150; black: 36-250,37-200,38-150"
                String[] groupColors = tmp.getDetail().split(";");
                for (String groupColor : groupColors) {
                    String[] groupSizes = groupColor.trim().split(":");
                    // check match format and existed color code
                    if (groupSizes.length == 2) {
                        Optional<Color> optColor = colorRepository
                                .findByCodeAndDeletedFlagFalse(groupSizes[0].trim());
                        if (optColor.isPresent()) {
                            String[] sizes = groupSizes[1].trim().split(",");
                            for (String size : sizes) {
                                String[] var = size.trim().split("-");
                                // check match format and existed size code
                                if (var.length == 2) {
                                    Optional<Size> optSize = sizeRepository
                                            .findByCodeAndDeletedFlagFalse(var[0].trim());
                                    if (optSize.isPresent()) {
                                        Optional<AllocationProduct> optAllocationProduct = allocationProductRepository
                                                .findByDeletedFlagFalseAndProductIdAndColorIdAndSizeIdAndBranchId(
                                                        tmp.getProductId(), optColor.get().getId(),
                                                        optSize.get().getId(), receipt.getBranchId());
                                        if (optAllocationProduct.isPresent()) {
                                            // update total allocation product
                                            AllocationProduct allocationProduct = optAllocationProduct.get();
                                            allocationProduct.setTotal(
                                                    allocationProduct.getTotal() + Integer.parseInt(var[1].trim()));
                                            allocationProductRepository.save(allocationProduct);
                                        } else {
                                            AllocationProduct allocationProduct = new AllocationProduct();
                                            allocationProduct.setTotal(Integer.parseInt(var[1].trim()));
                                            allocationProduct.setColorId(optColor.get().getId());
                                            allocationProduct.setBranchId(receipt.getBranchId());
                                            allocationProduct.setProductId(tmp.getProductId());
                                            allocationProduct.setSizeId(optSize.get().getId());
                                            allocationProductRepository.save(allocationProduct);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
            receipt.setApprovedFlag(true);
            receipt.setApprovedDate(new Date());
            receiptRepository.save(receipt);
        }
    }

    /**
     * Get receipt detail
     *
     * @param id Resource identifier
     * @return The receipt detail.
     */
    public List<ReceiptDetailResource> getReceiptDetail(Long id) {
        if (!receiptRepository.existsByIdAndDeletedFlagFalse(id)) {
            throw new CustomException(MessageCode.MSG_RECEIPT_NOT_EXISTED, HttpStatus.NOT_FOUND);
        }
        List<ReceiptDetail> receiptDetails = receiptDetailRepository.findByDeletedFlagFalseAndReceiptId(id);
        return receiptDetails.stream().map(tmp -> {
            ReceiptDetailResource resource = mapper.convertToResource(tmp);
            resource.setId(tmp.getId());
            resource.setEditable(false);
            resource.setIsNew(false);
            return resource;
        }).collect(Collectors.toList());
    }

    /**
     * Update resources.
     *
     * @param id       Resource identifier
     * @param resource resource
     * @return The updated resource is returned.
     */
    @Transactional
    @Override
    protected ReceiptResource updateResource(Long id, ReceiptResource resource) {
        Receipt receipt = receiptRepository.findByIdAndDeletedFlagFalse(id).orElseThrow(
                () -> new CustomException(MessageCode.MSG_RECEIPT_NOT_EXISTED, HttpStatus.NOT_FOUND));
        if (receipt.getApprovedFlag()) {
            throw new CustomException(MessageCode.MSG_RECEIPT_NOT_UPDATED, HttpStatus.CONFLICT);
        }
        receiptRepository.save(this.convertToEntity(id, resource));
        resource.getDetail().forEach(tmp -> {
            Optional<Product> optionalProduct = productRepository.findByCodeAndDeletedFlagFalse(tmp.getProductCode());
            if (optionalProduct.isPresent()) {
                ReceiptDetail receiptDetail = mapper.convertToEntity(tmp);
                receiptDetail.setReceiptId(id);
                receiptDetail.setProductId(optionalProduct.get().getId());
                receiptDetailRepository.save(receiptDetail);
            }
        });
        return resource;
    }

    /**
     * Specify a resource identifier and delete the resource.
     *
     * @param id Resource identifier
     */
    @Override
    public void deleteResource(Long id) {
        Receipt receipt = receiptRepository.findByIdAndDeletedFlagFalse(id).orElse(null);
        if (receipt != null) {
            if (receipt.getApprovedFlag()) {
                throw new CustomException(MessageCode.MSG_RECEIPT_NOT_DELETED, HttpStatus.CONFLICT);
            }
            super.deleteResource(receipt);
        }
    }

    /**
     * Implement the process of converting entities to resources
     *
     * @param entity entity
     * @return resource
     */
    @Override
    protected ReceiptResource convertToResource(Receipt entity) {
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
    protected Receipt convertToEntity(Long id, ReceiptResource resource) {
        resource.setId(id);
        return mapper.convertToEntity(resource);
    }

    /**
     * Implement the process of converting condition string to condition class
     *
     * @param condition condition
     * @return condition
     */
    @Override
    protected ReceiptCondition getCondition(FindCondition condition) {
        ReceiptCondition receiptCondition = condition.get(ReceiptCondition.class);
        return receiptCondition == null ? new ReceiptCondition() : receiptCondition;
    }
}
