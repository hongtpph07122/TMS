<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class BhsCallStrategy_List_View extends Vtiger_BhsList_View
{
    public function __construct()
    {
        parent::__construct();

    }

    public function process(Vtiger_Request $request)
    {
        $viewer = $this->getViewer($request);
        $moduleModel = new BhsCallStrategy_Module_Model();
        $cpid = $request->get('cpid');
        $cpid = intval($cpid);
        $res = $moduleModel->makeApiCall('GET', 'campaign/' . $cpid);
        $campain = $res['body']->data[0];
        $res = $moduleModel->makeApiCall('GET', 'campaign/config/' . $cpid);
        $config = $res['body']->data;
        $res = $moduleModel->makeApiCall('GET', 'campaign/'. $cpid.'/callinglist');
        $call_list_selected = $res['body']->data;
        $res = $moduleModel->makeApiCall('GET', 'agentgroup');
        $agentgroup = $res['body']->data;
        $res = $moduleModel->makeApiCall('GET', 'campaign/rule');
        $rules = $res['body']->data;
        $res = $moduleModel->makeApiCall('GET', 'campaign/callinglist');
        $calls = $res['body']->data;
        $res = $moduleModel->makeApiCall('GET', 'campaign/callstrategy');
        $callstrategy = $res['body']->data;
        $viewer->assign('CALLSTRATEGYS', $callstrategy);
        $viewer->assign('CALLS', $calls);
        $viewer->assign('CALL_SELECTED', $call_list_selected);
        $viewer->assign('RULES', $rules);
        $viewer->assign('CAMPAIN', $campain);
        $viewer->assign('AGENTGROUP', $agentgroup);
        $viewer->assign('RECORD', $config);
        parent::process($request);
    }
}
