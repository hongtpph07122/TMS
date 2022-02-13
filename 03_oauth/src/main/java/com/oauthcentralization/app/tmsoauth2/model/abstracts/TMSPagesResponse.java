package com.oauthcentralization.app.tmsoauth2.model.abstracts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oauthcentralization.app.tmsoauth2.model.dto.HttpStatusCodesResponseDTO;
import com.oauthcentralization.app.tmsoauth2.model.dto.StatusCodeResponseDTO;
import com.oauthcentralization.app.tmsoauth2.utils.DateUtils;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TMSPagesResponse<T> {

    private String message;
    private String publish;
    private StatusCodeResponseDTO header;
    @JsonIgnore
    private T data;
    private T items;
    private long total;

    public TMSPagesResponse() {
        setPublish(DateUtils.feedStageAsString(new Date()));
        setTotal(0);
    }

    private static Map<String, Object> buildPageComponents(Page<?> pages) {
        Map<String, Object> dataCurrentPages = new HashMap<>();
        dataCurrentPages.put("pageContents", pages.getContent());
        dataCurrentPages.put("currentPage", pages.getNumber());
        dataCurrentPages.put("totalItems", pages.getTotalElements());
        dataCurrentPages.put("totalPages", pages.getTotalPages());
        dataCurrentPages.put("totalNumberOfElements", pages.getNumberOfElements());
        dataCurrentPages.put("isFirst", pages.isFirst());
        dataCurrentPages.put("isLast", pages.isLast());
        dataCurrentPages.put("hasNext", pages.hasNext());
        dataCurrentPages.put("hasPrevious", pages.hasPrevious());
        return dataCurrentPages;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static TMSPagesResponse<?> buildTMSResponse(Page<?> pages) {
        TMSPagesResponse response = new TMSPagesResponse<>();
        response.setHeader(HttpStatusCodesResponseDTO.OK);
        response.setData(buildPageComponents(pages));
        response.setItems(buildPageComponents(pages).get("pageContents"));
        response.setTotal(pages.getContent().size());
        return response;
    }

    @SuppressWarnings("rawtypes")
    public static TMSPagesResponse<?> buildTMSResponse(Page<?> pages, StatusCodeResponseDTO statusCode) {
        TMSPagesResponse response = buildTMSResponse(pages);
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


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public T getItems() {
        return items;
    }

    public void setItems(T items) {
        this.items = items;
    }

    public StatusCodeResponseDTO getHeader() {
        return header;
    }

    public void setHeader(StatusCodeResponseDTO header) {
        this.header = header;
    }
}
