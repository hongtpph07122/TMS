<?php
/*+**********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.1
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 ************************************************************************************/

class BhsShipping_ViewEdit_View extends Vtiger_BhsBasicAjax_View {
	function __construct() {
		parent::__construct();
	}

    /**
     * Function to display the UI for advance search on any of the module
     * @param Vtiger_Request $request
     */
    function showEdit(Vtiger_Request $request) {
        $moduleModel = new BhsShipping_Module_Model();
        $moduleName = $request->getModule();
         $viewer = $this->getViewer($request);
         $viewer->assign('MODULE', $moduleName);
         $id=$request->get('id');
         $type=$request->get('type');
         $order=$moduleModel->orderDetail($id);
         $doHistory = $moduleModel->makeApiCall('GET', 'DO/'.$id.'/history')['body']->data;
         $DO = $order->doData;
         $DO->history = $doHistory;
         $customer=$order->leadData;
         $pro=$moduleModel->getProducts($DO->soId);
         $createdate = new DateTime($DO->createdate);
         $DO->createdate= $createdate->format('d-m-Y H:m:i');
         if($DO->approvedTime){
            $approvedTime=new DateTime($DO->approvedTime);
         $DO->approvedTime=$approvedTime->format('d-m-Y H:m:i');
         }
         $DO->stocks=$moduleModel->getStocks($DO->prodId);
         $DO->status=$moduleModel->getStatus($DO->status);
         if($DO->amountcod != null || $DO->amountcod != '')
         {
            $DO->amountcod = number_format($DO->amountcod, 0, '', ',');
         }         
        $viewer->assign('ORDER', $DO);
        $viewer->assign('PRODUCTS', $pro);
        $viewer->assign('CUSTOMER', $customer);
        echo $viewer->view('ShowEdit.tpl',$moduleName, true);
    }
}