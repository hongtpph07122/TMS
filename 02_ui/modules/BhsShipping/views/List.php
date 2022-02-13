<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class BhsShipping_List_View extends Vtiger_BhsList_View {
	function __construct() {
		parent::__construct();
	}
	function process(Vtiger_Request $request)
    {
        $viewer = $this->getViewer ($request);
        $moduleModel = new BhsShipping_Module_Model();
        $headers = $moduleModel->getHeaders();
        $columnDefs = $moduleModel->getColumnDefs();
        $viewer->assign('HEADERS', $headers);
        $viewer->assign('COLUMN_DEFS', json_encode($columnDefs));
        $viewer->assign('STATUS', $moduleModel->getStatusList()['delivery']);
        $userTMS = Vtiger_Session::get('agent_info');
        $viewer->assign('ROLE', $userTMS->roleId[0]);
        global $API_ENDPOINT;
        $viewer->assign('API_ENDPOINT', $API_ENDPOINT);
        parent::process($request);
    }
}