package com.odakota.tms.mapper;

import com.odakota.tms.business.auth.entity.Branch;
import com.odakota.tms.business.auth.entity.Permission;
import com.odakota.tms.business.auth.entity.Role;
import com.odakota.tms.business.auth.entity.User;
import com.odakota.tms.business.auth.resource.BranchResource;
import com.odakota.tms.business.auth.resource.PermissionResource;
import com.odakota.tms.business.auth.resource.RoleResource;
import com.odakota.tms.business.auth.resource.UserResource;
import com.odakota.tms.business.customer.entity.Customer;
import com.odakota.tms.business.customer.resource.CustomerResource;
import com.odakota.tms.business.notification.entity.Notification;
import com.odakota.tms.business.notification.resource.NotificationResource;
import com.odakota.tms.business.process.entity.ReportSetting;
import com.odakota.tms.business.process.resource.ReportSettingResource;
import com.odakota.tms.business.product.entity.Category;
import com.odakota.tms.business.product.entity.Color;
import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.resource.CategoryResource;
import com.odakota.tms.business.product.resource.ColorResource;
import com.odakota.tms.business.product.resource.ProductResource;
import com.odakota.tms.business.receipt.entity.Receipt;
import com.odakota.tms.business.receipt.entity.ReceiptDetail;
import com.odakota.tms.business.receipt.resource.ReceiptDetailResource;
import com.odakota.tms.business.receipt.resource.ReceiptResource;
import com.odakota.tms.business.sales.entity.Campaign;
import com.odakota.tms.business.sales.entity.SalesOrder;
import com.odakota.tms.business.sales.resource.CampaignResource;
import com.odakota.tms.business.sales.resource.SalesOrderResource;
import com.odakota.tms.enums.auth.Gender;
import com.odakota.tms.enums.customer.CustomerType;
import com.odakota.tms.enums.customer.Segment;
import com.odakota.tms.enums.notify.MsgType;
import com.odakota.tms.enums.notify.Priority;
import com.odakota.tms.enums.notify.SendStatus;
import com.odakota.tms.enums.process.ProcessStatus;
import com.odakota.tms.enums.process.ReportType;
import com.odakota.tms.enums.sale.DiscountType;
import com.odakota.tms.enums.sale.OrderStatus;
import com.odakota.tms.enums.sale.OrderType;
import com.odakota.tms.enums.sale.SaleType;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haidv
 * @version 1.0
 */
@Mapper
public interface ModelMapper {

    /**
     * Convert role entity to role resource
     *
     * @param entity {@link Role}
     * @return {@link RoleResource}
     */
    RoleResource convertToResource(Role entity);

    /**
     * Convert role resource to role entity
     *
     * @param resource {@link RoleResource}
     * @return {@link Role}
     */
    Role convertToEntity(RoleResource resource);

    /**
     * Convert branch entity to branch resource
     *
     * @param entity {@link Branch}
     * @return {@link BranchResource}
     */
    BranchResource convertToResource(Branch entity);

    /**
     * Convert branch resource to branch entity
     *
     * @param resource {@link BranchResource}
     * @return {@link Branch}
     */
    Branch convertToEntity(BranchResource resource);

    /**
     * Convert user entity to user resource
     *
     * @param entity {@link User}
     * @return {@link UserResource}
     */
    @Mapping(source = "sex", target = "sex", qualifiedByName = "getValueSex")
    UserResource convertToResource(User entity);

    /**
     * Convert user resource to user entity
     *
     * @param resource {@link UserResource}
     * @return {@link User}
     */
    @Mapping(source = "sex", target = "sex", qualifiedByName = "getSexByValue")
    User convertToEntity(UserResource resource);

    /**
     * Convert permission entity to permission resource
     *
     * @param entity {@link Permission}
     * @return {@link PermissionResource}
     */
    @Mappings({@Mapping(source = "name", target = "title", qualifiedByName = "getTitle"),
               @Mapping(source = "id", target = "key", qualifiedByName = "getKey")}
    )
    PermissionResource convertToResource(Permission entity);

    /**
     * Convert customer entity to customer resource
     *
     * @param entity {@link Customer}
     * @return {@link CustomerResource}
     */
    @Mappings({@Mapping(source = "sex", target = "sex", qualifiedByName = "getValueSex"),
               @Mapping(source = "customerType", target = "customerType", qualifiedByName = "getValueCustomerType"),
               @Mapping(source = "customerSegment", target = "customerSegment", qualifiedByName = "getValueSegment")})
    CustomerResource convertToResource(Customer entity);

    /**
     * Convert customer resource to customer entity
     *
     * @param resource {@link CustomerResource}
     * @return {@link Customer}
     */
    @Mappings({@Mapping(source = "sex", target = "sex", qualifiedByName = "getSexByValue"),
               @Mapping(source = "customerType", target = "customerType", qualifiedByName = "getCustomerTypeByValue"),
               @Mapping(source = "customerSegment", target = "customerSegment", qualifiedByName = "getSegmentByValue")})
    Customer convertToEntity(CustomerResource resource);

    /**
     * Convert notification entity to notification resource
     *
     * @param entity {@link Notification}
     * @return {@link NotificationResource}
     */
    @Mappings({@Mapping(source = "priority", target = "priority", qualifiedByName = "getValuePriority"),
               @Mapping(source = "type", target = "type", qualifiedByName = "getValueMsgType"),
               @Mapping(source = "sendStatus", target = "sendStatus", qualifiedByName = "getValueSendStatus")}
    )
    NotificationResource convertToResource(Notification entity);

    /**
     * Convert notification resource to notification entity
     *
     * @param resource {@link NotificationResource}
     * @return {@link Notification}
     */
    @Mappings({@Mapping(source = "priority", target = "priority", qualifiedByName = "getPriorityByValue"),
               @Mapping(source = "type", target = "type", qualifiedByName = "getMsgTypeByValue"),
               @Mapping(source = "sendStatus", target = "sendStatus", qualifiedByName = "getSendStatusByValue")}
    )
    Notification convertToEntity(NotificationResource resource);

    /**
     * Convert category entity to category resource
     *
     * @param entity {@link Category}
     * @return {@link CategoryResource}
     */
    CategoryResource convertToResource(Category entity);

    /**
     * Convert category resource to category entity
     *
     * @param resource {@link CategoryResource}
     * @return {@link Category}
     */
    Category convertToEntity(CategoryResource resource);

    /**
     * Convert campaign entity to campaign resource
     *
     * @param entity {@link Campaign}
     * @return {@link CampaignResource}
     */
    @Mapping(source = "discountType", target = "discountType", qualifiedByName = "getValueDiscountType")
    CampaignResource convertToResource(Campaign entity);

    /**
     * Convert campaign resource to campaign entity
     *
     * @param resource {@link CampaignResource}
     * @return {@link Campaign}
     */
    @Mapping(source = "discountType", target = "discountType", qualifiedByName = "getDiscountTypeByValue")
    Campaign convertToEntity(CampaignResource resource);

    /**
     * Convert product resource to product entity
     *
     * @param resource {@link ProductResource}
     * @return {@link Product}
     */
    Product convertToEntity(ProductResource resource);

    /**
     * Convert product resource to product entity
     *
     * @param entity {@link ProductResource}
     * @return {@link Product}
     */
    ProductResource convertToResource(Product entity);

    /**
     * Convert receipt resource to receipt entity
     *
     * @param entity {@link ReceiptResource}
     * @return {@link Receipt}
     */
    ReceiptResource convertToResource(Receipt entity);

    /**
     * Convert receipt resource to receipt entity
     *
     * @param resource {@link ReceiptResource}
     * @return {@link Receipt}
     */
    Receipt convertToEntity(ReceiptResource resource);

    /**
     * Convert receipt detail resource to receipt detail entity
     *
     * @param entity {@link ReceiptDetail}
     * @return {@link ReceiptDetailResource}
     */
    ReceiptDetailResource convertToResource(ReceiptDetail entity);

    /**
     * Convert receipt detail resource to receipt detail entity
     *
     * @param resource {@link ReceiptDetailResource}
     * @return {@link ReceiptDetail}
     */
    ReceiptDetail convertToEntity(ReceiptDetailResource resource);

    /**
     * Convert order resource to order entity
     *
     * @param entity {@link SalesOrder}
     * @return {@link SalesOrderResource}
     */
    @Mappings({@Mapping(source = "status", target = "status", qualifiedByName = "getValueOrderStatus"),
               @Mapping(source = "saleType", target = "saleType", qualifiedByName = "getValueSaleType"),
               @Mapping(source = "orderType", target = "orderType", qualifiedByName = "getValueOrderType")}
    )
    SalesOrderResource convertToResource(SalesOrder entity);

//    /**
//     * Convert order entity to order resource
//     *
//     * @param resource {@link SalesOrderResource}
//     * @return {@link SalesOrder}
//     */
//    @Mapping(source = "status", target = "status", qualifiedByName = "getOrderStatusByValue")
//    SalesOrder convertToEntity(SalesOrderResource resource);

    /**
     * Convert color resource to color entity
     *
     * @param entity {@link Color}
     * @return {@link ColorResource}
     */
    ColorResource convertToResource(Color entity);

    @Named("getValueSex")
    default Integer getValueSex(Gender gender) {
        return gender == null ? null : gender.getValue();
    }

    @Named("getSexByValue")
    default Gender getSexByValue(Integer value) {
        return Gender.of(value);
    }

    @Named("getTitle")
    default String getTitle(String name) {
        return name;
    }

    @Named("getKey")
    default Long getKey(Long id) {
        return id;
    }

    @Named("getValueCustomerType")
    default Integer getValueCustomerType(CustomerType customerType) {
        return customerType == null ? null : customerType.getValue();
    }

    @Named("getCustomerTypeByValue")
    default CustomerType getCustomerTypeByValue(Integer value) {
        return CustomerType.of(value);
    }

    @Named("getValuePriority")
    default Integer getValuePriority(Priority priority) {
        return priority == null ? null : priority.getValue();
    }

    @Named("getPriorityByValue")
    default Priority getPriorityByValue(Integer value) {
        return Priority.of(value);
    }

    @Named("getValueSendStatus")
    default Integer getValueSendStatus(SendStatus sendStatus) {
        return sendStatus == null ? null : sendStatus.getValue();
    }

    @Named("getSendStatusByValue")
    default SendStatus getSendStatusByValue(Integer value) {
        return SendStatus.of(value);
    }

    @Named("getValueMsgType")
    default Integer getValueMsgType(MsgType msgType) {
        return msgType == null ? null : msgType.getValue();
    }

    @Named("getMsgTypeByValue")
    default MsgType getMsgTypeByValue(Integer value) {
        return MsgType.of(value);
    }

    @Named("getValueDiscountType")
    default Integer getValueDiscountType(DiscountType discountType) {
        return discountType == null ? null : discountType.getValue();
    }

    @Named("getDiscountTypeByValue")
    default DiscountType getDiscountTypeByValue(Integer value) {
        return DiscountType.of(value);
    }

    @Named("getValueOrderStatus")
    default Integer getValueOrderStatus(OrderStatus orderStatus) {
        return orderStatus == null ? null : orderStatus.getValue();
    }

    @Named("getOrderStatusByValue")
    default OrderStatus getOrderStatusByValue(Integer value) {
        return OrderStatus.of(value);
    }

    @Named("getValueSegment")
    default Integer getValueSegment(Segment orderStatus) {
        return orderStatus == null ? null : orderStatus.getValue();
    }

    @Named("getSegmentByValue")
    default Segment getSegmentByValue(Integer value) {
        return Segment.of(value);
    }

    @Named("getValueOrderType")
    default Integer getValueOrderType(OrderType orderStatus) {
        return orderStatus == null ? null : orderStatus.getValue();
    }

    @Named("getValueSaleType")
    default Integer getValueSaleType(SaleType saleType) {
        return saleType == null ? null : saleType.getValue();
    }
    /**
     * Convert report setting resource to list report setting entity
     *
     * @param resource {@link ReportSettingResource}
     * @return {@link ReportSetting}
     */
    default List<ReportSetting> convertToEntity(ReportSettingResource resource, Long branchId) {
        List<ReportSetting> reportSettings = new ArrayList<>();
        ReportSetting reportSetting;
        if (branchId != null) {
            reportSetting = new ReportSetting();
            reportSetting.setBranchId(branchId);
            reportSetting.setType(ReportType.INVENTORY);
            reportSetting.setDailyTime(resource.getDaily1());
            reportSetting.setReceiver(StringUtils.join(resource.getReceiver1(), ","));
            reportSetting.setStatus(ProcessStatus.of(resource.getStatus1()));
            reportSettings.add(reportSetting);
        }
        reportSetting = new ReportSetting();
        reportSetting.setBranchId(branchId);
        reportSetting.setType(ReportType.SALE_SUMMARY);
        if (branchId != null) {
            reportSetting.setDailyTime(resource.getDaily2());
        }
        reportSetting.setReceiver(StringUtils.join(resource.getReceiver2(), ","));
        reportSetting.setStatus(ProcessStatus.of(resource.getStatus2()));
        reportSetting.setMonthlyDay(resource.getMonthlyDay2());
        reportSetting.setMonthlyTime(resource.getMonthlyTime2());
        reportSetting.setYearlyDay(resource.getYearlyDay2());
        reportSetting.setYearlyMonth(resource.getYearlyMonth2());
        reportSetting.setYearlyTime(resource.getYearlyTime2());
        reportSettings.add(reportSetting);
        reportSetting = new ReportSetting();
        reportSetting.setBranchId(branchId);
        reportSetting.setType(ReportType.REVENUE_BY_STAFF);
        reportSetting.setReceiver(StringUtils.join(resource.getReceiver3(), ","));
        reportSetting.setStatus(ProcessStatus.of(resource.getStatus3()));
        reportSetting.setMonthlyDay(resource.getMonthlyDay3());
        reportSetting.setMonthlyTime(resource.getMonthlyTime3());
        reportSetting.setYearlyDay(resource.getYearlyDay3());
        reportSetting.setYearlyMonth(resource.getYearlyMonth3());
        reportSetting.setYearlyTime(resource.getYearlyTime3());
        reportSettings.add(reportSetting);
        reportSetting = new ReportSetting();
        reportSetting.setBranchId(branchId);
        reportSetting.setType(ReportType.BESTSELLERS);
        reportSetting.setReceiver(StringUtils.join(resource.getReceiver4(), ","));
        reportSetting.setStatus(ProcessStatus.of(resource.getStatus4()));
        reportSetting.setMonthlyDay(resource.getMonthlyDay4());
        reportSetting.setMonthlyTime(resource.getMonthlyTime4());
        reportSetting.setYearlyDay(resource.getYearlyDay4());
        reportSetting.setYearlyMonth(resource.getYearlyMonth4());
        reportSetting.setYearlyTime(resource.getYearlyTime4());
        reportSettings.add(reportSetting);
        return reportSettings;
    }

    /**
     * Convert list report setting setting entity to report resource
     *
     * @param entity {@link ReportSetting}
     * @return {@link ReportSettingResource}
     */
    default ReportSettingResource convertToResource(List<ReportSetting> entity) {
        ReportSettingResource settingResource = new ReportSettingResource();
        entity.forEach(var -> {
            if (var.getType().equals(ReportType.INVENTORY)) {
                settingResource.setDaily1(var.getDailyTime());
                settingResource.setStatus1(var.getStatus().getValue());
                settingResource.setReceiver1(Arrays.stream(var.getReceiver().split(","))
                                                   .map(Integer::parseInt).collect(Collectors.toList()));
            }
            if (var.getType().equals(ReportType.SALE_SUMMARY)) {
                settingResource.setDaily2(var.getDailyTime());
                settingResource.setStatus2(var.getStatus().getValue());
                settingResource.setReceiver2(Arrays.stream(var.getReceiver().split(","))
                                                   .map(Integer::parseInt).collect(Collectors.toList()));
                settingResource.setMonthlyDay2(var.getMonthlyDay());
                settingResource.setMonthlyTime2(var.getMonthlyTime());
                settingResource.setYearlyMonth2(var.getYearlyMonth());
                settingResource.setYearlyDay2(var.getYearlyDay());
                settingResource.setYearlyTime2(var.getYearlyTime());
            }
            if (var.getType().equals(ReportType.REVENUE_BY_STAFF)) {
                settingResource.setStatus3(var.getStatus().getValue());
                settingResource.setReceiver3(Arrays.stream(var.getReceiver().split(","))
                                                   .map(Integer::parseInt).collect(Collectors.toList()));
                settingResource.setMonthlyDay3(var.getMonthlyDay());
                settingResource.setMonthlyTime3(var.getMonthlyTime());
                settingResource.setYearlyMonth3(var.getYearlyMonth());
                settingResource.setYearlyDay3(var.getYearlyDay());
                settingResource.setYearlyTime3(var.getYearlyTime());
            }
            if (var.getType().equals(ReportType.BESTSELLERS)) {
                settingResource.setStatus4(var.getStatus().getValue());
                settingResource.setReceiver4(Arrays.stream(var.getReceiver().split(","))
                                                   .map(Integer::parseInt).collect(Collectors.toList()));
                settingResource.setMonthlyDay4(var.getMonthlyDay());
                settingResource.setMonthlyTime4(var.getMonthlyTime());
                settingResource.setYearlyMonth4(var.getYearlyMonth());
                settingResource.setYearlyDay4(var.getYearlyDay());
                settingResource.setYearlyTime4(var.getYearlyTime());
            }
        });
        return settingResource;
    }
}
