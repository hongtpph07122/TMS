package com.oauthcentralization.app.tmsoauth2.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import com.oauthcentralization.app.tmsoauth2.variables.ConstVariable;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageRequestDTO {

    @JsonAlias({"page"})
    private int pageIndex;
    @JsonAlias({"size"})
    private int pageSize;
    @JsonIgnore
    private int pageOffset;
    private String keywords;
    private String[] sort;
    private String username; /* request by owner */
    private Integer userId;
    private Date topCreatedAt;
    private Date topModifiedAt;
    private Date bottomCreatedAt;
    private Date bottomModifiedAt;
    private Integer organizationId;
    private PageTimeRequestDTO pageTimeRequest;
    @JsonAlias("attributeOrderArchived")
    private boolean attributeOrderArchived;
    @JsonAlias("attributeOrderDeleted")
    private boolean attributeOrderDeleted;
    @JsonAlias("attributeOrderOther")
    private boolean attributeOrderOther;
    private UsersResponse user;
    @JsonAlias("deleted")
    private boolean isDeleted;
    @JsonAlias("archived")
    private boolean isArchived;
    @JsonIgnore
    private int limit;
    @JsonIgnore
    private int offset;

    public PageRequestDTO() {
        setPageIndex(1);
        setOrganizationId(null);
    }

    private int setPageSizeValid(int pageSize) {
        if (pageSize > 0) {
            return Math.min(pageSize, ConstVariable.PAGE_SIZE_DEFAULT);
        }
        return ConstVariable.PAGE_SIZE_OFFER;
    }

    private int setPageIndexValid(int pageIndex) {
        return pageIndex > 0 ? pageIndex - 1 : 0;
    }

    @JsonProperty("page")
    public int getPageIndex() {
        return this.pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = setPageIndexValid(pageIndex);
    }

    @JsonProperty("size")
    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = setPageSizeValid(pageSize);
    }

    public String getKeywords() {
        return this.keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String[] getSort() {
        return this.sort;
    }

    public void setSort(String[] sort) {
        this.sort = sort;
    }

    public PageTimeRequestDTO getPageTimeRequest() {
        return this.pageTimeRequest;
    }

    public void setPageTimeRequest(PageTimeRequestDTO pageTimeRequest) {
        this.pageTimeRequest = pageTimeRequest;
    }

    public boolean isAttributeOrderArchived() {
        return this.attributeOrderArchived;
    }

    public void setAttributeOrderArchived(boolean attributeOrderArchived) {
        this.attributeOrderArchived = attributeOrderArchived;
    }

    public boolean isAttributeOrderDeleted() {
        return this.attributeOrderDeleted;
    }

    public void setAttributeOrderDeleted(boolean attributeOrderDeleted) {
        this.attributeOrderDeleted = attributeOrderDeleted;
    }

    public boolean isAttributeOrderOther() {
        return this.attributeOrderOther;
    }

    public void setAttributeOrderOther(boolean attributeOrderOther) {
        this.attributeOrderOther = attributeOrderOther;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        setPageSize(limit);
        this.limit = getPageSize();
    }

    public int getOffset() {
        return this.offset;
    }

    public void setOffset(int offset) {
        setPageIndex(offset);
        this.offset = getPageIndex();
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UsersResponse getUser() {
        return this.user;
    }

    public void setUser(UsersResponse user) {
        this.user = user;
    }

    public Date getTopCreatedAt() {
        return this.topCreatedAt;
    }

    public void setTopCreatedAt(Date topCreatedAt) {
        this.topCreatedAt = topCreatedAt;
    }

    public Date getBottomCreatedAt() {
        return this.bottomCreatedAt;
    }

    public void setBottomCreatedAt(Date bottomCreatedAt) {
        this.bottomCreatedAt = bottomCreatedAt;
    }

    public Date getTopModifiedAt() {
        return this.topModifiedAt;
    }

    public void setTopModifiedAt(Date topModifiedAt) {
        this.topModifiedAt = topModifiedAt;
    }

    public Date getBottomModifiedAt() {
        return this.bottomModifiedAt;
    }

    public void setBottomModifiedAt(Date bottomModifiedAt) {
        this.bottomModifiedAt = bottomModifiedAt;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    public boolean isArchived() {
        return this.isArchived;
    }

    public void setArchived(boolean archived) {
        this.isArchived = archived;
    }

    public int getPageOffset() {
        if (ObjectUtils.allNotNull(this.pageIndex, this.pageSize)) {
            this.pageOffset = getPageIndex() * getPageSize();
        } else {
            this.pageOffset = 0;
        }
        return this.pageOffset;
    }

    public Integer getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }
}
