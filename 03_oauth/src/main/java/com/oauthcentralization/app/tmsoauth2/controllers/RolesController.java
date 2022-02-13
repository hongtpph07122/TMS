package com.oauthcentralization.app.tmsoauth2.controllers;

import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.filters.RolesFilter;
import com.oauthcentralization.app.tmsoauth2.service.RolesBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static com.oauthcentralization.app.tmsoauth2.variables.RoutesVariable.ROUTE_ROLES_API_URL;

@RestController
@RequestMapping(value = ROUTE_ROLES_API_URL)
public class RolesController {

    private final RolesBaseService rolesBaseService;

    @Autowired
    public RolesController(
            RolesBaseService rolesBaseService) {
        this.rolesBaseService = rolesBaseService;
    }

    @GetMapping("{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> findAll(@PathVariable("page") int page,
                              @PathVariable("size") int size) {
        RolesFilter rolesFilter = new RolesFilter();
        rolesFilter.setPageIndex(page);
        rolesFilter.setPageSize(size);

        return new ResponseEntity<>(TMSResponse.buildTMSResponse(rolesBaseService.findRolesBy(rolesFilter)), HttpStatus.OK);
    }

    @PostMapping("filter/{page}/{size}")
    public @ResponseBody
    ResponseEntity<?> filterRoles(@PathVariable("page") int page,
                                  @PathVariable("size") int size,
                                  @RequestBody RolesFilter rolesFilter) {
        rolesFilter.setPageIndex(page);
        rolesFilter.setPageSize(size);

        return new ResponseEntity<>(TMSResponse.buildTMSResponse(rolesBaseService.findRolesBy(rolesFilter)), HttpStatus.OK);
    }

    @GetMapping
    public @ResponseBody
    ResponseEntity<?> findOne(@RequestParam("roleId") int roleId) {
        RolesFilter rolesFilter = new RolesFilter();
        rolesFilter.setPageIndex(1);
        rolesFilter.setPageSize(1);
        rolesFilter.setListId(Collections.singletonList(roleId));

        return new ResponseEntity<>(TMSResponse.buildTMSResponse(rolesBaseService.findRolesBy(rolesFilter)), HttpStatus.OK);
    }
}
