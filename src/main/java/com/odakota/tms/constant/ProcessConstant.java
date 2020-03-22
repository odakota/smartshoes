package com.odakota.tms.constant;

/**
 * @author haidv
 * @version 1.0
 */
public class ProcessConstant {

    public static final String SYSTEM_GROUP = "System";

    public static final String MAP_KEY_BRANCH_ID = "branchId";

    public static final String MAP_KEY_CAMPAIGN_ID = "campaignId";

    public static final String MAP_KEY_SELECT_ALL = "selectAll";

    public static final String MAP_KEY_SELECT_CATEGORY = "selectCategory";

    public static final String MAP_KEY_SELECT_PRODUCT = "selectProduct";

    public static final String MAP_KEY_ROLE_ID = "roleId";

    //----------------------------------------------------//
    public final static String APPLY_CAMPAIGN_JOB_NAME = "apply campaign job - branch: {0} - campaign: {1}";

    public final static String APPLY_CAMPAIGN_TRIGGER_NAME = "apply campaign trigger - branch: {0} - campaign: {1}";

    public final static String DELETE_TOKEN_JOB_NAME = "auto delete token expired job";

    public final static String DELETE_TOKEN_TRIGGER_NAME = "auto delete token expired trigger";

    public final static String DELETE_TOKEN_CRON_EXPRESSION = "0 0 0 1,15 * ?";

    public final static String SEND_NOTIFY_UPDATE_PERMISSION_JOB_NAME = "send notify update permission job - role: {0} - time: {1}";

    public final static String SEND_NOTIFY_UPDATE_PERMISSION_TRIGGER_NAME = "send notify update permission trigger - role: {0} - time: {1}";

    public final static String HAPPY_BIRTHDAY_JOB_NAME = "happy birthday job";

    public final static String HAPPY_BIRTHDAY_TRIGGER_NAME = "happy birthday trigger";

    public final static String HAPPY_BIRTHDAY_CRON_EXPRESSION = "0 0 0 * * ?";

}
