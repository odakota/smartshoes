package com.odakota.tms.business.receipt.controller;

import com.odakota.tms.business.receipt.entity.Receipt;
import com.odakota.tms.business.receipt.resource.ReceiptResource;
import com.odakota.tms.business.receipt.service.ReceiptService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.base.BaseParameter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class ReceiptController extends BaseController<Receipt, ReceiptResource> {

    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        super(receiptService);
        this.receiptService = receiptService;
    }

    /**
     * API get receipt list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get receipt list")
    @RequiredAuthentication//(value = ApiId.R_RECEIPT)
    @GetMapping(value = "/receipts", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getReceipts(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }

    /**
     * API get receipt by id
     *
     * @param id role id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get receipt by id")
    @RequiredAuthentication//(value = ApiId.R_RECEIPT)
    @GetMapping(value = "/receipts/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getReceipt(@PathVariable Long id) {
        return super.getResource(id);
    }

    /**
     * API create new receipt
     *
     * @param resource {@link ReceiptResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API create new receipt")
    @RequiredAuthentication//(value = ApiId.C_RECEIPT)
    @PostMapping(value = "/receipts", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> createReceipt(@Validated @RequestBody ReceiptResource resource) {
        return super.createResource(resource);
    }

    /**
     * API update receipt
     *
     * @param id       receipt id
     * @param resource {@link ReceiptResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API update receipt")
    @RequiredAuthentication//(value = ApiId.U_RECEIPT)
    @PutMapping(value = "/receipts/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> updateReceipt(@PathVariable Long id, @RequestBody ReceiptResource resource) {
        return super.updateResource(id, resource);
    }

    /**
     * API delete receipt
     *
     * @param id receipt id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API delete receipt")
    @RequiredAuthentication//(value = ApiId.D_RECEIPT)
    @DeleteMapping(value = "/receipts/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> deleteReceipt(@PathVariable Long id) {
        return super.deleteResource(id);
    }
}
