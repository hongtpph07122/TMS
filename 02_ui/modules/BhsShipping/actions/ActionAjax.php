<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsShipping_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('cancelOrder');
        $this->exposeMethod('updateOrder');
        $this->exposeMethod('getList');
        $this->exposeMethod('validateDO');
        $this->exposeMethod('getToken');
        $this->exposeMethod('updateRescue');
    }


    function getList(Vtiger_Request $request){
        global $adb, $current_user;
        $moduleName = $request->getModule();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        $moduleModel = new BhsShipping_Module_Model();
        // $data[] = array( 'TC-'. $start , 'SO-'. $start , 'Customer name' . $start, '0156789934' . $start, 'Le Van Luong Street'.$start, '<span class="textColorGreen fontBold">Bimbim '. $start .'</span>', '<span class="text-info fontBold">MGate '. $start .'</span>', '<span class="text-info fontBold">Intransit' . $start .'</span>', '<span class="text-info">' . DateTimeField::convertToUserFormat(date('m-d-Y')).'</span>', '<a class="btnBhsCancel btn btn-danger marginBottom5px" style="color:#fff" href="javascript: void(0);" data-id="' . $id . '">Cancel Order</a>&nbsp;&nbsp; <a class="btnBhsCancel btn btn-blue" style="color:#fff" href="">Update Order</a>' );
        try{
            $data = array();
            $rows = $moduleModel->getRowsRefactor($request, 'DO');
            foreach ($rows as $row){
                if(!$row){
                    continue;
                }
                $id = $row['doId'];
                $item = $row;
                $item['leadId'] = $item['customerId'];
                $item['bhs_action'] ='<a class="btnBhsCancel btn btn-danger marginBottom5px" style="color:#fff" href="javascript: void(0);" data-id=" '.$id .' ">Cancel Order</a>';
                $item['doId'] ='<a class="btnBhsShowTracking" data-id="'.$id.'" style="color: #367BF5;" href="javascript: void(0);">' .$item['doId'] . '</a>';
                $item['createdate'] = $row['createdate'] ? Date('d/m/Y H:i:s', strtotime($row['createdate'])) : "";
                $item['customerName'] = $item['customer']->name;
                $item['customerPhone'] = $item['customer']->phone;
                $item['customerAddress'] = $item['customer']->address;
                $item['agency'] = $item['customer']->source;
                $item['statusSO']= $item['customer']->leadStatusName;
                $item['warehouseName']= $item['warehouse'];
                $item['carrierName']= $item['carrier'];
                $item['status']= $moduleModel->getStatus($row['status']) ;
                $item['prodId']= $this->getProductName($row['soId']);
                $item['checkbox']= '<input type="checkbox" data-id='. $row['doId'].' class="check-item"/>';
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
    public function cancelOrder(Vtiger_Request $request)
    {
        $res = array('success' => true);
        $id = $request->get('id');
        $moduleModel = new BhsShipping_Module_Model();
        try {
            $res = $moduleModel->cancelOrder($id)['body'];
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    public function updateOrder(Vtiger_Request $request)
    {
        $moduleModel = new BhsShipping_Module_Model();
        $res = array('success' => true);
        try {
          
                $moduleModel->updateOrder($request);
            
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function exportExcel()
    {
        $res = array('success' => true);
        try {
            // @Todo
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    function getProductName($ids){
         $moduleModel = new BhsShippingPending_Module_Model();
        // try{
        //     $data =array();
        //     $ids=explode(",",$ids);
        //     foreach ($ids as $id){
        //        $row = $moduleModel->makeApiCall('GET', 'products/'.$id);
        //        $namePro=$row['body']->data[0]->name;
        //        $data[]=$namePro;
        //   }
        //     return join(",",$data);
        // } catch (Exception $exception){
        //     return '';
        // }
        try {
            $data = array();
            $leads = [];
            $res = $moduleModel->makeApiCall('GET', 'SO/SaleOrderItems/' . $ids);
            $leads = $res['body']->data;
            foreach ($leads as $lead) {  
                $data[] = $lead->prodName;
            }
            return  join(",",$data);
        } catch (Exception $exception) {
            return  $data;
        }
    }
    function validateDO(Vtiger_Request $request){
        $res = array('success' => true);
        $moduleModel = new BhsShipping_Module_Model();
        try{
            // @Todo
        $moduleModel->validateDO($request);
        } catch (Exception $exception){
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    function getToken(Vtiger_Request $request)
    {
        $moduleModel = new BhsShipping_Module_Model();
        $res = $moduleModel->getToken();
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    function updateRescue() {
        $moduleModel = new BhsShipping_Module_Model();

        try{
            $res = $moduleModel->updateRescue();
        } catch (Exception $exception){
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }

        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
}
