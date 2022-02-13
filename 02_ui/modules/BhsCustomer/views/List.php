<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class BhsCustomer_List_View extends Vtiger_BhsList_View {
	function __construct() {
		parent::__construct();
	}

    function process(Vtiger_Request $request)
    {
        $viewer = $this->getViewer ($request);
        $moduleModel = new BhsCustomer_Module_Model();
        $headers = $moduleModel->getHeaders();
        $columnDefs = $moduleModel->getColumnDefs();
        $viewer->assign('HEADERS', $headers);
        $viewer->assign('COLUMN_DEFS', json_encode($columnDefs));
        $provinces = array(
            '1' => 'Provinces 1',
            '2' => 'Provinces 2',
            '3' => 'Provinces 3',
        );
        $viewer->assign('PROVINCES', $provinces);
        $customer_status = array(
            '1' => 'Status 1',
            '2' => 'Status 2',
            '3' => 'Status 3',
        );
        $viewer->assign('CUSTOMER_STATUS', $customer_status);
        $columns = array(
            '1' => 'Column 1',
            '2' => 'Column 2',
            '3' => 'Column 3',
        );
        $viewer->assign('COLUMNS', $columns);
        parent::process($request);
    }
}