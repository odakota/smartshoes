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
import com.odakota.tms.business.product.entity.Category;
import com.odakota.tms.business.product.entity.Product;
import com.odakota.tms.business.product.resource.CategoryResource;
import com.odakota.tms.business.product.resource.ProductResource;
import com.odakota.tms.business.receipt.entity.Receipt;
import com.odakota.tms.business.receipt.entity.ReceiptDetail;
import com.odakota.tms.business.receipt.resource.ReceiptDetailResource;
import com.odakota.tms.business.receipt.resource.ReceiptResource;
import com.odakota.tms.business.sales.entity.Campaign;
import com.odakota.tms.business.sales.resource.CampaignResource;
import com.odakota.tms.enums.auth.Gender;
import com.odakota.tms.enums.customer.CustomerType;
import com.odakota.tms.enums.notify.MsgType;
import com.odakota.tms.enums.notify.Priority;
import com.odakota.tms.enums.notify.SendStatus;
import com.odakota.tms.enums.sale.DiscountType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

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
               @Mapping(source = "customerType", target = "customerType", qualifiedByName = "getValueCustomerType")})
    CustomerResource convertToResource(Customer entity);

    /**
     * Convert customer resource to customer entity
     *
     * @param resource {@link CustomerResource}
     * @return {@link Customer}
     */
    @Mappings({@Mapping(source = "sex", target = "sex", qualifiedByName = "getSexByValue"),
               @Mapping(source = "customerType", target = "customerType", qualifiedByName = "getCustomerTypeByValue")})
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
}
