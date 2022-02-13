<?php

class BhsAgentReport_CallDetail_View extends Vtiger_BhsList_View {
    function __construct() {
        parent::__construct();
    }

    function process (Vtiger_Request $request) {
        $viewer = $this->getViewer ($request);
        $moduleModel = new BhsAgentReport_Module_Model();
        $columnDefs = $moduleModel->getColumnDefs_AgentLoginTime();
        $viewer->assign('COLUMN_DEFS', json_encode($columnDefs));
        parent::process($request, 'CallDetail.tpl');
    }
}