<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsShippingPending_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('resendDo');
        $this->exposeMethod('removeDo');
    }

    function getList(Vtiger_Request $request){
        global $adb;
//        $moduleName = $request->getModule();
        $moduleModel = new BhsShippingPending_Module_Model();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            $rows = $this->getRowsRefactor($request, 'DO/pending', $moduleModel);
            foreach ($rows as $row){
                 if(!$row){
                    continue;
                }
//                $errorMessage = json_decode(explode('|',$row['errorMessage'])[1]);
                $id = $row['doId'];
                $item = $row;
//                $item['product']=$this->getProductName($item['soId']);
                $item['amount'] = CurrencyField::convertToUserFormat($row['amount'], null, true);
//                $item['result']=!empty($errorMessage->message)? 'Error '.$errorMessage->errorCode. ': '. $errorMessage->message: (!empty($errorMessage->result)?$errorMessage->result:'');
                $item['soIdkw']='';
                $item['soUpdatedate'] = $row['soUpdatedate'] ? Date('d/m/Y H:i:s', strtotime($row['soUpdatedate'])) : "";;
                $item['soId'] ='<a class="btnBhsShowTracking" data-id="'.$id.'" style="color: #367BF5;" href="javascript: void(0);">' .$item['soId'] . '</a>';
                $item['checkbox']= '<input type="checkbox" data-id='. $row['soId'].' class="check-item"/>';
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
            return  join(", ",$data);
        } catch (Exception $exception) {
            return  $data;
        }
    }
    
    function resendDo(Vtiger_Request $request) {
        $moduleModel = new BhsShippingPending_Module_Model();
        $doIds = $request->get('doIds');
        try {
            $json = [
                'doIds' => $doIds,
                'status' => 0
            ];
                $do = $moduleModel->makeApiCall('PUT', 'SO/resend', [
                    'json' => $json,
                    'headers' => [
                        'Content-Type' => 'application/json'
                    ]
                ]);

                if ($do['code'] != 200) {
                    throw new Exception($do['message']);
                }
                $doBody = $do['body'];
                if ($doBody->code != 200) {
                    throw new Exception($doBody->message);
                }

            $res = array(
                'success' => true,
                'message' => '',
                'data' => $doBody->data,
            );
        } catch (Exception $exception) {
            $res = array(
                'success' => false,
                'error' => $exception->getMessage(),
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }

    function removeDo(Vtiger_Request $request) {
        $moduleModel = new BhsShippingPending_Module_Model();
        $doIds = $request->get('doIds');
        try {
                $orgDo = $moduleModel->makeApiCall('GET', 'DO/remove', [
                    'json' => $doIds,
                    'headers' => [
                        'Content-Type' => 'application/json'
                    ]
                ]);

                if ($orgDo['code'] != 200) {
                    throw new Exception($orgDo['message']);
                }
                $orgDoBody = $orgDo['body'];

                if ($orgDoBody->code != 200) {
                    throw new Exception($orgDoBody->message);
                }

            $res = array(
                'success' => true,
                'message' => '',
                'data' => $orgDoBody->data
            );
        } catch (Exception $exception) {
            $res = array(
                'success' => false,
                'error' => $exception->getMessage(),
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }

    public function getRowsRefactor(Vtiger_Request $request, $function, BhsShippingPending_Module_Model $module)
    {

        try {
            $request2 = array();
            $form_params = $_REQUEST;
            $columns = $form_params['columns'];

            $dateFieldsFilter = ['soUpdatedate'];

            $form_params['columns'] = $columns;
            $form_params['draw'] = $request->get('draw');
            $form_params['columns'] = $request->get('columns');
            $form_params['order'] = $request->get('order');
            $form_params['search']['value'] = $request->get('keyword');
            $form_params['limit'] = $request->get('length');
            $form_params['offset'] = $request->get('start');
            foreach ($columns as $column) {
                if (isset($column['search']['value']) && $column['search']['value'] != '') {
                    if (in_array($column['name'], $dateFieldsFilter) && !empty($column['search']['value'])) {
                        $dateValue = explode('-', $column['search']['value']);
                        $startDate = date('YmdHis', strtotime(str_replace('/', '-', $dateValue[0])));
                        $endDate = date('YmdHis', strtotime(str_replace('/', '-', $dateValue[1])));
                        $dateValue = $startDate . '|' . $endDate;
                        $form_params[$column['name']] = $dateValue;
                    } else {
                        $form_params[$column['name']] = $column['search']['value'];
                    }
                }
            }
            $request2['query'] = $form_params;
            $apiResult = $module->makeApiCall('GET', $function, $request2);
            $apiResult['body']->recordsTotal = $apiResult['body']->total;
            $apiResult['body']->recordsFiltered = $apiResult['body']->total;
            $module->getTotalFromApiResult($apiResult);
            return $module->getContentsFromApiResult($apiResult);
        } catch (Exception $exception) {
            return array();
        }
    }
}
