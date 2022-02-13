package com.tms.api.exception;

public enum ErrorMessage {
	BAD_REQUEST(400, "bad_request"),
    CONNECTION_DATABASE_FAIL(600, "connection_data_fail"),
    APIKEY_INVALID(600, "apikey_invalid"),
    CONNECTION_REDIS_FAIL(601, "connection_redis_fail"),
    QUEUE_FAIL(602, "queue_fail"),
    AUTHEN_FAIL(603, "authen_fail"),
    SUCCESS(200, "success"),
	INVALID_PARAM(400001, "invalid_input_params"),
	REPORT_ERROR(400002, "cannot_generate_report"),
    CONTRAINS_EXCEPTION (400003,"duplicate key value violates unique constraint"),
    DO_HAVE_FFM_ID_OR_CARRIER_ID_NULL(400_004, "DO have ffmId or carrierId is null"),
    CAN_NOT_CANCEL_DO_WITH_FINAL_STATUS(400_005, "Can not cancel DO with final status"),

	FORBIDDEN_API(40301, "cannot_access_api"),
	
	NOT_FOUND(404, "resource_not_found"),
	
    LEAD_NOT_FOUND(404001, "lead_not_found"),
    USER_NOT_FOUND(404002, "user_not_found"),
    PASSWORD_NOT_MATCH(404000, "password_not_match"),
    GROUP_NOT_FOUND(404003, "group_not_found"),
    PROVINCE_NOT_FOUND(404004, "province_not_found"),
    DISTRICT_NOT_FOUND(404005, "district_not_found"),
    SUBDISTRICT_NOT_FOUND(404006, "subdistrict_not_found"),
    CAMPAIGN_NOT_FOUND(404007, "campaign_not_found"),
    DELIVERY_ORDER_NOT_FOUND(404008, "delivery_order_not_found"),
    CARRIER_NOT_FOUND(404009, "carrier_not_found"),
    TRY_APPROVE_LEAD_FAILED(404010, "try_to_approve_lead_fail"),
    CAN_NOT_SET_UNCALL(404012, "can_not_set_uncall"),
    CANNOT_SAVE_UNCALL(404011, "cannot_save_uncall"),
    CANNOT_SAVE_LEAD(404021, "cannot_save_lead"),
    LEAD_IS_APPROVED(404031, "lead_is_approved"),
    CAN_NOT_GET_LEAD(400041, "can_not_get_lead"),
    MISSING_REASON(404013, "missing_reason"),
    FFM_NOT_FOUND(404_014, "Fulfillment not found"),
    THREE_PLS_NOT_FOUND(404_015, "ThreePls not found"),

    UNSUPPORTED_MEDIA_TYPE(415000, "Unsupported_Media_Type"),
    ;

    /** The error code. */
    private int    errorCode;

    /** The message. */
    private String message;

    /**
     * Instantiates a new error message.
     * 
     * @param pCode
     *            the code
     * @param pMessage
     *            the message
     */
    ErrorMessage(int pCode, String pMessage) {
        errorCode = pCode;
        message = pMessage;
    }

    /**
     * Gets the code.
     * 
     * @return the code
     */
    public int getCode() {
        return errorCode;
    }

    /**
     * Gets the message.
     * 
     * @return the message
     */
    public String getMessage() {
        return message;
    }
}
