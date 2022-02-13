<?php
/**
 * Created by PhpStorm.
 * User: DucVA
 * Date: 4/4/2019
 * Time: 4:26 PM
 */
class Vtiger_BhsAddNew_View extends Vtiger_AddNew_View {
    function __construct() {
        parent::__construct();
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
            "~libraries/jquery/datatables/jquery.dataTables.min.js",
            "~libraries/jquery/datatables/dataTables.bootstrap.min.js",
            "~libraries/jquery/datatables/dataTables.responsive.min.js",
            "~libraries/jquery/datatables/custom_datatables.js",
            "modules.$moduleName.resources.AddNew",
        );

        $jsScriptInstances = $this->checkAndConvertJsScripts($jsFileNames);
        $headerScriptInstances = array_merge($headerScriptInstances, $jsScriptInstances);
        return $headerScriptInstances;
    }

    public function getHeaderCss(Vtiger_Request $request) {
        $headerCssInstances = parent::getHeaderCss($request);
        $moduleName = $request->getModule();

        $cssFileNames = array(
            "~libraries/jquery/datatables/dataTables.bootstrap.min.css",
            "~libraries/jquery/datatables/custom_datatables.css",
            "~layouts/v7/modules/$moduleName/resources/AddNew.css",
        );
        $cssInstances = $this->checkAndConvertCssStyles($cssFileNames);
        $headerCssInstances = array_merge($headerCssInstances, $cssInstances);
        return $headerCssInstances;
    }
}