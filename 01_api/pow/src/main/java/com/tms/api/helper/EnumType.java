package com.tms.api.helper;

import com.tms.api.utils.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class EnumType {
    public enum LEAD_STATUS {
        NEW(1),
        APPROVED(2),
        REJECTED(3),
        DUPLICATED(4),
        INVALID(5),
        CLOSED(6),
        UNREACHABLE(7),
        CALLBACK_CONSULTING(8),
        CALLBACK_NOT_PROPECT(9),
        BUSY(10),
        NOANSWER(11),
        URGENT(12),
        APPROVED_TEMP(13),
        CALLBACK_POTENTIAL(14);

        private final int status;

        LEAD_STATUS(int status) {
            this.status = status;
        }

        public static boolean isContainsStatus(Integer statusRequest){
            List<Integer> list = new ArrayList<>();
            for (LEAD_STATUS leadStatus: LEAD_STATUS.values()) {
                list.add(leadStatus.getValue());
            }
            return CollectionUtils.containsInstance(list, statusRequest);
        }

        public static boolean isCallback(int _status) {
            return _status == CALLBACK_NOT_PROPECT.getValue() || _status == CALLBACK_CONSULTING.getValue() || _status == CALLBACK_POTENTIAL.getValue();

        }

        public static boolean isUncall(int _status) {
            if (!ObjectUtils.allNotNull(_status)) {
                return false;
            }
            return _status == BUSY.getValue() || _status == UNREACHABLE.getValue() || _status == NOANSWER.getValue();

        }

        public static boolean isAvailable(int _status) {
            if (!ObjectUtils.allNotNull(_status)) {
                return false;
            }
            return _status == INVALID.getValue() || _status == APPROVED.getValue() || isUncall(_status) || isCallback(_status) || _status == REJECTED.getValue();
        }

        public int getValue() {
            return status;
        }
    }

    public enum CALLING_LIST_TYPE{
        REALTIME("1"),
        MANUAL("2"),
        RESERVER("3");

        private final String status;
        CALLING_LIST_TYPE(String status){
            this.status = status;
        }

        public String getValue(){ return status;}
    }

    public enum AGENT_MONITOR_STATUS {
        ONCALL(1),
        AVAIABLE(2),
        OFFLINE(3);

        private final int status;

        AGENT_MONITOR_STATUS(int status) {
            this.status = status;
        }

        public int getValue() {
            return status;
        }
    }

    public enum CAMPAIGN_CONFIG_TYPE {
        STRATEGY(81),
        RULE(82),
        AGENTGROUP(85),
        CLGROUP(86),;
        private final int status;

        CAMPAIGN_CONFIG_TYPE(int status) {
            this.status = status;
        }

        public int getValue() {
            return status;
        }
    }

    public enum CAMPAIGN_STATUS_ID {
        NEW(36),
        RUNNING(31),
        STOPPED(32),
        PAUSED(33),
        STOPPING(34),
        DELETED(35),;
        private final int status;

        CAMPAIGN_STATUS_ID(int status) {
            this.status = status;
        }

        public int getValue() {
            return status;
        }
    }

    public enum CAMPAIGN_STATUS_NAME {
        STOP("stopped"),
        WAITING("pause"),
        START("start"),;
        private final String status;

        CAMPAIGN_STATUS_NAME(String status) {
            this.status = status;
        }

        public String getValue() {
            return status;
        }
    }


    public enum GHN {
        GHN_HOST("apiv3-test.ghn.vn"),
        GHN_CREATE_DO("/api/v1/apiv3/CreateOrder"),
        GHN_UPDATE_DO("/api/v1/apiv3/UpdateOrder"),
        GHN_CANCEL_DO("/api/v1/apiv3/CancelOrder"),;
        private final String ghn;

        GHN(String ghn) {
            this.ghn = ghn;
        }

        public String getValue() {
            return ghn;
        }
    }

    public enum DO_STATUS_ID {
        NEW(51),
        READY_TO_PICK(52),
        CANCEL(53),
        PICKING(54),
        IN_TRANSIT(55),
        RETURN(56),//returning
        REJECT(57),
        IN_PREPARATION(58),
        DELIVERED(59),
        PAID(60),
        RETURNED(61),
        FAILED(62),//delivering
        PICKED_UP(63),
        PENDING(65);
        private final int status;

        DO_STATUS_ID(int status) {
            this.status = status;
        }

        public int getValue() {
            return status;
        }
    }

    public enum DO_STATUS_NAME {
        STATUS_PENDING_TEXT("pending"),
        STATUS_ACTIVATED_TEXT("activated");
        private final String status;

        DO_STATUS_NAME(String status) {
            this.status = status;
        }

        public String getValue() {
            return status;
        }
    }

    public enum CALLBACK_PHONE_TYPE {
        AVAIABLE(0),
        NEW(1),;
        private final int status;

        CALLBACK_PHONE_TYPE(int status) {
            this.status = status;
        }

        public int getValue() {
            return status;
        }
    }



    public enum PAYMENT_METHOD {
        COD(1),
        BANKING(2),
        DEPOSIT(3),
        ESPAY(4),
        _2C2P(5);
        private final int status;

        PAYMENT_METHOD(int status) {
            this.status = status;
        }

        public int getValue() {
            return status;
        }
    }

    public enum DISTRIBUTION_RULE {
        LIFO(1),
        FIFO(2),
        MIX(3),
        RATE(4),
        RUBY(5),
        LIFO2(6),
        LIFO_V2(7),
        LIFO2_V2(8),
        AGENT_RATE_3_ID(9),
        CIT_UN_CALL(10),
        UNCALL_ME(11),
        ;
        private final int status;

        DISTRIBUTION_RULE(int status) {
            this.status = status;
        }

        public int getValue() {
            return status;
        }
    }

    public enum SALE_ORDER_STATUS {
        NEW(41),
        PENDING(42),
        VALIDATED(43),
        CANCEL(44),
        SUCCESS(45),
        UNASSIGNED(46),
        DELAYVALIDATE(357),
        DOCREATEFAIL(48);
        private final int status;

        SALE_ORDER_STATUS(int status) {
            this.status = status;
        }

        public int getValue() {
            return status;
        }
    }

    public enum AGENTCY_TYPE {
        CPC(1),
        CPI(2),
        CPL(3),
        CPO(4),
        CPS(5),
        NoPostBack(6);

        private final int value;

        AGENTCY_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum PRODUCT_TYPE {
        NORMAL(1),
        COMBO(2),
       ;

        private final int value;

        PRODUCT_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum CARRIER_ID {
        DHL(4),
        KERRY (20),
        SAP(21),
        ALPHA(57),
        FLASH_TH(150);

        private final int status;

        CARRIER_ID(int status) {
            this.status = status;
        }

        public int getValue() {
            return status;
        }
    }

    public enum FFM_ID {
        DHL(28),
        KERRY (26),
        SAP(27),
        MYCLOUD(56);

        private final int status;

        FFM_ID(int status) {
            this.status = status;
        }

        public int getValue() {
            return status;
        }
    }

    public enum REDIS_LEAD_PROPERTIES {
        ID("leadId");
        private final String name;

        REDIS_LEAD_PROPERTIES(String name) {
            this.name = name;
        }

        public String getValue() {
            return name;
        }
    }

    public enum DHL_LABEL_TYPE {
        URL("U"), BASE64("Y"), NOBASE64("N");
        private final String name;

        DHL_LABEL_TYPE(String name) {
            this.name = name;
        }

        public String getValue() {
            return name;
        }
    }

    public enum LEAD_TYPE {
        NEW("NEW"), UNCALL("UNCALL");
        private final String type;

        LEAD_TYPE(String name) {
            this.type = name;
        }

        public String getType() {
            return type;
        }
    }

    public enum CAMPAIGN_TH {
        FRESH_BEAUTY(576),
        FRESH_SLIMMING(568),
        FRESH_AMULET(580),
        FRESH_HEARING(581),
        FRESH_HC(584),
        FRESH_HAIR(597),
        RESELL_BEAUTY(575),
        FRESH_EVERLIFT(600),
        FRESH_LAVITE(601),
        RESELL_BEAUTY_AR(611),
        RESELL_BEAUTY_RJ(612),
        RESELL_BEAUTY_ETC(613),
        RESELL_SLIMMING(566),
        RESELL_HEARING(578);
        private final int cpId;

        CAMPAIGN_TH(int cpId) {
            this.cpId = cpId;
        }

        public int getCpId() {
            return cpId;
        }

    }

    public enum CAMPAIGN_TYPE {
        FRESH(1),
        RESELL(2),
        DIGITAL(3);
        private final int cpType;

        CAMPAIGN_TYPE(int cpType) {
            this.cpType = cpType;
        }

        public int getCpType() {
            return cpType;
        }
    }

    public enum BOOLEAN_INTEGER {
        TRUE(1),
        FALSE(0);

        private final int value;

        BOOLEAN_INTEGER(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum USER_LOCK {
        LOCK(1),
        NONE(0);

        private final int value;

        USER_LOCK(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum USER_TYPE {
        ACCOUNTANT("accountant"),
        ADMIN("admin"),
        AGENT("agent"),
        AGENT_MKT("agent mkt"),
        CUSTOMER_SERVICE("customer_service"),
        DIRECTOR("director"),
        MANAGEMENT("management"),
        MANIPULATE("manipulate"),
        LASTMILE("lastmile"),
        LOGISTIC("logistic"),
        TEAM_LEADER("team_leader"),
        TL_EFA("tl_efa"),
        VALIDATION("validation");
        private final String value;

        USER_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum GENDER {
        FEMALE(0, "Female"),
        MALE(1, "Male"),
        ETC(2, "Etc");

        private final int value;
        private final String name;

        GENDER(int value, String name) {
            this.value = value;
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public String getName() {
            return name;
        }
    }

    public enum CRM_ACTION_TYPE {
        NEW_LEAD(0),
        GET_LEAD(1),
        ASSIGN_LEAD(2),
        UPDATE_LEAD(3),
        SO_NEW(4),
        ;
        private final int value;

        CRM_ACTION_TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum AGENT_STATE {
        LOGIN(1, "Login"),
        LOGOUT(2, "Logout"),
        ASSIGNED(3, "Assigned"),
        BUSY(4, "Busy"),
        BREAK(5, "Break"),
        ON_CALL(6, "On call"),
        WRAP_UP(7, "Wrap up"),
        REGISTERED(9, "Registered"),
        AVAILABLE(10, "Available"),
        UNAVAILABLE(8, "Unavailable"),
        REQUESTED_BREAK(11, "Requested break"),
        REQUESTED_BUSY(12, "Requested busy"),
        REQUESTED_UNAVAILABLE(13, "Requested unavailable"),
        END(14, "End"),
        RINGING(15, "Ringing"),
        CONNECTED(16, "Connected"),
        OUTBOUND(17, "Outbound"),
        TOKEN(18, "token");

        private final Integer value;
        private final String name;

        AGENT_STATE(Integer value, String name) {
            this.value = value;
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }
        public String getName() {
            return name;
        }
    }
}
