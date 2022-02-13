package com.tms.api.dto;

import javax.validation.constraints.NotNull;
import com.tms.entity.User;

import java.util.List;

public class AgentInfoDto {
    List<Integer> roleId;
	User info;
	ThemeDTO theme;


	public List<Integer> getRoleId() {
		return roleId;
	}

	public void setRoleId(List<Integer> roleId) {
		this.roleId = roleId;
	}

	public User getInfo() {
		return info;
	}

	public void setInfo(User info) {
		this.info = info;
	}

	public ThemeDTO getTheme() {
		return theme;
	}

	public void setTheme(ThemeDTO theme) {
		this.theme = theme;
	}
}