package com.odakota.tms.business.process.job;

import com.odakota.tms.business.auth.repository.UserRepository;
import com.odakota.tms.business.process.entity.BestsellerReport;
import com.odakota.tms.business.process.entity.InventoryReport;
import com.odakota.tms.business.process.repository.BestsellerReportRepository;
import com.odakota.tms.business.process.repository.InventoryReportRepository;
import com.odakota.tms.business.process.service.QuartzScheduleService;
import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.repository.ProductRepository;
import com.odakota.tms.business.receipt.entity.ReceiptDetail;
import com.odakota.tms.business.receipt.repository.ReceiptDetailRepository;
import com.odakota.tms.business.sales.entity.SalesOrderDetail;
import com.odakota.tms.business.sales.repository.SalesOrderDetailRepository;
import com.odakota.tms.constant.Constant;
import com.odakota.tms.constant.ProcessConstant;
import com.odakota.tms.enums.file.TemplateName;
import com.odakota.tms.enums.process.ReportType;
import com.odakota.tms.system.service.email.SendMailService;
import com.odakota.tms.system.service.storage.S3StorageService;
import com.odakota.tms.utils.Utils;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Slf4j
@Component
public class ReportJob extends QuartzJobBean {

    @Autowired
    private QuartzScheduleService quartzScheduleService;

    @Autowired
    private InventoryReportRepository inventoryReportRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReceiptDetailRepository receiptDetailRepository;

    @Autowired
    private SalesOrderDetailRepository salesOrderDetailRepository;

    @Autowired
    private BestsellerReportRepository bestsellerReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private S3StorageService storageService;

    @Autowired
    private SendMailService sendMailService;

    @Value("${spring.file-path.report}")
    private String outFilePath;

    @Value("${storage-service.files-storage-name}")
    private String filesStorageName;

    /**
     * Execute the actual job. The job data map will already have been applied as bean property values by execute. The
     * contract is exactly the same as for the standard Quartz execute method.
     *
     * @param context {@link JobExecutionContext}
     * @see #execute
     */
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        log.info("-------{} start-----", ReportJob.class.getSimpleName());
        JobDetail jobDetail = context.getJobDetail();
        try {
            JobDataMap jobDataMap = jobDetail.getJobDataMap();
            Long branchId = (Long) jobDataMap.get(ProcessConstant.MAP_KEY_BRANCH_ID);
            ReportType reportType = (ReportType) jobDataMap.get(ProcessConstant.MAP_KEY_REPORT_TYPE);
            if (ReportType.INVENTORY.equals(reportType)) {
                buildInventoryReport(branchId);
            }
            if (ReportType.BESTSELLERS_OF_MONTH.equals(reportType)) {
                String reportDate = Utils.convertToStringFormat(Utils.calculationTimeMonth(new Date(), -1),
                                                                Constant.YYYY_MM_DD).substring(0, 7);
                String format = "YYYY-MM";
                buildBestsellerReport(branchId, reportDate, format);
            }
            if (ReportType.BESTSELLERS_OF_YEAR.equals(reportType)) {
                String reportDate = Utils.getCurrentYear() - 1 + "";
                String format = "YYYY";
                buildBestsellerReport(branchId, reportDate, format);
            }
//            String localFilePath = fillToFormInventory();
//            File file = new File(localFilePath);
//            String fileName = localFilePath.replace(outFilePath, "");
//            Map<String, Object> map = new HashMap<>();
//            map.put("job", "jobName");
//            map.put("time", Utils.convertToStringFormat(new Date(), Constant.HH_MM_SS_DD_MM_YYYY));
//
//            sendMailService.sendMailWithAttachment("Warns of failed processes", "doanhai8080@gmail.com",
//                                                   TemplateName.TEMPLATE_JOB_ERROR, map, fileName,
//                                                   IOUtils.toByteArray(new FileInputStream(file)),
//                                                   MediaType.APPLICATION_OCTET_STREAM_VALUE);
//            storageService.put(filesStorageName + FilePath.INVENTORY_FILE_PATH + fileName, file);
        } catch (Exception e) {
            log.error("ReportJob exception: ", e);
            quartzScheduleService.sendMailWhenJobFailed(jobDetail.getKey().getName());
        }
        log.info("-------{} end-----", ReportJob.class.getSimpleName());
    }

    private void buildBestsellerReport(Long branchId, String reportDate, String format) {
        List<Product> products = productRepository.findAllByBranch();
        List<SalesOrderDetail> salesOrders = salesOrderDetailRepository
                .findAllPreMonthByBranch(branchId, reportDate, format);
        List<BestsellerReport> bestsellerReports = new ArrayList<>();
        for (Product product : products) {
            BestsellerReport bestsellerReport = new BestsellerReport();
            bestsellerReport.setBranchId(branchId);
            bestsellerReport.setProductName(product.getName());
            bestsellerReport.setProductCode(product.getCode());
            bestsellerReport.setReportDate(reportDate);
            bestsellerReport.setTotalSale(salesOrders.stream()
                                                     .filter(tmp -> product.getCode()
                                                                           .equalsIgnoreCase(
                                                                                   tmp.getProductCode()))
                                                     .mapToLong(SalesOrderDetail::getAmountProduct).sum());
            bestsellerReports.add(bestsellerReport);
        }
        bestsellerReports = bestsellerReports.stream()
                                             .sorted(Comparator.comparingLong(BestsellerReport::getTotalSale))
                                             .collect(Collectors.toList());
        int size = bestsellerReports.size();
        if (branchId == null) {
            bestsellerReports = size > 20 ? bestsellerReports.subList(0, 20) : bestsellerReports;
        } else {
            bestsellerReports = size > 10 ? bestsellerReports.subList(0, 10) : bestsellerReports;
        }
        size = bestsellerReports.size();
        for (BestsellerReport tmp : bestsellerReports) {
            tmp.setOrderNumber(size);
            size--;
        }
        bestsellerReportRepository.saveAll(bestsellerReports);
    }

    private void buildInventoryReport(Long branchId) {
        List<InventoryReport> inventoryReports = new ArrayList<>();
        List<Product> products = productRepository.findAllByBranch();
        List<ReceiptDetail> receiptDetails = receiptDetailRepository.findAllByBranch(branchId, Utils
                .calculationTime(new Date(), -1));
        List<SalesOrderDetail> salesOrders = salesOrderDetailRepository
                .findAllYesterdayByBranch(branchId,
                                          Utils.convertToStringFormat(Utils.calculationTime(new Date(), -1),
                                                                      Constant.YYYY_MM_DD));
        List<InventoryReport> yesterdayReport = inventoryReportRepository
                .findByBranchId(branchId, Utils.calculationTime(new Date(), -2));
        for (Product product : products) {
            InventoryReport inventoryReport = new InventoryReport();
            inventoryReport.setBranchId(branchId);
            inventoryReport.setProductCode(product.getCode());
            inventoryReport.setProductName(product.getName());
            inventoryReport.setReportDate(Utils.calculationTime(new Date(), -1));
            long inputTotal = receiptDetails.stream()
                                            .filter(tmp -> product.getCode()
                                                                  .equalsIgnoreCase(tmp.getProductCode()))
                                            .mapToLong(ReceiptDetail::getAmount).sum();
            inventoryReport.setInputTotal(inputTotal);
            long outputTotal = salesOrders.stream()
                                          .filter(tmp -> product.getCode()
                                                                .equalsIgnoreCase(tmp.getProductCode()))
                                          .mapToLong(SalesOrderDetail::getAmountProduct).sum();
            inventoryReport.setOutputTotal(outputTotal);
            long preTotal = yesterdayReport.stream()
                                           .filter(tmp -> product.getCode()
                                                                 .equalsIgnoreCase(tmp.getProductCode()))
                                           .mapToLong(InventoryReport::getFinalTotal).sum();
            inventoryReport.setPreTotal(preTotal);
            inventoryReport.setFinalTotal(preTotal + inputTotal - outputTotal);
            inventoryReports.add(inventoryReport);
        }
        if (!inventoryReports.isEmpty()) {
            inventoryReportRepository.saveAll(inventoryReports);
        }
    }

    private String fillToFormInventory() {
        OutputStream out = null;
        InputStream in = null;
        String outPath = "";
        try {
            in = new ClassPathResource(TemplateName.INVENTORY_REPORT_TEMPLATE.getValue()).getInputStream();
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in, TemplateEngineKind.Velocity);
            IContext context = report.createContext();
            context.put("branchName", "HN");
            context.put("reportNumber", "123456");
            String currentDate = Utils.convertToStringFormat(Calendar.getInstance().getTime(), Constant.YYYY_MM_DD);
            context.put("currentDate", currentDate);
            FieldsMetadata metadata = new FieldsMetadata();
            metadata.addFieldAsList("prod.code");
            metadata.addFieldAsList("prod.name");
            metadata.addFieldAsList("prod.preTotal");
            metadata.addFieldAsList("prod.inTotal");
            metadata.addFieldAsList("prod.outTotal");
            metadata.addFieldAsList("prod.summary");
            report.setFieldsMetadata(metadata);

            context.put("prod", new ArrayList<>());

            outPath = outFilePath + ProcessConstant.INVENTORY_REPORT_FILE_NAME.replace("{0}", "VN1")
                                                                              .replace("{1}",
                                                                                       currentDate.replace("-", ""));
            out = new FileOutputStream(new File(outPath));
            report.process(context, out);
        } catch (XDocReportException | IOException e) {
            close(out, in);
            log.error("Fill data to file error: ", e);
        } finally {
            close(out, in);
        }
        return outPath;
    }

    private void close(OutputStream out, InputStream in) {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            log.error("Close file stream error: ", e);
        }
    }
}
