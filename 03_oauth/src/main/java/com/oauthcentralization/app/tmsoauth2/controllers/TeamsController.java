package com.oauthcentralization.app.tmsoauth2.controllers;

import com.oauthcentralization.app.tmsoauth2.controllers.base.BaseController;
import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.filters.TeamMembersFilter;
import com.oauthcentralization.app.tmsoauth2.model.filters.TeamsFilter;
import com.oauthcentralization.app.tmsoauth2.model.request.TeamsMemberRequest;
import com.oauthcentralization.app.tmsoauth2.service.TeamPoliciesService;
import com.oauthcentralization.app.tmsoauth2.service.TeamsBaseService;
import com.oauthcentralization.app.tmsoauth2.service.TeamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.oauthcentralization.app.tmsoauth2.variables.RoutesVariable.ROUTE_TEAMS_API_URL;

@RestController
@RequestMapping(value = ROUTE_TEAMS_API_URL)
public class TeamsController extends BaseController {

    private final TeamsBaseService teamsBaseService;
    private final TeamsService teamsService;
    private final TeamPoliciesService teamPoliciesService;

    @Autowired
    public TeamsController(
            TeamsBaseService teamsBaseService,
            TeamsService teamsService,
            TeamPoliciesService teamPoliciesService) {
        this.teamsBaseService = teamsBaseService;
        this.teamsService = teamsService;
        this.teamPoliciesService = teamPoliciesService;
    }

    @GetMapping("{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> findAll(@PathVariable("page") int page,
                              @PathVariable("size") int size) {
        TeamsFilter teamsFilter = new TeamsFilter();
        teamsFilter.setPageIndex(page);
        teamsFilter.setPageSize(size);
        return new ResponseEntity<>(TMSResponse.buildTMSResponse(teamPoliciesService.findTeamsBy(teamsFilter, this.myUserDetails)), HttpStatus.OK);
    }

    @PostMapping("/filter/{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> filterTeams(@PathVariable("page") int page,
                                  @PathVariable("size") int size,
                                  @RequestBody TeamsFilter teamsFilter) {
        teamsFilter.setPageIndex(page);
        teamsFilter.setPageSize(size);
        return new ResponseEntity<>(TMSResponse.buildTMSResponse(teamsBaseService.findTeamsBy(teamsFilter)), HttpStatus.OK);
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<?> findOne(@RequestParam("teamId") Integer teamId) {
        TeamsFilter groupsFilter = new TeamsFilter();
        groupsFilter.setPageIndex(1);
        groupsFilter.setPageSize(1);
        groupsFilter.setListId(Collections.singletonList(teamId));
        return new ResponseEntity<>(TMSResponse.buildTMSResponse(teamsBaseService.findTeamsBy(groupsFilter)), HttpStatus.OK);
    }

    @PostMapping("/members/{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> findTeamMembers(@PathVariable("page") int page,
                                      @PathVariable("size") int size,
                                      @RequestBody TeamMembersFilter teamMembersFilter) {
        teamMembersFilter.setPageIndex(page);
        teamMembersFilter.setPageSize(size);

        return new ResponseEntity<>(TMSResponse.buildTMSResponse(teamsBaseService.findTeamMembersBy(teamMembersFilter)), HttpStatus.OK);
    }

    @GetMapping("/members/{teamId}/{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> findTeamMembers(@PathVariable("teamId") int teamId,
                                      @PathVariable("page") int page,
                                      @PathVariable("size") int size
    ) {
        TeamMembersFilter teamMembersFilter = new TeamMembersFilter();
        teamMembersFilter.setPageIndex(page);
        teamMembersFilter.setPageSize(size);
        teamMembersFilter.setTeamsId(Collections.singletonList(teamId));

        return new ResponseEntity<>(TMSResponse.buildTMSResponse(teamsBaseService.findTeamMembersBy(teamMembersFilter)), HttpStatus.OK);
    }

    @PostMapping("/add-members")
    public @ResponseBody
    ResponseEntity<?> addMembers(@RequestBody TeamsMemberRequest teamsMemberRequest) {
        return new ResponseEntity<>(teamsService.addMembers(teamsMemberRequest, this.myUserDetails), HttpStatus.OK);
    }
}
