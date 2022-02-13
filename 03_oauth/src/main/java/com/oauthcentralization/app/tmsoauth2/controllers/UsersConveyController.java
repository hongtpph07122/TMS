package com.oauthcentralization.app.tmsoauth2.controllers;

import com.oauthcentralization.app.tmsoauth2.controllers.base.BaseController;
import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSDocsResponse;
import com.oauthcentralization.app.tmsoauth2.model.filters.UsersFilter;
import com.oauthcentralization.app.tmsoauth2.service.UsersPoliciesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.oauthcentralization.app.tmsoauth2.variables.RoutesVariable.ROUTE_USERS_API_URL;

@RestController
@RequestMapping(value = ROUTE_USERS_API_URL)
public class UsersConveyController extends BaseController {

    private final UsersPoliciesService usersPoliciesService;

    @Autowired
    public UsersConveyController(
            UsersPoliciesService usersPoliciesService) {
        this.usersPoliciesService = usersPoliciesService;
    }

    @PostMapping("/export-excel")
    public @ResponseBody
    ResponseEntity<?> exportExcelEnabled(@RequestParam(value = "sheetName", defaultValue = "Sheet1", required = false) String sheetName,
                                         @RequestParam(value = "filename", defaultValue = "Users_Data_Exported.xlsx", required = false) String filename,
                                         @RequestBody UsersFilter usersFilter) {
        return TMSDocsResponse.buildScrapeDocsResponse(usersPoliciesService.exportUserToExcel(usersFilter, this.myUserDetails, sheetName), filename, ".xlsx");
    }
}
