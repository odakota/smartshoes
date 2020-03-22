package com.odakota.tms.business.process.job;

import com.odakota.tms.business.process.service.QuartzScheduleService;
import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.repository.ProductRepository;
import com.odakota.tms.business.sales.entity.CampaignItem;
import com.odakota.tms.business.sales.repository.CampaignItemRepository;
import com.odakota.tms.constant.ProcessConstant;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
public class ApplyCampaignItemJob extends QuartzJobBean {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CampaignItemRepository campaignItemRepository;

    @Autowired
    private QuartzScheduleService quartzScheduleService;

    /**
     * Execute the actual job. The job data map will already have been applied as bean property values by execute. The
     * contract is exactly the same as for the standard Quartz execute method.
     *
     * @param context {@link JobExecutionContext}
     * @see #execute
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("-------{} start-----", ApplyCampaignItemJob.class.getSimpleName());

        JobDetail jobDetail = context.getJobDetail();
        try {
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            Long campaignId = jobDataMap.getLongFromString(ProcessConstant.MAP_KEY_CAMPAIGN_ID);
            List<Long> selectCategory = (List<Long>) jobDataMap.get(ProcessConstant.MAP_KEY_SELECT_CATEGORY);
            List<Long> selectProduct = (List<Long>) jobDataMap.get(ProcessConstant.MAP_KEY_SELECT_PRODUCT);
            if (jobDataMap.getBooleanFromString(ProcessConstant.MAP_KEY_SELECT_ALL)) {
                List<Product> products = productRepository.findAllByBranch();
                products.forEach(var -> {
                    CampaignItem campaignItem = new CampaignItem();
                    campaignItem.setCampaignId(campaignId);
                    campaignItem.setProductId(var.getId());
                    campaignItemRepository.save(campaignItem);
                });
            }
            if (!selectCategory.isEmpty()) {
                List<Product> products = productRepository.findAllByCategoryAndBranch(selectCategory);
                products.forEach(var -> {
                    CampaignItem campaignItem = new CampaignItem();
                    campaignItem.setCampaignId(campaignId);
                    campaignItem.setProductId(var.getId());
                    campaignItemRepository.save(campaignItem);
                });
            }
            if (!selectProduct.isEmpty()) {
                selectProduct.forEach(var -> {
                    CampaignItem campaignItem = new CampaignItem();
                    campaignItem.setCampaignId(campaignId);
                    campaignItem.setProductId(var);
                    campaignItemRepository.save(campaignItem);
                });
            }
        } catch (Exception e) {
            quartzScheduleService.sendMailWhenJobFailed(jobDetail.getKey().getName());
        }
        log.info("-------{} end-----", ApplyCampaignItemJob.class.getSimpleName());
    }
}
