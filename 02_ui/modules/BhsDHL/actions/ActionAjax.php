<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsDHL_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

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
        $moduleModel = new BhsDHL_Module_Model();
        // $data[] = array( 'TC-'. $start , 'SO-'. $start , 'Customer name' . $start, '0156789934' . $start, 'Le Van Luong Street'.$start, '<span class="textColorGreen fontBold">Bimbim '. $start .'</span>', '<span class="text-info fontBold">MGate '. $start .'</span>', '<span class="text-info fontBold">Intransit' . $start .'</span>', '<span class="text-info">' . DateTimeField::convertToUserFormat(date('m-d-Y')).'</span>', '<a class="btnBhsCancel btn btn-danger marginBottom5px" style="color:#fff" href="javascript: void(0);" data-id="' . $id . '">Cancel Order</a>&nbsp;&nbsp; <a class="btnBhsCancel btn btn-blue" style="color:#fff" href="">Update Order</a>' );
        try{
            $data = array();
            $rows = $moduleModel->getRowsRefactor($request, 'DO/carrier/dhl');
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
        $moduleModel = new BhsDHL_Module_Model();
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
