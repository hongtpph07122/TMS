<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class BhsSearchOrders_List_View extends Vtiger_BhsList_View {
	function __construct() {
		parent::__construct();
	}

    function process(Vtiger_Request $request)
    {
        $viewer = $this->getViewer ($request);
        // $moduleModel = Vtiger_Module_Model::getInstance('BhsOrderProcessing');
        $moduleModel = new BhsSearchOrders_Module_Model();
        $headers = $moduleModel->getHeaders();
        $columnDefs = $moduleModel->getColumnDefs();
        $viewer->assign('HEADERS', $headers);
        $viewer->assign('COLUMN_DEFS', json_encode($columnDefs));

        parent::process($request);
    }
}