package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.entities.TeamMembersEntity;
import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.dto.HttpStatusCodesResponseDTO;
import com.oauthcentralization.app.tmsoauth2.model.enums.*;
import com.oauthcentralization.app.tmsoauth2.model.filters.UsersFilter;
import com.oauthcentralization.app.tmsoauth2.model.request.SMGActionTrunkRequest;
import com.oauthcentralization.app.tmsoauth2.model.request.TeamsMemberRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.repositories.TeamMembersRepository;
import com.oauthcentralization.app.tmsoauth2.service.*;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import com.oauthcentralization.app.tmsoauth2.variables.MessagesVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"FieldCanBeLocal"})
@Service(value = "teamsService")
@Transactional
public class TeamsServiceImpl implements TeamsService {

    private static final Logger logger = LoggerFactory.getLogger(TeamsServiceImpl.class);

    private final TeamMembersRepository teamMembersRepository;
    private final SMGActionTrunkService smgActionTrunkService;
    private final UsersBaseService usersBaseService;
    private final TeamsBaseService teamsBaseService;
    private final CommonsService commonsService;

    @Autowired
    public TeamsServiceImpl(
            TeamMembersRepository teamMembersRepository,
            CommonsService commonsService,
            SMGActionTrunkService smgActionTrunkService,
            UsersBaseService usersBaseService,
            TeamsBaseService teamsBaseService) {
        this.teamMembersRepository = teamMembersRepository;
        this.commonsService = commonsService;
        this.smgActionTrunkService = smgActionTrunkService;
        this.usersBaseService = usersBaseService;
        this.teamsBaseService = teamsBaseService;
    }

    @Override
    public TMSResponse<?> addMembers(TeamsMemberRequest teamsMemberRequest, MyUserDetailsImpl myUserDetails) {

        try {
            boolean hasPermissions = commonsService.hasRole(myUserDetails, RoleType.ROLE_MANAGEMENT) ||
                    commonsService.hasRole(myUserDetails, RoleType.ROLE_ADMIN) ||
                    commonsService.hasRole(myUserDetails, RoleType.ROLE_MANAGER);

            if (!hasPermissions) {
                return TMSResponse.buildTMSResponse("You don't have permissions.", null, HttpStatusCodesResponseDTO.NOT_ACCEPTABLE);
            }

            if (!ObjectUtils.allNotNull(teamsMemberRequest.getTeamId())) {
                return TMSResponse.buildTMSResponse("Team not found.", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
            }

            if (CollectionsUtility.isEmpty(teamsMemberRequest.getUsersId())) {
                return TMSResponse.buildTMSResponse("Users not found.", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
            }


            UsersFilter usersFilter = new UsersFilter();
            usersFilter.setUsersId(teamsMemberRequest.getUsersId());
            usersFilter.setPageIndex(1);
            usersFilter.setPageSize(teamsMemberRequest.getUsersId().size());
            List<UsersResponse> users = usersBaseService.findUsersBy(usersFilter);

            if (CollectionsUtility.isEmpty(users)) {
                return TMSResponse.buildTMSResponse("Members undefined", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
            }

            for (UsersResponse user : users) {
                if (!commonsService.hasPermission(myUserDetails, PermissionType.ADD_USER_TEAM, Collections.singletonList(UsersType.findBy(users.get(0).getUserType())))) {
                    return TMSResponse.buildTMSResponse(String.format("You do not have the authority to add this user %s", user.getUsername()), null, HttpStatusCodesResponseDTO.METHOD_NOT_ALLOWED);
                }
            }


            SMGActionTrunkRequest smgActionTrunkRequest = new SMGActionTrunkRequest();
            List<TeamMembersEntity> teamMembersEntities = new ArrayList<>();
            List<String> listOfUsername = new ArrayList<>();
            List<Integer> usersId = new ArrayList<>();
            String formatted;
            String prefix;
            int counter = 0;

            for (Integer userId : teamsMemberRequest.getUsersId()) {
                if (!teamMembersRepository.existsByUserId(userId)) {
                    TeamMembersEntity teamMembersEntity = new TeamMembersEntity();
                    teamMembersEntity.setTeamId(teamsMemberRequest.getTeamId());
                    teamMembersEntity.setUserId(userId);
                    teamMembersEntities.add(teamMembersEntity);
                } else {
                    usersId.add(userId);
                }
            }

            if (CollectionsUtility.isNotEmpty(teamMembersEntities)) {
                for (TeamMembersEntity teamMembersEntity : teamMembersEntities) {
                    teamMembersRepository.save(teamMembersEntity);
                    counter++;
                }
            }

            if (counter >= 1) {
                prefix = MessagesVariable.USERS_ADDED;
                /* begin::Trunk */
                smgActionTrunkRequest.setMyUserDetails(myUserDetails);
                smgActionTrunkRequest.setModuleType(TrunkModuleType.OAUTH2_MODULE);
                smgActionTrunkRequest.setActionType(TrunkType.ADD_TEAM_MEMBERS);
                smgActionTrunkRequest.setRequestJson(teamsMemberRequest);
                smgActionTrunkRequest.setOnTable(TablesType.TEAM_MEMBERS_TABLE);
                smgActionTrunkService.saveAsPayload(smgActionTrunkRequest);
                /* end::Trunk */
            } else {
                prefix = MessagesVariable.NO_ADDED;
            }

            if (CollectionsUtility.isNotEmpty(usersId)) {
                usersFilter.setUsersId(usersId);
                usersFilter.setPageIndex(1);
                usersFilter.setPageSize(usersId.size());
                List<UsersResponse> usersResponses = usersBaseService.findUsersBy(usersFilter);
                if (CollectionsUtility.isNotEmpty(usersResponses)) {
                    listOfUsername = usersResponses.stream().map(UsersResponse::getUsername).collect(Collectors.toList());
                }

                if (counter == 0) {
                    formatted = String.format("%d %s, because users %s already exist(s) in this team or in other teams.", counter, prefix, CollectionsUtility.toString(listOfUsername, ", "));
                } else {
                    formatted = String.format("%d %s .But we cannot add users %s to this team because they already exist in this team or other teams.", counter, prefix, CollectionsUtility.toString(listOfUsername, ", "));
                }

            } else {
                formatted = String.format("%d %s ", counter, prefix);
            }

            return TMSResponse.buildTMSResponse(formatted, null, HttpStatusCodesResponseDTO.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return TMSResponse.buildTMSResponse("Can not add user(s) to team.", null, HttpStatusCodesResponseDTO.INTERNAL_SERVER_ERROR);
        }
    }
}
