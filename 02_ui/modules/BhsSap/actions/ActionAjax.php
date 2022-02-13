<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsSap_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('getList');
        $this->exposeMethod('getOrders');
    }


    function getList(Vtiger_Request $request){
        global $adb, $current_user;
        $moduleName = $request->getModule();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        $moduleModel = new BhsSap_Module_Model();
        try{
            $data = array();
            $rows = $moduleModel->getRowsRefactor($request, 'DO/carrier/sap');
            foreach ($rows as $row){
                if(!$row){
                    continue;
                }
                $item = $row;
                $item['checkbox']= '<input type="checkbox" data-id='. $row['doId'].' class="check-item"/>';
                $item['customerAddress']='<span title="'. $item['customerAddress'].'">'.$item['customerAddress'].'</span>';
                $item['amountcod']= number_format($item['amountcod']);
                $item['createdate']= !empty($item['createdate'])? Date('d/m/Y H:i:s', strtotime($item['createdate'])):"";
                $data[] = $item;
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
    function getOrders(Vtiger_Request $request){
        $res = array('success' => true);
        $moduleModel = new BhsSap_Module_Model();
        try{
            // @Todo
            $moduleModel->getOrders($request);
        } catch (Exception $exception){
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
}
