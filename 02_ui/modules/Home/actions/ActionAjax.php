<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class Home_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('monitor');
    }

    public function monitor(Vtiger_Request $request)
    {
        $searchDate = $request->get('datetime');
        $moduleModel = new Home_MainDashBoard_Model();
        if (empty($searchDate)) {
            echo json_encode($moduleModel->makeApiCall('GET', 'dashboard/monitor'));
        } else {
            $userTMS = Vtiger_Session::get('agent_info');
            $userId = $userTMS->info->userId;

            $dateValue = explode('-',$searchDate);
            $startDate = date('YmdHis',strtotime(str_replace('/','-',$dateValue[0])));
            $endDate = date('YmdHis',strtotime(str_replace('/','-',$dateValue[1])));
            $searchDate = $startDate.'|'.$endDate;
            echo json_encode($moduleModel->makeApiCall('GET', 'dashboard/monitor_history?userId='.$userId.'&searchDate='.$searchDate));
        }

    }
}
