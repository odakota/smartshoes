package com.odakota.tms.business.asserts.service;

import com.odakota.tms.business.asserts.entity.Province;
import com.odakota.tms.business.asserts.repository.ProvinceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class ProvinceService {

    private final ProvinceRepository provinceRepository;

    @Autowired
    public ProvinceService(ProvinceRepository provinceRepository) {
        this.provinceRepository = provinceRepository;
    }

    /**
     * Get all province
     *
     * @return list province
     */
    public List<Province> getProvinces() {
        return provinceRepository.findAll();
    }
}
