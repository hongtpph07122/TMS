package com.oauthcentralization.app.tmsoauth2.service.serviceImpl;

import com.oauthcentralization.app.tmsoauth2.entities.GroupsAssigneeEntity;
import com.oauthcentralization.app.tmsoauth2.model.abstracts.TMSResponse;
import com.oauthcentralization.app.tmsoauth2.model.dto.HttpStatusCodesResponseDTO;
import com.oauthcentralization.app.tmsoauth2.model.enums.RoleType;
import com.oauthcentralization.app.tmsoauth2.model.enums.TablesType;
import com.oauthcentralization.app.tmsoauth2.model.enums.TrunkModuleType;
import com.oauthcentralization.app.tmsoauth2.model.enums.TrunkType;
import com.oauthcentralization.app.tmsoauth2.model.filters.UsersFilter;
import com.oauthcentralization.app.tmsoauth2.model.request.GroupsMemberRequest;
import com.oauthcentralization.app.tmsoauth2.model.request.SMGActionTrunkRequest;
import com.oauthcentralization.app.tmsoauth2.model.response.UsersResponse;
import com.oauthcentralization.app.tmsoauth2.repositories.GroupsAssigneeRepository;
import com.oauthcentralization.app.tmsoauth2.service.CommonsService;
import com.oauthcentralization.app.tmsoauth2.service.GroupsService;
import com.oauthcentralization.app.tmsoauth2.service.SMGActionTrunkService;
import com.oauthcentralization.app.tmsoauth2.service.UsersBaseService;
import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import com.oauthcentralization.app.tmsoauth2.variables.MessagesVariable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"FieldCanBeLocal"})
@Service(value = "groupsService")
@Transactional
public class GroupsServiceImpl implements GroupsService {

    private static final Logger logger = LoggerFactory.getLogger(GroupsServiceImpl.class);

    private final GroupsAssigneeRepository groupsAssigneeRepository;
    private final CommonsService commonsService;
    private final SMGActionTrunkService smgActionTrunkService;
    private final UsersBaseService usersBaseService;

    @Autowired
    public GroupsServiceImpl(
            CommonsService commonsService,
            GroupsAssigneeRepository groupsAssigneeRepository,
            SMGActionTrunkService smgActionTrunkService,
            UsersBaseService usersBaseService) {
        this.commonsService = commonsService;
        this.groupsAssigneeRepository = groupsAssigneeRepository;
        this.smgActionTrunkService = smgActionTrunkService;
        this.usersBaseService = usersBaseService;
    }

    @Override
    public TMSResponse<?> addMembers(GroupsMemberRequest groupsMemberRequest, MyUserDetailsImpl myUserDetails) {

        try {
            boolean hasPermissions = commonsService.hasRole(myUserDetails, RoleType.ROLE_ALL_SUPER);

            if (!hasPermissions) {
                return TMSResponse.buildTMSResponse("You don't have permissions.", null, HttpStatusCodesResponseDTO.NOT_ACCEPTABLE);
            }

            if (!ObjectUtils.allNotNull(groupsMemberRequest.getGroupId())) {
                return TMSResponse.buildTMSResponse("Group Id not found.", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
            }

            if (CollectionsUtility.isEmpty(groupsMemberRequest.getUsersId())) {
                return TMSResponse.buildTMSResponse("Users not found.", null, HttpStatusCodesResponseDTO.BAD_REQUEST);
            }

            SMGActionTrunkRequest smgActionTrunkRequest = new SMGActionTrunkRequest();
            List<GroupsAssigneeEntity> groupsAssigneeEntities = new ArrayList<>();
            List<String> listOfUsername = new ArrayList<>();
            UsersFilter usersFilter = new UsersFilter();
            List<Integer> usersId = new ArrayList<>();
            String formatted;
            String prefix;
            int counter = 0;

            for (Integer userId : groupsMemberRequest.getUsersId()) {
                if (!groupsAssigneeRepository.existsByAgentIdAndGroupId(userId, groupsMemberRequest.getGroupId())) {
                    GroupsAssigneeEntity groupsAssigneeEntity = new GroupsAssigneeEntity();
                    groupsAssigneeEntity.setAgentId(userId);
                    groupsAssigneeEntity.setGroupId(groupsMemberRequest.getGroupId());
                    groupsAssigneeEntity.setModifiedAt(new Date());
                    groupsAssigneeEntity.setModifiedBy(myUserDetails.getUserId());
                    groupsAssigneeEntity.setAssigneeSkillsLevel(2);
                    groupsAssigneeEntities.add(groupsAssigneeEntity);
                } else {
                    usersId.add(userId);
                }
            }

            if (CollectionsUtility.isNotEmpty(groupsAssigneeEntities)) {
                for (GroupsAssigneeEntity groupsAssigneeEntity : groupsAssigneeEntities) {
                    groupsAssigneeRepository.save(groupsAssigneeEntity);
                    counter++;
                }
            }

            if (counter >= 1) {
                prefix = MessagesVariable.USERS_ADDED;
                /* begin::Trunk */
                smgActionTrunkRequest.setMyUserDetails(myUserDetails);
                smgActionTrunkRequest.setModuleType(TrunkModuleType.OAUTH2_MODULE);
                smgActionTrunkRequest.setActionType(TrunkType.ADD_GROUP_MEMBERS);
                smgActionTrunkRequest.setRequestJson(groupsMemberRequest);
                smgActionTrunkRequest.setOnTable(TablesType.GROUP_AGENT_TABLE);
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
                formatted = String.format("%d %s .We cannot add users %s to this group because they already exist in groups.", counter, prefix, CollectionsUtility.toString(listOfUsername, ", "));
            } else {
                formatted = String.format("%d %s ", counter, prefix);
            }

            return TMSResponse.buildTMSResponse(formatted, null, HttpStatusCodesResponseDTO.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return TMSResponse.buildTMSResponse("Can not add user(s) to group.", null, HttpStatusCodesResponseDTO.INTERNAL_SERVER_ERROR);
        }
    }
}
