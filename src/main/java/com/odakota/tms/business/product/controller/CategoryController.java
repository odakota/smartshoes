package com.odakota.tms.business.product.controller;

import com.odakota.tms.business.product.entity.Category;
import com.odakota.tms.business.product.resource.CategoryResource;
import com.odakota.tms.business.product.service.CategoryService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.enums.auth.ApiId;
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
public class CategoryController extends BaseController<Category, CategoryResource> {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        super(categoryService);
        this.categoryService = categoryService;
    }

    /**
     * API get category list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get category list")
    @RequiredAuthentication(value = ApiId.R_CATEGORY)
    @GetMapping(value = "/categories", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getCategories(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }

    /**
     * API get category by id
     *
     * @param id category id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get category by id")
    @RequiredAuthentication(value = ApiId.R_CATEGORY)
    @GetMapping(value = "/categories/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getCategory(@PathVariable Long id) {
        return super.getResource(id);
    }

    /**
     * API create new category
     *
     * @param resource {@link CategoryResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API create new category")
    @RequiredAuthentication(value = ApiId.C_CATEGORY)
    @PostMapping(value = "/categories", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> createCategory(@Validated @RequestBody CategoryResource resource) {
        return super.createResource(resource);
    }

    /**
     * API update category
     *
     * @param id       category id
     * @param resource {@link CategoryResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API update category")
    @RequiredAuthentication(value = ApiId.U_CATEGORY)
    @PutMapping(value = "/categories/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryResource resource) {
        return super.updateResource(id, resource);
    }

    /**
     * API delete category
     *
     * @param id category id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API delete category")
    @RequiredAuthentication(value = ApiId.D_CATEGORY)
    @DeleteMapping(value = "/categories/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        return super.deleteResource(id);
    }


    @RequiredAuthentication//(value = ApiId.R_CATEGORY)
    @GetMapping(value = "/color", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getColors() {
        return ResponseEntity.ok(new ResponseData<>().success(categoryService.getColors()));
    }

    @RequiredAuthentication//(value = ApiId.R_CATEGORY)
    @GetMapping(value = "/size", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getSizes() {
        return ResponseEntity.ok(new ResponseData<>().success(categoryService.getSizes()));
    }
}
