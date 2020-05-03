package com.odakota.tms.constant;

/**
 * @author haidv
 * @version 1.0
 */
public class Constant {

    // DEFAULT VALUE
    public static final long ROLE_ID_DEFAULT = 2; // roleId = 1 : root; roleId = 2 : normal role
    public static final int USER_ID_DEFAULT = 1;
    public static final int BRANCH_ID_DEFAULT = 1;
    public static final int NUMBER_OF_ROLE_DEFAULT = 2;
    public static final int NUMBER_OF_ACCOUNT_DEFAULT = 2;
    public static final int MENU_TYPE_SUB_MENU = 1;
    public static final int MENU_TYPE_MENU = 0;
    public static final int MENU_TYPE_BUTTON = 2;
    // OPERATION FILTER
    public static final String OPERATION_EQUAL = "=";
    public static final String OPERATION_NOT_EQUAL = "!=";
    public static final String OPERATION_LIKE = "like";
    // TOKEN
    public static final String TOKEN_CLAIM_USER_ID = "userId";
    public static final String TOKEN_CLAIM_CUSTOMER_ID = "customerId";
    public static final String TOKEN_CLAIM_ROLE_ID = "roleIds";
    public static final String TOKEN_CLAIM_BRANCH_ID = "branchId";
    // DATETIME FORMAT
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String DD_MM_YYYY_HH_MM_SS = "dd-MM-yyyy HH:mm:ss";
    public static final String HH_MM_SS_DD_MM_YYYY = "HH:mm:ss dd-MM-yyyy";
    // OTP PREFIX KEY
    public static final String LOGIN_PHONE_PREFIX_KEY = "login-otp-";
    public static final String FORGOT_PASS_PREFIX_KEY = "forgot-otp-";
    // OTP FIELD NAME
    public static final String OTP_CODE_OTP = "otp";
    public static final String OTP_DATA = "data";
    // VIEW CONSTANT
    public static final String MODEL_LIST_DATA = "list";
    public static final String MODEL_SHEET_NAME = "sheet";
    public static final String MODEL_CLAZZ = "clazz";

    private Constant() {
    }
}
