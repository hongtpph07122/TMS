package com.oauthcentralization.app.tmsoauth2.controllers;

import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSDocsResponse;
import com.oauthcentralization.app.tmsoauth2.model.filters.GroupAssigneesFilter;
import com.oauthcentralization.app.tmsoauth2.service.GroupsBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.oauthcentralization.app.tmsoauth2.variables.RoutesVariable.ROUTE_GROUPS_API_URL;

@RestController
@RequestMapping(value = ROUTE_GROUPS_API_URL)
public class GroupsConveyController {

    private final GroupsBaseService groupsBaseService;

    @Autowired
    public GroupsConveyController(
            GroupsBaseService groupsBaseService) {
        this.groupsBaseService = groupsBaseService;
    }

    @PostMapping("/members/{groupId}/export-excel")
    public @ResponseBody
    ResponseEntity<?> exportExcelEnabled(
            @PathVariable("groupId") int groupId,
            @RequestParam(value = "sheetName", defaultValue = "Sheet1", required = false) String sheetName,
            @RequestParam(value = "filename", defaultValue = "Group_Members_Data_Exported.xlsx", required = false) String filename,
            @RequestBody GroupAssigneesFilter groupAssigneesFilter) {
        groupAssigneesFilter.setGroupsId(Collections.singletonList(groupId));
        return TMSDocsResponse.buildScrapeDocsResponse(groupsBaseService.exportGroupsMemberToExcel(groupAssigneesFilter, sheetName), filename, ".xlsx");
    }
}
