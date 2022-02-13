<?php
/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

class BhsCallbackManagement_CallsByStatus_Dashboard extends Vtiger_IndexAjax_View {

	public function process(Vtiger_Request $request) {
		$currentUser = Users_Record_Model::getCurrentUserModel();
		$viewer = $this->getViewer($request);
		$moduleName = $request->getModule();

		$linkId = $request->get('linkid');
		$data = $request->get('data');
		$dates = $request->get('createdtime');

		$widget = Vtiger_Widget_Model::getInstance($linkId, $currentUser->getId());

		$data = array();
		for ($idx = 0; $idx < 6; $idx++){
		    $row = array(
		        'line' => 'Line'. $idx,
		        'waiting' => rand(20, 50),
		        'talking' => rand(20, 50),
		        'total_offered' => rand(50, 70),
		        'total_lost' => rand(50, 70),
		        'lost_call' => rand(50, 70),
            );
            $data[] = $row;
        }

		$viewer->assign('WIDGET', $widget);
		$viewer->assign('MODULE_NAME', $moduleName);
		$viewer->assign('DATA', $data);
		$viewer->assign('CURRENTUSER', $currentUser);

		$accessibleUsers = $currentUser->getAccessibleUsersForModule($moduleName);
		$viewer->assign('ACCESSIBLE_USERS', $accessibleUsers);

		$content = $request->get('content');
		if(!empty($content)) {
			$viewer->view('dashboards/DashBoardWidgetContents.tpl', $moduleName);
		} else {
			$viewer->view('dashboards/CampaignsByStatus.tpl', $moduleName);
		}
	}
}
