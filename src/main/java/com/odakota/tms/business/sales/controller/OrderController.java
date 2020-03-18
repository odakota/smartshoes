package com.odakota.tms.business.sales.controller;

import com.odakota.tms.business.sales.entity.SalesOrder;
import com.odakota.tms.business.sales.resource.SalesOrderResource;
import com.odakota.tms.business.sales.service.SalesOrderService;
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
public class OrderController extends BaseController<SalesOrder, SalesOrderResource> {

    private final SalesOrderService salesOrderService;

    @Autowired
    public OrderController(SalesOrderService salesOrderService) {
        super(salesOrderService);
        this.salesOrderService = salesOrderService;
    }

    /**
     * API get order list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get order list")
    @RequiredAuthentication//(value = ApiId.R_ORDER)
    @GetMapping(value = "/orders", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getSalesOrders(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }

    /**
     * API get order by id
     *
     * @param id role id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get order by id")
    @RequiredAuthentication//(value = ApiId.R_ORDER)
    @GetMapping(value = "/orders/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getSalesOrder(@PathVariable Long id) {
        return super.getResource(id);
    }

    /**
     * API create new order
     *
     * @param resource {@link SalesOrderResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API create new order")
    @RequiredAuthentication//(value = ApiId.C_ORDER)
    @PostMapping(value = "/orders", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> createSalesOrder(@Validated @RequestBody SalesOrderResource resource) {
        return super.createResource(resource);
    }

    /**
     * API update order
     *
     * @param id       order id
     * @param resource {@link SalesOrderResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API update order")
    @RequiredAuthentication//(value = ApiId.U_ORDER)
    @PutMapping(value = "/orders/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> updateSalesOrder(@PathVariable Long id, @RequestBody SalesOrderResource resource) {
        return super.updateResource(id, resource);
    }

    /**
     * API delete order
     *
     * @param id order id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API delete order")
    @RequiredAuthentication//(value = ApiId.D_ORDER)
    @DeleteMapping(value = "/orders/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> deleteSalesOrder(@PathVariable Long id) {
        return super.deleteResource(id);
    }

    /**
     * API cancel order
     *
     * @param id order id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API cancel order")
    @RequiredAuthentication//(value = ApiId.U_ORDER)
    @PutMapping(value = "/orders/{id}/cancel", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> cancelSalesOrder(@PathVariable Long id) {
        salesOrderService.cancelSalesOrder(id);
        return ResponseEntity.noContent().build();
    }
}
