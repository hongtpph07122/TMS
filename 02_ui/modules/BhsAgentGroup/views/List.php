<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class BhsAgentGroup_List_View extends Vtiger_BhsList_View
{

    function __construct()
    {
        parent::__construct();
    }

    public function process(
        Vtiger_Request $request,
        $templateName = 'ListViewContents.tpl'
    ) {
        $agentGroupModel = new BhsAgentGroup_Module_Model();
        $agentGroups = $agentGroupModel->getAgentGroup();
        $listAgent  = $agentGroupModel->getAgent();

        $groupId = $request->get('groupId', -2);
        $sFullname = $request->get('fullname');
        $viewer = $this->getViewer($request);
        $viewer->assign('GROUPID', $groupId);
        $viewer->assign('SEARCH_FULLNAME', $sFullname);
        $viewer->assign('AGENT_GROUPS', $agentGroups);
        $viewer->assign('AGENTS', $listAgent);

        parent::process($request, $templateName);
    }
}