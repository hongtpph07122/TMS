package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.model.enums.RoleType;
import com.oauthcentralization.app.tmsoauth2.model.filters.TeamMembersFilter;
import com.oauthcentralization.app.tmsoauth2.model.filters.TeamsFilter;
import com.oauthcentralization.app.tmsoauth2.model.response.TeamsResponse;
import com.oauthcentralization.app.tmsoauth2.service.CommonsService;
import com.oauthcentralization.app.tmsoauth2.service.TeamPoliciesService;
import com.oauthcentralization.app.tmsoauth2.service.TeamsBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@SuppressWarnings({"FieldCanBeLocal"})
@Service(value = "teamPoliciesService")
@Transactional
public class TeamPoliciesServiceImpl implements TeamPoliciesService {

    private static final Logger logger = LoggerFactory.getLogger(TeamPoliciesServiceImpl.class);

    private final TeamsBaseService teamsBaseService;
    private final CommonsService commonsService;

    @Autowired
    public TeamPoliciesServiceImpl(
            TeamsBaseService teamsBaseService,
            CommonsService commonsService) {
        this.teamsBaseService = teamsBaseService;
        this.commonsService = commonsService;
    }

    @Override
    public List<TeamsResponse> findTeamsBy(TeamsFilter teamsFilter, MyUserDetailsImpl myUserDetails) {

        if (commonsService.hasRole(myUserDetails, RoleType.ROLE_TEAM_LEADER)) {
            TeamMembersFilter teamMembersFilter = new TeamMembersFilter();
            teamMembersFilter.setUsersId(Collections.singletonList(myUserDetails.getUserId()));
            teamMembersFilter.setPageIndex(teamsFilter.getPageIndex());
            teamMembersFilter.setPageSize(teamsFilter.getPageSize());

            return teamsBaseService.findTeamMembersBy(teamMembersFilter);
        }
        return teamsBaseService.findTeamsBy(teamsFilter);
    }
}
