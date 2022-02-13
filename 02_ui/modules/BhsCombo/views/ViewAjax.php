<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class BhsCombo_ViewAjax_View extends Vtiger_BhsBasicAjax_View {
	function __construct() {
		parent::__construct();
        $this->exposeMethod('showDetail');
	}

    /**
     * Function to display the UI for advance search on any of the module
     * @param Vtiger_Request $request
     */
    function showDetail(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $viewer = $this->getViewer($request);
        $moduleModel = new BhsCombo_Module_Model();
        $recordId = $request->get('id');
        $recordId = intval($recordId);
        $record = array();
        if ($recordId > 0) {
            $record = $moduleModel->getRecordById('products', $recordId);
        }
        $price=explode("|",$record->price);
        $string="";
        foreach($price as $p){
            $p=number_format($p);
            $string.='<span class="tag label label-info">'.$p.'</span>';
        }
        $record->price=$string;
        $modifydate= new DateTime($record->modifydate);
        $record->modifydate=$modifydate->format('d/m/Y H:i:s');
        $viewer->assign('MODULE', $moduleName);
        $viewer->assign('RECORD', $record);
        echo $viewer->view('ShowDetail.tpl',$moduleName, true);
    }

    /**
     * Function to display the UI for advance search on any of the module
     * @param Vtiger_Request $request
     */
    function showEdit(Vtiger_Request $request) {
        $moduleName = $request->getModule();
        $moduleModel = new BhsCombo_Module_Model();
        $modelProduct = new BhsProduct_Module_Model();

        $products = $modelProduct->getAllProductsActive();
        $recordId = $request->get('id');
        $recordId = intval($recordId);
        $record = array();
        if ($recordId > 0) {
            $record = $moduleModel->getRecordById('products', $recordId);
        }

        $list_product = array();
        foreach ( $products as $product ){
            $list_product[$product['prodId']] = $product;
        }
        foreach ($record->items as $key => &$item){
            $count = 0;
            $price_index = 0;
            $list_price = $list_product[$item->productId]['price'];
            $list_price = explode('|',$list_price);
            foreach ($list_price as $price){
                if ( (int)$item->price == (int)$price){
                    $price_index = $count;
                }
                $count++;
            }
            $item->price_index = $price_index;
        }
        unset($item);

        $currency = $moduleModel->getCurrencyWithOrgId();
        $viewer = $this->getViewer($request);
        $viewer->assign('MODULE', $moduleName);
        $viewer->assign('PRODUCTS', $list_product);
        $viewer->assign('RECORD_ID', $recordId);
        $viewer->assign('RECORD', $record);
        $viewer->assign('CURRENCY', $currency);
        echo $viewer->view('ShowEdit.tpl',$moduleName, true);
    }
}
