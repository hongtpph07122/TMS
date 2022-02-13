<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class BhsOrders_ViewAjax_View extends Vtiger_BhsBasicAjax_View {
	function __construct() {
		parent::__construct();
        $this->exposeMethod('showReassignForm');
	}

    /**
     * Function to display the UI for advance search on any of the module
     * @param Vtiger_Request $request
     */
    function showReassignForm(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $moduleModel = new BhsOrders_Module_Model();
        $agentList = $moduleModel->makeApiCall('GET', 'agentgroup/getListAgent');
        $viewer = $this->getViewer($request);
        $viewer->assign('MODULE', $moduleName);
        $viewer->assign('AGENT_LIST', !empty($agentList['body'])? $agentList['body']->data: []);
        echo $viewer->view('showReassignForm.tpl',$moduleName, true);
    }
}