<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class BhsValidation_ViewAjax_View extends Vtiger_BhsBasicAjax_View {
	function __construct() {
		parent::__construct();
	}

    /**
     * Function to display the UI for advance search on any of the module
     * @param Vtiger_Request $request
     */
    function showEdit(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $moduleModel = new BhsValidation_Module_Model();
        $viewer = $this->getViewer($request);
        $apiResult = $moduleModel->makeApiCall('GET','lead/'.$request->get('leadId').'/cdr' );
        $listCdr = $moduleModel->getContentsFromApiResult($apiResult);
        $viewer->assign('MODULE', $moduleName);
        $viewer->assign('LIST_CDR', $listCdr);
        $viewer->assign('LEAD_NAME', $request->get('leadName'));
        $viewer->assign('LEAD_PHONE', $request->get('leadPhone'));

        echo $viewer->view('showPlay.tpl',$moduleName, true);
    }
}