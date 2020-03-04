package com.odakota.tms.business.asserts.service;

import com.odakota.tms.business.asserts.entity.Ward;
import com.odakota.tms.business.asserts.repository.WardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Service
public class WardService {

    private final WardRepository wardRepository;

    @Autowired
    public WardService(WardRepository wardRepository) {
        this.wardRepository = wardRepository;
    }

    public List<Ward> getWards() {
        return wardRepository.findAll();
    }

    public List<Ward> getWards(Long districtId) {
        return wardRepository.findByDistrictId(districtId);
    }
}
