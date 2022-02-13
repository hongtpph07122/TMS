package com.oauthcentralization.app.tmsoauth2.model.filters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oauthcentralization.app.tmsoauth2.model.dto.PageRequestDTO;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RolesFilter extends PageRequestDTO {

    private List<Integer> listId;
    private String name;
    private String label;

    public RolesFilter() {
        super();
    }

    public List<Integer> getListId() {
        return listId;
    }

    public void setListId(List<Integer> listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
