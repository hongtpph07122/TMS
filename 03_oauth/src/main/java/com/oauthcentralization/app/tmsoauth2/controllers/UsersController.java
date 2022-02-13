package com.oauthcentralization.app.tmsoauth2.controllers;

import com.oauthcentralization.app.tmsoauth2.controllers.base.BaseController;
import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSDocsResponse;
import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.dto.HttpStatusCodesResponseDTO;
import com.oauthcentralization.app.tmsoauth2.model.filters.UsersFilter;
import com.oauthcentralization.app.tmsoauth2.model.request.GroupAttributesRequest;
import com.oauthcentralization.app.tmsoauth2.model.request.TeamAttributesRequest;
import com.oauthcentralization.app.tmsoauth2.model.request.UsersRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.service.UsersBaseService;
import com.oauthcentralization.app.tmsoauth2.service.UsersPoliciesService;
import com.oauthcentralization.app.tmsoauth2.service.UsersService;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

import static com.oauthcentralization.app.tmsoauth2.variables.RoutesVariable.ROUTE_USERS_API_URL;

@RestController
@RequestMapping(value = ROUTE_USERS_API_URL)
public class UsersController extends BaseController {

    private final UsersService usersService;
    private final UsersBaseService usersBaseService;
    private final UsersPoliciesService usersPoliciesService;

    @Autowired
    public UsersController(
            UsersService usersService,
            UsersBaseService usersBaseService,
            UsersPoliciesService usersPoliciesService) {
        this.usersService = usersService;
        this.usersBaseService = usersBaseService;
        this.usersPoliciesService = usersPoliciesService;
    }

    @GetMapping("/types")
    public @ResponseBody
    ResponseEntity<?> findAllUsersType() {
        return new ResponseEntity<>(TMSResponse.buildTMSResponse(usersPoliciesService.findAllUsersType(this.myUserDetails)), HttpStatus.OK);
    }

    @GetMapping("/{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> findAll(@PathVariable("page") int page,
                              @PathVariable("size") int size) {
        UsersFilter usersFilter = new UsersFilter();
        usersFilter.setPageIndex(page);
        usersFilter.setPageSize(size);
        return new ResponseEntity<>(TMSResponse.buildTMSResponse(usersPoliciesService.findUsersByPolicies(usersFilter, this.myUserDetails)), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public @ResponseBody
    ResponseEntity<?> findOneBy(@PathVariable("userId") Integer userId) {
        UsersFilter usersFilter = new UsersFilter();
        usersFilter.setPageIndex(1);
        usersFilter.setPageSize(1);
        usersFilter.setUsersId(Collections.singletonList(userId));
        return new ResponseEntity<>(TMSResponse.buildTMSResponse(usersBaseService.findUsersBy(usersFilter)), HttpStatus.OK);
    }

    @PostMapping("/filter/{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> filterBy(@PathVariable("page") int page,
                               @PathVariable("size") int size,
                               @RequestBody UsersFilter usersFilter) {
        usersFilter.setPageIndex(page);
        usersFilter.setPageSize(size);
        return new ResponseEntity<>(TMSResponse.buildTMSResponse(usersPoliciesService.filterUsersByPolicies(usersFilter, this.myUserDetails)), HttpStatus.OK);
    }

    @PostMapping("/new")
    public @ResponseBody
    ResponseEntity<?> createOne(@RequestBody UsersRequest usersRequest) {
        return new ResponseEntity<>(usersService.saveAsPayloads(usersRequest, this.myUserDetails), HttpStatus.OK);
    }

    @PutMapping("/update/{userId}")
    public @ResponseBody
    ResponseEntity<?> updateOne(@PathVariable("userId") Integer userId, @RequestBody UsersResponse usersRequest) {
        return new ResponseEntity<>(usersService.updateAsPayloads(userId, usersRequest, this.myUserDetails), HttpStatus.OK);
    }

    @PostMapping("/group/remove")
    public @ResponseBody
    ResponseEntity<?> removeGroup(@RequestBody GroupAttributesRequest groupAttributesRequest) {

        if (!ObjectUtils.allNotNull(groupAttributesRequest.getGroupId(), groupAttributesRequest.getUserId(), groupAttributesRequest.getGroupAssigneeId())) {
            return new ResponseEntity<>(TMSResponse.buildTMSResponse("userId, groupId, groupMemberId is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST), HttpStatus.OK);
        }
        return new ResponseEntity<>(usersBaseService.excludeGroupMembers(groupAttributesRequest, this.myUserDetails), HttpStatus.OK);
    }

    @PostMapping("/group/move")
    public @ResponseBody
    ResponseEntity<?> moveGroup(@RequestBody GroupAttributesRequest groupAttributesRequest) {

        if (!ObjectUtils.allNotNull(groupAttributesRequest.getGroupId(), groupAttributesRequest.getUserId(), groupAttributesRequest.getGroupAssigneeId())) {
            return new ResponseEntity<>(TMSResponse.buildTMSResponse("userId, groupId, groupMemberId is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST), HttpStatus.OK);
        }
        return new ResponseEntity<>(usersBaseService.moveGroupMembers(groupAttributesRequest, this.myUserDetails), HttpStatus.OK);
    }


    @PostMapping("/team/remove")
    public @ResponseBody
    ResponseEntity<?> removeTeam(@RequestBody TeamAttributesRequest teamAttributesRequest) {

        if (!ObjectUtils.allNotNull(teamAttributesRequest.getUserId(), teamAttributesRequest.getTeamId(), teamAttributesRequest.getTeamMemberId())) {
            return new ResponseEntity<>(TMSResponse.buildTMSResponse("userId, teamId, teamMemberId is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST), HttpStatus.OK);
        }
        return new ResponseEntity<>(usersBaseService.excludeTeamMembers(teamAttributesRequest, this.myUserDetails), HttpStatus.OK);
    }

    @PostMapping("/team/move")
    public @ResponseBody
    ResponseEntity<?> moveTeam(@RequestBody TeamAttributesRequest teamAttributesRequest) {

        if (!ObjectUtils.allNotNull(teamAttributesRequest.getUserId(), teamAttributesRequest.getTeamId(), teamAttributesRequest.getTeamMemberId())) {
            return new ResponseEntity<>(TMSResponse.buildTMSResponse("userId, teamId, teamMemberId is required", null, HttpStatusCodesResponseDTO.BAD_REQUEST), HttpStatus.OK);
        }
        return new ResponseEntity<>(usersBaseService.moveTeamMembers(teamAttributesRequest, this.myUserDetails), HttpStatus.OK);
    }

    @PostMapping("/upload-users")
    public @ResponseBody
    ResponseEntity<?> uploadUsers(@RequestParam("file") MultipartFile file,
                                  @RequestParam(value = "sheetName", defaultValue = "Sheet1", required = false) String sheetName,
                                  @RequestParam(value = "filename", defaultValue = "Users_Failed.xlsx", required = false) String filename) {

        TMSResponse<?> response = usersBaseService.importUsers(sheetName, file, this.myUserDetails);

        if (response.getHeader().equals(HttpStatusCodesResponseDTO.CREATED)) {
            return TMSDocsResponse.buildScrapeDocsResponse((byte[]) response.getData(), filename, ".xlsx");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
