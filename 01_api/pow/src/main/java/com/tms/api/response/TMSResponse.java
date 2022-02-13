package com.tms.api.response;

import com.tms.api.exception.ErrorMessage;
import com.tms.api.exception.TMSException;
import com.tms.dto.DBResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by dinhanhthai on 21/04/2019.
 */

@SuppressWarnings("rawtypes")
public class TMSResponse<T> {
    private int code;
    private String message;
    private T data;
    private int total;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public static TMSResponse buildResponse(Object data) {
        TMSResponse res = new TMSResponse();
        res.data = data;
        res.code = 200;
        return res;
    }

    public static TMSResponse<String> buildResponse(ErrorMessage errorMessage) {
        TMSResponse<String> res = new TMSResponse<>();
        res.code = errorMessage.getCode();
        res.message = errorMessage.getMessage();
        return res;
    }

    public static <T> TMSResponse<T> buildResponse(T data, ErrorMessage errorMessage) {
        TMSResponse<T> res = new TMSResponse<>();
        res.data = data;
        res.code = errorMessage.getCode();
        res.message = errorMessage.getMessage();
        return res;
    }

    @SuppressWarnings("rawtypes")
    public static TMSResponse buildResponse(Object data, Integer total) {
        TMSResponse res = buildResponse(data);
        res.total = total;
        return res;
    }

    public static TMSResponse buildResponse(Object data, Integer total, String message, Integer errCode) {
        TMSResponse res = buildResponse(data, total);
        res.code = errCode;
        res.setMessage(message);
        return res;
    }

    public static TMSResponse buildResponse(DBResponse dbResponse) {
        TMSResponse res = new TMSResponse();
        res.data = dbResponse.getResult();
        res.code = (dbResponse.getErrorCode() == 0 ? 200 : dbResponse.getErrorCode());
        res.message = dbResponse.getErrorMsg();
        return res;
    }
    public static TMSResponse buildApplicationException(TMSException tmsEx) {
        TMSResponse res = new TMSResponse();
        res.code = tmsEx.getErrorMessage().getCode();
        res.message = tmsEx.getErrorMessage().getMessage();
        return res;
    }

    public static TMSResponse buildApplicationException(String tmsEx, int code) {
        TMSResponse res = new TMSResponse();
        res.code = code;
        res.message = tmsEx;
        return res;
    }
    public static ResponseEntity buildExcelFileResponse(byte[] data, String fileName) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("content-disposition", "attachment; filename=" + fileName);
        responseHeaders.add("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        ResponseEntity respEntity = new ResponseEntity(data, responseHeaders, HttpStatus.OK);
        return respEntity;
    }

    public static ResponseEntity buildDeninedPermission() {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json");
        ResponseEntity respEntity = new ResponseEntity(
                "You don't have permision to run this function, please contact Administrator!", responseHeaders,
                HttpStatus.BAD_REQUEST);
        return respEntity;
    }
}
