package com.odakota.tms.business.asserts.service;

import com.odakota.tms.business.asserts.entity.District;
import com.odakota.tms.business.asserts.repository.DistrictRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class DistrictService {

    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictService(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    public List<District> getDistricts() {
        return districtRepository.findAll();
    }

    public List<District> getDistricts(Long provinceId) {
        return districtRepository.findByProvinceId(provinceId);
    }
}
