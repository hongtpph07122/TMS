<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsCombo_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('getList');
        $this->exposeMethod('saveRecord');
        $this->exposeMethod('changeStatus');
        $this->exposeMethod('deleteCombo');
    }


    function getList(Vtiger_Request $request){
        $draw = intval($request->get('draw'));
        $moduleModel = new BhsCombo_Module_Model();
        try{
            $data = [];
            $rows = $moduleModel->getAllCombos($request);
            foreach ($rows as $row){
                if(!$row){
                    continue;
                }
                $id = $row['prodId'];
                $item = $row;
                if (!empty($item['status'])) {
                    $item['status'] = '<button type="button" class="btn btn-sm btn-toggle active" data-id="' . $id . '" data-toggle="button" aria-pressed="true" autocomplete="off"><div class="handle"></div></button>';
                } else {
                    $item['status'] = '<button type="button" class="btn btn-sm btn-toggle" data-id="' . $id . '"  data-toggle="button" aria-pressed="true" autocomplete="off"><div class="handle"></div></button>';
                }
                $item['bhs_action'] = '<div class="btn-group btn-group-action btn-group-action-custom">
                      <a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                         <i class="fa fa-ellipsis-h" aria-hidden="true"></i>
                      </a>
                      <div class="dropdown-menu dropdown-menu-right">' . $this->createActionButtonsOnList($id) . '</div>
                    </div>';
//                $item['startDate']= !empty($item['startDate'])? Date('d/m/Y H:i:s', strtotime($item['startDate'])):"";
//                $item['endDate']= !empty($item['endDate'])? Date('d/m/Y H:i:s', strtotime($item['endDate'])):"";
                $item['price']= number_format($item['price']);
                $data[] = $item;
            }

            $res = [
                'recordsTotal' => $moduleModel->getRecordTotal(),
                'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'draw' => $draw,
                'data' => $data,
            ];

        } catch (Exception $exception){
            $res = [
                'error' => $exception->getMessage(),
                'recordsTotal' => 0,
                'recordsFiltered' => 0,
                'draw' => $draw,
                'data' => [],
            ];
        }
        $this->customEmit($res);
    }
    function deleteCombo(Vtiger_Request $request)
    {
        $moduleName = $request->getModule();
        $res = ['success' => true];
        $moduleModel = new BhsProduct_Module_Model();
        try {
            // @Todo
            $prodId = intval($request->get('id'));
            $result = $moduleModel->deleteRecordById($prodId);
            if (empty($result['body'])) {
                $res['success'] = false;
                $res['message'] = vtranslate('Cannot delete.', $moduleName);
            }
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    function saveRecord(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsCombo_Module_Model();
            $params = [];
            $params['comboInfo'] = $request->get('comboInfo');
            $params['prods'] = $request->get('prods');
            $return = $moduleModel->saveRecord($params);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    function changeStatus(Vtiger_Request $request)
    {
        $res = ['success' => true];
        try {
            $moduleModel = new BhsProduct_Module_Model();
            $params = [];
            $params['prodId'] = intval($request->get('id'));
            $params['status'] = $request->get('status');
            $record = $moduleModel->saveRecord($params);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
}
