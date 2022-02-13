<?php
/**
 * Created by PhpStorm.
 * User: DucVA
 * Date: 4/22/2019
 * Time: 10:59 AM
 */

class BhsCampaignReport_PhoneContacted_View extends Vtiger_BhsList_View {
    function __construct() {
        parent::__construct();
    }

    function process (Vtiger_Request $request) {
        $viewer = $this->getViewer ($request);
        $moduleModel = new BhsCampaignReport_Module_Model();
        $columnDefs = $moduleModel->getColumnDefs_PhoneContacted();
        $viewer->assign('COLUMN_DEFS', json_encode($columnDefs));
        parent::process($request, 'PhoneContacted.tpl');
    }
}