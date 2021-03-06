package com.odakota.tms.business.auth.controller;

import com.odakota.tms.business.asserts.service.ExportService;
import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.resource.UserResource;
import com.odakota.tms.business.auth.service.UserService;
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
public class UserController extends BaseController<User, UserResource> {

    private final UserService userService;

    private final ExportService<User> exportService;

    @Autowired
    public UserController(UserService userService,
                          ExportService<User> exportService) {
        super(userService);
        this.userService = userService;
        this.exportService = exportService;
    }

    /**
     * API get users list
     *
     * @param baseReq List acquisition request
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API get users list")
    @RequiredAuthentication(value = ApiId.R_USER)
    @GetMapping(value = "/users", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> getUsers(@ModelAttribute @Valid BaseParameter baseReq) {
        return super.getResources(baseReq);
    }

    /**
     * API create new user
     *
     * @param userResource {@link UserResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API create new user")
    @RequiredAuthentication(value = ApiId.C_USER)
    @PostMapping(value = "/users", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> createUsers(@RequestBody @Validated UserResource userResource) {
        return super.createResource(userResource);
    }

    /**
     * API update user
     *
     * @param id           userId
     * @param userResource {@link UserResource}
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API update user")
    @RequiredAuthentication(value = ApiId.U_USER)
    @PutMapping(value = "/users/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> updateUsers(@PathVariable Long id, @RequestBody @Validated UserResource userResource) {
        return super.updateResource(id, userResource);
    }

    /**
     * API change password user
     *
     * @param id       userId
     * @param password new password
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API change password user")
    @RequiredAuthentication(value = ApiId.U_USER)
    @PutMapping(value = "/users/{id}/change-password", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> changePassword(@PathVariable Long id, @RequestBody String password) {
        userService.changePassword(id, password);
        return ResponseEntity.noContent().build();
    }

    /**
     * API change status user
     *
     * @param id userId
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API change status user")
    @RequiredAuthentication(value = ApiId.U_USER)
    @PutMapping(value = "/users/{id}/status", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> changeStatus(@PathVariable Long id) {
        userService.changeStatus(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * API delete user
     *
     * @param id user id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API delete user")
    @RequiredAuthentication(value = ApiId.D_USER)
    @DeleteMapping(value = "/users/{id}", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> deleteUsers(@PathVariable Long id) {
        return super.deleteResource(id);
    }

    /**
     * API batch delete user
     *
     * @param ids list user id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API batch delete user")
    @RequiredAuthentication(value = ApiId.D_USER)
    @DeleteMapping(value = "/users", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> batchDeleteUsers(@RequestParam String ids) {
        return super.batchDeleteResource(ids);
    }

    /**
     * API batch change status user
     *
     * @param ids list user id
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API batch change status user")
    @RequiredAuthentication(value = ApiId.U_USER)
    @PutMapping(value = "/users/status", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<?> batchChangeStatusUsers(@RequestParam String ids, @RequestParam Boolean status) {
        userService.batchChangeStatus(ids, status);
        return ResponseEntity.noContent().build();
    }

    /**
     * API export users
     *
     * @return {@link ResponseEntity}
     */
    @ApiOperation("API export users")
    @RequiredAuthentication(value = ApiId.E_USER)
    @GetMapping(value = "/users/export", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<byte[]> exportUser(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        //MediaType.parseMediaType("application/vnd.ms-excel")
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(exportService.export(FileGroup.USER, response), headers, HttpStatus.OK);
    }
}
