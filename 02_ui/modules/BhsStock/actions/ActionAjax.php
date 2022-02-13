<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsStock_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('updateProduct');
        $this->exposeMethod('exportExcel');
        $this->exposeMethod('createStock');
    }

    function getList(Vtiger_Request $request){
        global $adb;
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        $moduleModel = new BhsStock_Module_Model();
        try{
            $data = array();
            // for($id = 1; $id <= $length; $id++){
            //     $start += 1;
            //     $data[] = array(
            //         $start,
            //         'Product Name ' . $start,
            //         'Sendo '.$start,
            //         $start,
            //         '1'. $start
            //     );
            // }
            $moduleOrderModel = new BhsOrderProcessing_Module_Model();
            $listStock = $moduleOrderModel->getListStock()->lst;
            $rows = $moduleModel->getRowsRefactor($request, 'products/StockByProduct');
            foreach ($rows as $row){
                if(!$row['partnerId']){
                    continue;
                } 
                foreach ($listStock as $st) {
                    if ($st->pnId == $row['partnerId']) {
                        $row['partnerId'] = $st->shortname;
                        break;
                    }
                }
                $data[] = $row;
            }
            $res = array(
                'recordsTotal' => $moduleModel->getRecordTotal(),
                'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception){
            $res = array(
                'error' => $exception->getMessage(),
                'recordsTotal' => 0,
                'recordsFiltered' => 0,
                'draw' => $draw,
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }
    function exportExcel() {
        $res = array('success' => true);
        try{
            // @Todo
        } catch (Exception $exception){
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    function createStock(Vtiger_Request $request) {
        $stock = [
            "partnerId"=>  $request->get('partnerId'),
            "prodId"=> $request->get('prodId'),
            "quantityAvailable"=> $request->get('quantityAvailable'),
            "quantityTotal" => 1500
        ];
       ;
        $request1['json'] = $stock;
        $request1['headers']['Content-Type'] = 'application/json';
        $moduleModel = new BhsStock_Module_Model();
        $res = $moduleModel->makeApiCall('POST', 'stock', $request1);

        return $res;
    }
}
