package com.oauthcentralization.app.tmsoauth2.model.abstracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.oauthcentralization.app.tmsoauth2.model.dto.HttpStatusCodesResponseDTO;
import com.oauthcentralization.app.tmsoauth2.model.dto.StatusCodeResponseDTO;
import com.oauthcentralization.app.tmsoauth2.utils.DateUtils;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;

import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TMSResponse<T> {

    private String message;
    private String publish;
    private StatusCodeResponseDTO header;
    @JsonIgnore
    private T data;
    private T items;
    private long total;

    public TMSResponse() {
        setPublish(DateUtils.feedStageAsString(new Date()));
        setTotal(0);
    }

    public static TMSResponse<?> buildTMSResponseMessage(String message) {
        TMSResponse<?> response = new TMSResponse<>();
        response.setMessage(message);
        return response;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static TMSResponse<?> buildTMSResponse(String message, Object data) {
        TMSResponse response = buildTMSResponseMessage(message);
        response.setHeader(HttpStatusCodesResponseDTO.OK);
        response.setData(data);
        response.setItems(data);
        if (data instanceof List) {
            response.setTotal(((List) data).size());
        } else {
            if (ObjectUtils.allNotNull(data)) {
                response.setTotal(1);
            } else {
                response.setTotal(0);
            }
        }
        return response;
    }

    @SuppressWarnings({"rawtypes"})
    public static TMSResponse<?> buildTMSResponse(String message, Object data, StatusCodeResponseDTO statusCode) {
        TMSResponse response = buildTMSResponse(message, data);
        response.setHeader(statusCode);
        return response;
    }

    @SuppressWarnings({"rawtypes"})
    public static TMSResponse<?> buildTMSResponse(String message, StatusCodeResponseDTO statusCode) {
        TMSResponse response = buildTMSResponseMessage(message);
        response.setHeader(statusCode);
        return response;
    }

    public static TMSResponse<?> buildTMSResponse(Object data) {
        return buildTMSResponse(null, data);
    }

    @SuppressWarnings({"rawtypes"})
    public static TMSResponse<?> buildTMSResponse(Object data, StatusCodeResponseDTO statusCode) {
        TMSResponse response = buildTMSResponse(null, data);
        response.setHeader(statusCode);
        return response;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPublish() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish = publish;
    }

    public StatusCodeResponseDTO getHeader() {
        return header;
    }

    public void setHeader(StatusCodeResponseDTO header) {
        this.header = header;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getItems() {
        return items;
    }

    public void setItems(T items) {
        this.items = items;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
