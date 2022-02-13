<?php
/**
 * Created by PhpStorm.
 * User: DucVA
 * Date: 4/22/2019
 * Time: 10:59 AM
 */

class BhsCallcenterReport_CustomerAdded_View extends Vtiger_BhsList_View {
    function __construct() {
        parent::__construct();
    }

    function process (Vtiger_Request $request) {
        $viewer = $this->getViewer ($request);
        $moduleModel = new BhsCallCenterReport_Module_Model();
        $columnDefs = $moduleModel->getColumnDefs_CustomerAdded();
        $viewer->assign('COLUMN_DEFS', json_encode($columnDefs));
        $users = $moduleModel->getUsers();
        $viewer->assign('USERS', $users);
        parent::process($request, 'CustomerAdded.tpl');
    }
}