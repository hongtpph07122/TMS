<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class Vtiger_BhsList_View extends Vtiger_List_View {
	function __construct() {
		parent::__construct();
	}

    function process (Vtiger_Request $request, $templateName = 'ListViewContents.tpl') {
        $viewer = $this->getViewer ($request);
        $moduleName = $request->getModule();
        $moduleModel = Vtiger_Module_Model::getInstance($moduleName);
        $viewName = $request->get('viewname');
        if(!empty($viewName)) {
            $this->viewName = $viewName;
        }

        $this->initializeListViewContents($request, $viewer);
        $this->assignCustomViews($request,$viewer);
        $viewer->assign('VIEW', $request->get('view'));
        $viewer->assign('MODULE_MODEL', $moduleModel);
        $viewer->assign('RECORD_ACTIONS', $this->getRecordActionsFromModule($moduleModel));
        $viewer->assign('CURRENT_USER_MODEL', Users_Record_Model::getCurrentUserModel());
        $viewer->view($templateName, $moduleName);
    }

	/**
	 * Function to get the list of Script models to be included
	 * @param Vtiger_Request $request
	 * @return <Array> - List of Vtiger_JsScript_Model instances
	 */
	function getHeaderScripts(Vtiger_Request $request) {
		$headerScriptInstances = parent::getHeaderScripts($request);
		$moduleName = $request->getModule();
		$viewName = $request->get('view');

		$jsFileNames = array(
			"~libraries/jquery/datatables/jquery.dataTables.min.js",
			"~libraries/jquery/datatables/dataTables.bootstrap.min.js",
			// "~libraries/jquery/datatables/dataTables.responsive.min.js",
			// "~libraries/jquery/datatables/dataTables.select.min.js",
			// "~libraries/jquery/datatables/dataTables.buttons.min.js",
			"~libraries/jquery/datatables/custom_datatables.js",
            "modules.$moduleName.resources.$viewName",
		);
		$jsScriptInstances = $this->checkAndConvertJsScripts($jsFileNames);
		$headerScriptInstances = array_merge($headerScriptInstances, $jsScriptInstances);
		return $headerScriptInstances;
	}

    public function getHeaderCss(Vtiger_Request $request) {
        $headerCssInstances = parent::getHeaderCss($request);
        $moduleName = $request->getModule();
        $viewName = $request->get('view');

        $cssFileNames = array(
            "~libraries/jquery/datatables/dataTables.bootstrap.min.css",
            "~libraries/jquery/datatables/custom_datatables.css",
            // "~libraries/jquery/datatables/select.dataTables.min.css",
            // "~libraries/jquery/datatables/buttons.dataTables.min.css",
            "~layouts/v7/modules/$moduleName/resources/$viewName.css",
        );
        $cssInstances = $this->checkAndConvertCssStyles($cssFileNames);
        $headerCssInstances = array_merge($headerCssInstances, $cssInstances);
        return $headerCssInstances;
    }
}