package com.odakota.tms.business.sales.repository;

import com.odakota.tms.business.sales.entity.CampaignItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author haidv
 * @version 1.0
 */
@Repository
public interface CampaignItemRepository extends JpaRepository<CampaignItem, Long> {
}
