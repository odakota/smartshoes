package com.odakota.tms.constant;

/**
 * @author haidv
 * @version 1.0
 */
public class ProcessConstant {

    public static final String SYSTEM_GROUP = "System";

    public static final String NOTIFY_GROUP = "Notify";

    public static final String CAMPAIGN_GROUP = "Campaign";

    public static final String REPORT_GROUP = "Report";

    public static final String MAP_KEY_BRANCH_ID = "branchId";

    public static final String MAP_KEY_CAMPAIGN_ID = "campaignId";

    public static final String MAP_KEY_SELECT_ALL = "selectAll";

    public static final String MAP_KEY_SELECT_CATEGORY = "selectCategory";

    public static final String MAP_KEY_SELECT_PRODUCT = "selectProduct";

    public static final String MAP_KEY_ROLE_ID = "roleId";

    public static final String MAP_KEY_TIME_TYPE = "timeType";

    public static final String MAP_KEY_REPORT_TYPE = "reportType";

    public static final String MAP_VALUE_TIME_TYPE_DAILY = "daily";

    public static final String MAP_VALUE_TIME_TYPE_MONTHLY = "monthly";

    public static final String MAP_VALUE_TIME_TYPE_YEARLY = "yearly";

    //----------------------------------------------------//
    public final static String APPLY_CAMPAIGN_JOB_NAME = "apply campaign job - branch: {0} - campaign: {1}";

    public final static String APPLY_CAMPAIGN_TRIGGER_NAME = "apply campaign trigger - branch: {0} - campaign: {1}";

    public final static String DELETE_TOKEN_JOB_NAME = "auto delete token expired job";

    public final static String DELETE_TOKEN_TRIGGER_NAME = "auto delete token expired trigger";

    public final static String DELETE_TOKEN_CRON_EXPRESSION = "0 0 1 1,15 * ?";

    public final static String SEND_NOTIFY_UPDATE_PERMISSION_JOB_NAME = "send notify update permission job - role: {0} - time: {1}";

    public final static String SEND_NOTIFY_UPDATE_PERMISSION_TRIGGER_NAME = "send notify update permission trigger - role: {0} - time: {1}";

    public final static String HAPPY_BIRTHDAY_JOB_NAME = "happy birthday job";

    public final static String HAPPY_BIRTHDAY_TRIGGER_NAME = "happy birthday trigger";

    public final static String HAPPY_BIRTHDAY_CRON_EXPRESSION = "0 0 0 * * ?";

    public final static String INVENTORY_REPORT_DAILY_JOB_NAME = "inventory report daily job - branch: {0}";

    public final static String INVENTORY_REPORT_DAILY_TRIGGER_NAME = "inventory report daily trigger - branch: {0}";

    public final static String SALE_SUMMARY_REPORT_DAILY_JOB_NAME = "sale summary report daily job - branch: {0}";

    public final static String SALE_SUMMARY_REPORT_DAILY_TRIGGER_NAME = "sale summary report daily trigger - branch: {0}";

    public final static String SALE_SUMMARY_REPORT_MONTHLY_JOB_NAME = "sale summary report monthly job - branch: {0}";

    public final static String SALE_SUMMARY_REPORT_MONTHLY_TRIGGER_NAME = "sale summary report monthly trigger - branch: {0}";

    public final static String SALE_SUMMARY_REPORT_YEARLY_JOB_NAME = "sale summary report yearly job - branch: {0}";

    public final static String SALE_SUMMARY_REPORT_YEARLY_TRIGGER_NAME = "sale summary report yearly trigger - branch: {0}";

    public final static String REVENUE_BY_STAFF_REPORT_MONTHLY_JOB_NAME = "revenue by staff report monthly job - branch: {0}";

    public final static String REVENUE_BY_STAFF_REPORT_MONTHLY_TRIGGER_NAME = "revenue by staff report monthly trigger - branch: {0}";

    public final static String REVENUE_BY_STAFF_REPORT_YEARLY_JOB_NAME = "revenue by staff report yearly job - branch: {0}";

    public final static String REVENUE_BY_STAFF_REPORT_YEARLY_TRIGGER_NAME = "revenue by staff report yearly trigger - branch: {0}";

    public final static String BESTSELLERS_REPORT_MONTHLY_JOB_NAME = "bestseller report monthly job - branch: {0}";

    public final static String BESTSELLERS_REPORT_MONTHLY_TRIGGER_NAME = "bestseller report monthly trigger - branch: {0}";

    public final static String BESTSELLERS_REPORT_YEARLY_JOB_NAME = "bestseller report yearly job - branch: {0}";

    public final static String BESTSELLERS_REPORT_YEARLY_TRIGGER_NAME = "bestseller report yearly trigger - branch: {0}";

    public final static String REPORT_DAILY_CRON_EXPRESSION = "0 {0} {1} * * ?";

    public final static String REPORT_MONTHLY_CRON_EXPRESSION = "0 {0} {1} {2} * ?";

    public final static String REPORT_YEARLY_CRON_EXPRESSION = "0 {0} {1} {2} {3} ?";

    public static final String INVENTORY_REPORT_FILE_NAME = "inventory_report_{0}_{1}.docx";
}
