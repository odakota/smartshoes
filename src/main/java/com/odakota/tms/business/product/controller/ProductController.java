package com.odakota.tms.business.product.controller;

import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.resource.ProductResource;
import com.odakota.tms.business.product.service.ProductService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.system.annotations.RequiredAuthentication;
import com.odakota.tms.system.base.BaseController;
import com.odakota.tms.system.base.BaseParameter;
import com.odakota.tms.system.config.data.ResponseData;
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
public class ProductController extends BaseController<Product, ProductResource> {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        super(productService);
        this.productService = productService;
    }

    /**
     * API get product list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get all product list")
    @RequiredAuthentication//(value = ApiId.R_PRODUCT)
    @GetMapping(value = "/products", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getProducts(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }

    /**
     * API get product list sale
     *
     * @return {@link ResponseEntity}
     * @param categoryId
     */
    @ApiOperation("API get product list sale")
    @RequiredAuthentication//(value = ApiId.R_PRODUCT)
    @GetMapping(value = "/products/sale", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getProducts(@RequestParam(required = false) Long categoryId, @RequestParam(required = false) String productName) {
        return ResponseEntity.ok(new ResponseData<>().success(productService.getProducts(categoryId, productName)));
    }

    /**
     * API get product by id
     *
     * @param id product id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get product by id")
    @RequiredAuthentication//(value = ApiId.R_PRODUCT)
    @GetMapping(value = "/products/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getProduct(@PathVariable Long id) {
        return super.getResource(id);
    }

    /**
     * API create new product
     *
     * @param resource {@link ProductResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API create new product")
    @RequiredAuthentication//(value = ApiId.C_PRODUCT)
    @PostMapping(value = "/products", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> createProduct(@Validated @RequestBody ProductResource resource) {
        return super.createResource(resource);
    }

    /**
     * API update product
     *
     * @param id       product id
     * @param resource {@link ProductResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API update product")
    @RequiredAuthentication//(value = ApiId.U_PRODUCT)
    @PutMapping(value = "/products/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductResource resource) {
        return super.updateResource(id, resource);
    }

    /**
     * API delete product
     *
     * @param id product id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API delete product")
    @RequiredAuthentication//(value = ApiId.D_PRODUCT)
    @DeleteMapping(value = "/products/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        return super.deleteResource(id);
    }

    /**
     * API get product color
     *
     * @param id product id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get product color")
    @RequiredAuthentication//(value = ApiId.R_PRODUCT)
    @GetMapping(value = "/products/{id}/color", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getProductColor(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseData<>().success(productService.getProductColor(id)));
    }

    /**
     * API get product size
     *
     * @param id product id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get product size")
    @RequiredAuthentication//(value = ApiId.R_PRODUCT)
    @GetMapping(value = "/products/{id}/color/{colorId}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getProductSize(@PathVariable Long id, @PathVariable Long colorId) {
        return ResponseEntity.ok(new ResponseData<>().success(productService.getProductSize(id, colorId)));
    }
}
