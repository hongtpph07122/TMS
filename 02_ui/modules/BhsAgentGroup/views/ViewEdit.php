<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class BhsAgentGroup_ViewEdit_View extends Vtiger_BhsBasicAjax_View {
	function __construct() {
		parent::__construct();
	}

    /**
     * Function to display the UI for advance search on any of the module
     * @param Vtiger_Request $request
     */
    function showEdit(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $viewer = $this->getViewer($request);
        $viewer->assign('MODULE', $moduleName);
        echo $viewer->view('ShowEdit.tpl',$moduleName, true);
    }
}