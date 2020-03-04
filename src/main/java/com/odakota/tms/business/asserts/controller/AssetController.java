package com.odakota.tms.business.asserts.controller;

import com.odakota.tms.business.asserts.resource.UploadResource;
import com.odakota.tms.business.asserts.service.DistrictService;
import com.odakota.tms.business.asserts.service.ProvinceService;
import com.odakota.tms.business.asserts.service.UploadService;
import com.odakota.tms.business.asserts.service.WardService;
import com.odakota.tms.constant.ApiVersion;
import com.odakota.tms.enums.file.FileType;
import com.odakota.tms.system.annotations.NoAuthentication;
import com.odakota.tms.system.config.data.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author haidv
 * @version 1.0
 */
@RestController
public class AssetController {

    private final UploadService assetService;

    private final ProvinceService provinceService;

    private final WardService wardService;

    private final DistrictService districtService;

    @Autowired
    public AssetController(UploadService assetService,
                           ProvinceService provinceService,
                           WardService wardService,
                           DistrictService districtService) {
        this.assetService = assetService;
        this.provinceService = provinceService;
        this.wardService = wardService;
        this.districtService = districtService;
    }

    /**
     * upload an asset image
     *
     * @param file     upload file
     * @param fileType image type
     * @return file information file name file path
     */
    @SuppressWarnings("unchecked")
    @NoAuthentication
    @ApiOperation("upload an asset image")
    @PostMapping(value = "/upload-assets", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData<UploadResource>> updateAssetFile(@RequestParam MultipartFile file,
                                                                        @RequestParam FileType fileType) {
        return ResponseEntity.ok(new ResponseData<>().success(assetService.uploadFileToS3(file, fileType)));
    }

    /**
     * API get list province in VietNam
     *
     * @return {@link ResponseEntity}
     */
    @SuppressWarnings("unchecked")
    @NoAuthentication
    @ApiOperation("API get list province in VietNam")
    @GetMapping(value = "/province", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData<Object>> getProvinces() {
        return ResponseEntity.ok(new ResponseData<>().success(provinceService.getProvinces()));
    }

    /**
     * API get list district of a province in VietNam
     *
     * @param id province id
     * @return {@link ResponseEntity}
     */
    @SuppressWarnings("unchecked")
    @NoAuthentication
    @ApiOperation("API get list district of a province in VietNam")
    @GetMapping(value = "/province/{id}/district", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData<Object>> getDistricts(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseData<>().success(districtService.getDistricts(id)));
    }

    /**
     * API get list district in VietNam
     *
     * @return {@link ResponseEntity}
     */
    @SuppressWarnings("unchecked")
    @NoAuthentication
    @ApiOperation("API get list district in VietNam")
    @GetMapping(value = "/district", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData<Object>> getDistricts() {
        return ResponseEntity.ok(new ResponseData<>().success(districtService.getDistricts()));
    }

    /**
     * API get list ward of a district in VietNam
     *
     * @return {@link ResponseEntity}
     */
    @SuppressWarnings("unchecked")
    @NoAuthentication
    @ApiOperation("API get list ward of a district in VietNam")
    @GetMapping(value = "/district/{id}/ward", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData<Object>> getWards(@PathVariable Long id) {
        return ResponseEntity.ok(new ResponseData<>().success(wardService.getWards(id)));
    }

    /**
     * API get list ward in VietNam
     *
     * @return {@link ResponseEntity}
     */
    @SuppressWarnings("unchecked")
    @NoAuthentication
    @ApiOperation("API get list ward in VietNam")
    @GetMapping(value = "/ward", produces = ApiVersion.API_VERSION_1)
    public ResponseEntity<ResponseData<Object>> getWards() {
        return ResponseEntity.ok(new ResponseData<>().success(wardService.getWards()));
    }
}
