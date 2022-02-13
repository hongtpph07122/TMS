<?php

/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

class Home_DashBoard_View extends Vtiger_DashBoard_View
{
    public function process(Vtiger_Request $request)
    {
        $viewer = $this->getViewer($request);
        $moduleModel = new Home_MainDashBoard_Model();
        $res = $moduleModel->makeApiCall('GET', 'dashboard/monitor');
        $mySale = $res['body']->data->mySale;
        $lead = $res['body']->data->lead;
        $compare = $res['body']->data->compare;
        $totalCall = $res['body']->data->totalCall;
        $moduleName = $request->getModule();
        $viewer->assign('mySale', $mySale);
        $viewer->assign('lead', $lead);
        $viewer->assign('compare', $compare->lst);
        $viewer->assign('totalCall', $totalCall);
        $viewer->assign('accessToken', $totalCall);
        $viewer->assign('MODULE', $moduleName);
        parent::process($request);
    }
}