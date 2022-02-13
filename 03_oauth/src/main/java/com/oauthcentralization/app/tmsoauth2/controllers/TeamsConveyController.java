package com.oauthcentralization.app.tmsoauth2.controllers;

import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSDocsResponse;
import com.oauthcentralization.app.tmsoauth2.model.filters.TeamMembersFilter;
import com.oauthcentralization.app.tmsoauth2.service.TeamsBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.oauthcentralization.app.tmsoauth2.variables.RoutesVariable.ROUTE_TEAMS_API_URL;

@RestController
@RequestMapping(value = ROUTE_TEAMS_API_URL)
public class TeamsConveyController {

    private final TeamsBaseService teamsBaseService;

    @Autowired
    public TeamsConveyController(
            TeamsBaseService teamsBaseService) {
        this.teamsBaseService = teamsBaseService;
    }

    @PostMapping("/members/{teamId}/export-excel")
    public @ResponseBody
    ResponseEntity<?> exportExcelEnabled(
            @PathVariable("teamId") int teamId,
            @RequestParam(value = "sheetName", defaultValue = "Sheet1", required = false) String sheetName,
            @RequestParam(value = "filename", defaultValue = "Team_Members_Data_Exported.xlsx", required = false) String filename,
            @RequestBody TeamMembersFilter teamMembersFilter) {
        teamMembersFilter.setTeamsId(Collections.singletonList(teamId));
        return TMSDocsResponse.buildScrapeDocsResponse(teamsBaseService.exportTeamsMembersToExcel(teamMembersFilter, sheetName), filename, ".xlsx");
    }
}
