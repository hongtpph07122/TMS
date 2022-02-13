package com.tms.api.helper;

import com.tms.api.dto.UpdateCampaignConfigDto;
import com.tms.dto.CPCampaignResp;
import com.tms.dto.DBResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.beans.FeatureDescriptor;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by dinhanhthai on 21/04/2019.
 */
public class Helper {
    private static final Logger logger = LoggerFactory.getLogger(Helper.class);
    private static final String REGEX_NUMBER = "^-?[0-9][0-9,\\.]+$";
    public static String getUserName() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        OAuth2Authentication principal = (OAuth2Authentication) request.getUserPrincipal();
        return principal.getName();
    }

    public static boolean isAgencyNeedHold(Integer agencyId) {
        if (agencyId != null)
            switch (agencyId) {
                case Const.AGENCY_MOSIAC:
                case Const.AGENCY_CLICK_DEAL:
                case Const.AGENCY_MAXXI:
                case Const.AGENCY_TMA:
                case Const.AGENCY_AFSU:
                    return true;
            }
        return false;
    }

    public static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static boolean isNullOrBlank(String param) {
        return param == null || param.trim().length() == 0;
    }

    /**
     * returns a view (not a new list) of the sourceList for the
     * range based on page and pageSize
     *
     * @param sourceList
     * @param page,      page number should start from 1
     * @param pageSize
     * @return
     */
    public static <T> List<T> getPage(List<T> sourceList, Integer curPage, Integer curPageSize) {
        int page = (curPage != null ? curPage : 1);
        int pageSize = (curPageSize != null ? curPageSize : Const.DEFAULT_PAGE_SIZE);

        if (pageSize <= 0 || page <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }

        int fromIndex = (page - 1) * pageSize;
        if (sourceList == null || sourceList.size() < fromIndex) {
            return Collections.emptyList();
        }

        // toIndex exclusive
        return sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size()));
    }

    public static Boolean isIndonesia(String countryCode) {
        return countryCode.equals("ID");
    }

    public static Boolean isThailand(String countryCode) {
        return countryCode.equals("TH");
    }


    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        return Stream.of(src.getPropertyDescriptors()).map(FeatureDescriptor::getName)
                .filter(propertyName -> src.getPropertyValue(propertyName) == null).toArray(String[]::new);
    }

    public static int RoundToHundred(Double val) {
        double result = 0;
        result = Math.round(val / 100) * 100;
        return (int) result;
    }

    public static String getSessionId() {
        return RequestContextHolder.currentRequestAttributes().getSessionId();
    }

    public static UpdateCampaignConfigDto getCampaignConfig(List<CPCampaignResp> cpConfigs) {
        UpdateCampaignConfigDto campaignConfigDto = new UpdateCampaignConfigDto();
        if (cpConfigs.isEmpty()) {
            return campaignConfigDto;
        }

        for (CPCampaignResp cpConfig : cpConfigs) {
            if (cpConfig.getCpId() != null) {
                campaignConfigDto.setCampaignId(cpConfig.getCpId());
            }

            if (cpConfig.getCallStrategy() != null) {
                campaignConfigDto.setStrategyId(cpConfig.getCallStrategy());
            }

            if (cpConfig.getDistributionRule() != null) {
                campaignConfigDto.setRuleId(cpConfig.getDistributionRule());
            }

            if (cpConfig.getGroupAgent() != null && !campaignConfigDto.getAgentGroupIdList().contains(cpConfig.getGroupAgent())) {
                campaignConfigDto.getAgentGroupIdList().add(cpConfig.getGroupAgent());
            }

            if (cpConfig.getCallingList() != null && !campaignConfigDto.getCallingList().contains(cpConfig.getCallingList())) {
                campaignConfigDto.getCallingList().add(cpConfig.getCallingList());
            }
        }

        return campaignConfigDto;
    }

    public static boolean isDBSuccess(DBResponse dbResponse) {
        return (dbResponse != null && dbResponse.getErrorCode() == Const.INS_DB_SUCCESS);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == "")
            return false;
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isNullOrEmpty(String str) {
        return !(str != null && !str.trim().isEmpty());
    }

    public static String toString(Integer input) {return input != null ? input.toString() : null;}

    public static String toStringEmpty(String input) {
        return (input != null ? input.trim() : "");
    }

    public static String toStringEmpty(Integer input) {
        return (input != null ? input.toString() : "");
    }

    public static boolean isCallback(int status) {
        return (status == EnumType.LEAD_STATUS.CALLBACK_CONSULTING.getValue()) || (status == EnumType.LEAD_STATUS.CALLBACK_NOT_PROPECT.getValue() || (status == EnumType.LEAD_STATUS.CALLBACK_POTENTIAL.getValue()));
    }

    public static String convertToTMSDate(String inDate, String format) {
        String[] arrStr = inDate.split("/");
        return String.format("%s%s%s", arrStr[2], arrStr[0], arrStr[1]);
    }

    public static boolean isValidDate(String inDate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public static int IntergeTryParse(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return defaultVal;
        }
    }

    public static int IntegerTryParse(String value, int defaultVal) {
        if (!StringUtils.hasText(value) || !Pattern.matches(REGEX_NUMBER, value))
            return defaultVal;
        return Integer.parseInt(value);
    }

    public static String getKeyConfigByOrg(Integer orgId, String key) {
        switch (orgId) {
            case 4://kiwi
                key = key;
                break;
            case 6://nori
                key = "nori." + key;
                break;
            case 9://ekiwi ID
                key = "kiwiID." + key;
                break;
            default:
        }
        return key;
    }

    public static boolean isTeamLeader(com.tms.entity.User _curUser) {
        return (_curUser.getUserType().equals(Const.USER_TEAMLEADER_TYPE));
    }

    public static boolean isTeamLeaderOther(com.tms.entity.User _curUser) {
        return (_curUser.getUserType().equals(Const.USER_TEAMLEADER_OTHER));
    }

    public static boolean isManager(com.tms.entity.User _curUser) {
        return (_curUser.getUserType().equals(Const.USER_MANAGER_TYPE));
    }

    public static boolean isAdmin(com.tms.entity.User _curUser) {
        return (_curUser.getUserType().equals(Const.USER_ADMIN_TYPE));
    }

    public static boolean isAgent(com.tms.entity.User _curUser) {
        return (_curUser.getUserType().equals(Const.USER_AGENT_TYPE));
    }

    public static boolean isDirector(com.tms.entity.User _curUser){
        return Const.USER_DIRECTOR_TYPE.equals(_curUser.getUserType());
    }

    public static boolean isValidator(com.tms.entity.User _curUser){
        return Const.USER_VALIDATION_TYPE.equals(_curUser.getUserType());
    }

    public static boolean hasExportPermission(com.tms.entity.User user) {
        return !user.getUserName().equals("teamlead5@ekiwi.com");

    }

    public static boolean isPhoneValid(String phone, String country) {
        String regex = "^\\+?\\d{1,3}?[- .]?\\(?(?:\\d{2,3})\\)?[- .]?\\d\\d\\d[- .]?\\d\\d\\d$";
        if (Helper.isIndonesia(country)) {
            regex = "\\+?([ -]?\\d+)+|\\(\\d+\\)([ -]\\d+)";
        }

        if (phone.trim().length() < 9)
            return false;
        if (phone.startsWith("0") && phone.length() < 10)
            return false;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean isPhoneValid(String country, String[] phones) {
        for (int i = 0; i < phones.length; i++) {
            if (isPhoneValid(phones[i], country))
                return true;
        }
        return false;
    }

    public static String createRedisKey(String prefix, String key) {
        return String.format("%s:%s", prefix, key);
    }

    public static String createRedisPrefix(String prefix, int orgId) {
        return String.format("%s:%s:", prefix, orgId);
    }

    public static long currentEpoch() {
        return (new java.util.Date()).getTime();
    }

    public static int IntergeTryParse(String value) {
        return IntergeTryParse(value, 0);
    }

    public static int IntegerTryParse(String value) {
        return IntegerTryParse(value, 0);
    }

    public static double formatNumber(double number) {
        return Math.round(number * 100.0) / 100.0;
    }

    public static double convertToPercent(double val1, double val2) {
        return Math.round((val1 / val2) * 100 * 100.0) / 100.0;
    }

    public static int AgencyType(Integer leadStatus) {
        switch (leadStatus) {
            case 3://Reject
            case 4://duplicate
            case 6://close
                return -1;
            case 5://invalid, trash
                return 2;
            case 13://pre-appove lead, need to sent as hold
            default:
                return 0;
        }
    }

    public static String formatPhoneNumber(String phone) {

        phone = phone.replaceAll(" ", "").replaceAll("\\.", "");

        StringBuilder phoneBuilder = new StringBuilder(phone);
        if (phone.startsWith("o") || phone.startsWith("O")) {
            phoneBuilder = new StringBuilder().append(0).append(phone.substring(1));
        } else if (phone.startsWith("+84"))
            phoneBuilder = new StringBuilder().append(0).append(phone.substring(3));
        else if (phone.length() == 9 && !phone.substring(0, 1).equals("0"))
            phoneBuilder = phoneBuilder.insert(0, "0");

        return phoneBuilder.toString();
    }

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params) {
            String[] p = param.split("=");
            String name = p[0];
            if (p.length > 1) {
                String value = p[1];
                map.put(name, value);
            }
        }
        return map;
    }

    public static String getQueryRequest(String url) {
        return url.substring(url.indexOf("?") + 1);
    }


    public static String getMktParam(String url, String param) {
        Map<String, String> params = getQueryMap(getQueryRequest(url));
        if (params.containsKey(param))
            return params.get(param);
        return null;
    }
}
