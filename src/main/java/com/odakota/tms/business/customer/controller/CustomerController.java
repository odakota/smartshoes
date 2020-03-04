package com.odakota.tms.business.customer.controller;

import com.odakota.tms.business.asserts.service.ExportService;
import com.odakota.tms.business.customer.entity.Customer;
import com.odakota.tms.business.customer.resource.CustomerResource;
import com.odakota.tms.business.customer.service.CustomerService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.enums.auth.ApiId;
import com.odakota.tms.enums.file.FileGroup;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.base.BaseParameter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class CustomerController extends BaseController<Customer, CustomerResource> {

    private final CustomerService customerService;

    private final ExportService<Customer> exportService;

    @Autowired
    public CustomerController(CustomerService customerService,
                              ExportService<Customer> exportService) {
        super(customerService);
        this.customerService = customerService;
        this.exportService = exportService;
    }

    /**
     * API get customer list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get customer list")
    @RequiredAuthentication(value = ApiId.R_CUSTOMER)
    @GetMapping(value = "/customers", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getCustomers(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }

    /**
     * API get customer by id
     *
     * @param id role id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get customer by id")
    @RequiredAuthentication(value = ApiId.R_CUSTOMER)
    @GetMapping(value = "/customers/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        return super.getResource(id);
    }

    /**
     * API create new customer
     *
     * @param resource {@link CustomerResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API create new customer")
    @RequiredAuthentication(value = ApiId.C_CUSTOMER)
    @PostMapping(value = "/customers", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> createCustomer(@Validated @RequestBody CustomerResource resource) {
        return super.createResource(resource);
    }

    /**
     * API update customer
     *
     * @param id       customer id
     * @param resource {@link CustomerResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API update customer")
    @RequiredAuthentication(value = ApiId.U_CUSTOMER)
    @PutMapping(value = "/customers/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerResource resource) {
        return super.updateResource(id, resource);
    }

    /**
     * API delete customer
     *
     * @param id customer id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API delete customer")
    @RequiredAuthentication(value = ApiId.D_CUSTOMER)
    @DeleteMapping(value = "/customers/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        return super.deleteResource(id);
    }

    /**
     * API batch delete customer
     *
     * @param ids list customer id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API batch delete customer")
    @RequiredAuthentication(value = ApiId.D_CUSTOMER)
    @DeleteMapping(value = "/customers", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> batchDeleteCustomer(@RequestParam String ids) {
        return super.batchDeleteResource(ids);
    }

    /**
     * API export customers to excel
     *
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API export customers to excel")
    @RequiredAuthentication(value = ApiId.E_CUSTOMER)
    @GetMapping(value = "/customers/export", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<byte[]> exportCustomer(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        //MediaType.parseMediaType("application/vnd.ms-excel")
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(exportService.export(FileGroup.CUSTOMER, response), headers, HttpStatus.OK);
    }
}
