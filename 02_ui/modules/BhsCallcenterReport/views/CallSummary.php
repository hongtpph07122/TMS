<?php

class BhsCallcenterReport_CallSummary_View extends Vtiger_BhsList_View {
    function __construct() {
        parent::__construct();
    }

    function process (Vtiger_Request $request) {
        $viewer = $this->getViewer ($request);
        $moduleModel = new BhsCallCenterReport_Module_Model();
        $columnDefs = $moduleModel->getColumnDefs_CallDuration();
        $viewer->assign('COLUMN_DEFS', json_encode($columnDefs));
        $exts = $moduleModel->getExts();
        $viewer->assign('EXTS', $exts);
        parent::process($request, 'CallSummary.tpl');
    }
}