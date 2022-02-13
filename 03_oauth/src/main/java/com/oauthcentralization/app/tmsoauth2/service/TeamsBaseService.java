package com.oauthcentralization.app.tmsoauth2.service;

import com.oauthcentralization.app.tmsoauth2.model.filters.TeamMembersFilter;
import com.oauthcentralization.app.tmsoauth2.model.filters.TeamsFilter;
import com.oauthcentralization.app.tmsoauth2.model.response.TeamsResponse;

import java.util.List;

public interface TeamsBaseService {

    List<TeamsResponse> findTeamsBy(TeamsFilter teamsFilter);

    List<TeamsResponse> findTeamMembersBy(TeamMembersFilter teamMembersFilter);

    byte[] exportTeamsMembersToExcel(TeamMembersFilter teamMembersFilter, String sheetName);
}
