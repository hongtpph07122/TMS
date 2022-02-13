package com.oauthcentralization.app.tmsoauth2.controllers;

import com.oauthcentralization.app.tmsoauth2.controllers.base.BaseController;
import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.filters.GroupAssigneesFilter;
import com.oauthcentralization.app.tmsoauth2.model.filters.GroupsFilter;
import com.oauthcentralization.app.tmsoauth2.model.request.GroupsMemberRequest;
import com.oauthcentralization.app.tmsoauth2.service.GroupPoliciesService;
import com.oauthcentralization.app.tmsoauth2.service.GroupsBaseService;
import com.oauthcentralization.app.tmsoauth2.service.GroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.oauthcentralization.app.tmsoauth2.variables.RoutesVariable.ROUTE_GROUPS_API_URL;

@RestController
@RequestMapping(value = ROUTE_GROUPS_API_URL)
public class GroupsController extends BaseController {

    private final GroupsBaseService groupsBaseService;
    private final GroupsService groupsService;
    private final GroupPoliciesService groupPoliciesService;

    @Autowired
    public GroupsController(
            GroupsBaseService groupsBaseService,
            GroupsService groupsService,
            GroupPoliciesService groupPoliciesService) {
        this.groupsBaseService = groupsBaseService;
        this.groupsService = groupsService;
        this.groupPoliciesService = groupPoliciesService;
    }

    @GetMapping("{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> findAll(@PathVariable("page") int page,
                              @PathVariable("size") int size) {
        GroupsFilter groupsFilter = new GroupsFilter();
        groupsFilter.setPageIndex(page);
        groupsFilter.setPageSize(size);
        return new ResponseEntity<>(TMSResponse.buildTMSResponse(groupPoliciesService.findGroupsBy(groupsFilter, this.myUserDetails)), HttpStatus.OK);
    }

    @PostMapping("{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> filterGroups(@PathVariable("page") int page,
                                   @PathVariable("size") int size,
                                   @RequestBody GroupsFilter groupsFilter) {
        groupsFilter.setPageIndex(page);
        groupsFilter.setPageSize(size);
        return new ResponseEntity<>(TMSResponse.buildTMSResponse(groupPoliciesService.findGroupsBy(groupsFilter, this.myUserDetails)), HttpStatus.OK);
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<?> findOne(@RequestParam("groupId") Integer groupId) {
        GroupsFilter groupsFilter = new GroupsFilter();
        groupsFilter.setPageIndex(1);
        groupsFilter.setPageSize(1);
        groupsFilter.setListId(Collections.singletonList(groupId));
        return new ResponseEntity<>(TMSResponse.buildTMSResponse(groupsBaseService.findGroupsBy(groupsFilter)), HttpStatus.OK);
    }

    @PostMapping("/assignee/{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> findGroupAssignee(@PathVariable("page") int page,
                                        @PathVariable("size") int size,
                                        @RequestBody GroupAssigneesFilter groupAssigneesFilter) {
        groupAssigneesFilter.setPageIndex(page);
        groupAssigneesFilter.setPageSize(size);

        return new ResponseEntity<>(TMSResponse.buildTMSResponse(groupsBaseService.findGroupAssigneesBy(groupAssigneesFilter)), HttpStatus.OK);
    }

    @GetMapping("/assignee/{groupId}/{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> findGroupAssignee(@PathVariable("groupId") int groupId,
                                        @PathVariable("page") int page,
                                        @PathVariable("size") int size
    ) {
        GroupAssigneesFilter groupAssigneesFilter = new GroupAssigneesFilter();
        groupAssigneesFilter.setPageIndex(page);
        groupAssigneesFilter.setPageSize(size);
        groupAssigneesFilter.setGroupsId(Collections.singletonList(groupId));
        return new ResponseEntity<>(TMSResponse.buildTMSResponse(groupsBaseService.findGroupAssigneesBy(groupAssigneesFilter)), HttpStatus.OK);
    }

    @PostMapping("/add-members")
    public @ResponseBody
    ResponseEntity<?> addMembers(@RequestBody GroupsMemberRequest groupsMemberRequest) {
        return new ResponseEntity<>(groupsService.addMembers(groupsMemberRequest, this.myUserDetails), HttpStatus.OK);
    }
}
