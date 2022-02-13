<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class BhsProduct_List_View extends Vtiger_BhsList_View {
	function __construct() {
		parent::__construct();
	}

    function process(Vtiger_Request $request)
    {
        $viewer = $this->getViewer ($request);
        $moduleModel = new BhsProduct_Module_Model();
        $headers = $moduleModel->getHeaders();
        $columnDefs = $moduleModel->getColumnDefs();
        $viewer->assign('HEADERS', $headers);
        $viewer->assign('COLUMN_DEFS', json_encode($columnDefs));
        parent::process($request);
    }

    /**
     * Function to get the list of Script models to be included
     * @param Vtiger_Request $request
     * @return <Array> - List of Vtiger_JsScript_Model instances
     */
    function getHeaderScripts(Vtiger_Request $request) {
        $headerScriptInstances = parent::getHeaderScripts($request);
        $moduleName = $request->getModule();

        $jsFileNames = array(
            "~layouts/v7/modules/BhsProduct/resources/bootstrap-tagsinput.min.js",
        );

        $jsScriptInstances = $this->checkAndConvertJsScripts($jsFileNames);
        $headerScriptInstances = array_merge($headerScriptInstances, $jsScriptInstances);
        return $headerScriptInstances;
    }

    public function getHeaderCss(Vtiger_Request $request) {
        $headerCssInstances = parent::getHeaderCss($request);
        $moduleName = $request->getModule();

        $cssFileNames = array(
            "~layouts/v7/modules/BhsProduct/resources/bootstrap-tagsinput.css",
        );
        $cssInstances = $this->checkAndConvertCssStyles($cssFileNames);
        $headerCssInstances = array_merge($headerCssInstances, $cssInstances);
        return $headerCssInstances;
    }
}