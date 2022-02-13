<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsCallbackList_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('getAssignedList');
        $this->exposeMethod('getUnassignedList');
        $this->exposeMethod('CallbackTake');
        $this->exposeMethod('updateRequestTime');

    }

    function getUnassignedList(Vtiger_Request $request){
        global $adb;
        $moduleName = $request->getModule();
        $moduleModel = new BhsCallbackList_Module_Model();
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
//            $request->set('status', 'unassign');
            $_REQUEST['status'] = 'unassign';
            $rows = $moduleModel->getRowsRefactor($request, 'callback');
            foreach ($rows as $row){
                if(!$row){
                    continue;
                }
                $id = $row['leadId'];
                $item = $row;
                $item['takeAction'] =  '<input type="checkbox" class="check-item" data-id = "'.$id.'"/>';
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

    function getAssignedList(Vtiger_Request $request){
        global $adb;
        $moduleName = $request->getModule();
        $moduleModel = new BhsCallbackList_Module_Model();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            $rows = $moduleModel->getRowsRefactor($request, 'callback');
            foreach ($rows as $row){
                if(!$row){
                    continue;
                }
                $id = $row['leadId'];
                $item = $row;
                $item['bhs_action'] = $this->createActionCallBackOnList($id);
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


    public function CallbackTake(Vtiger_Request $request){
        $moduleModel = new BhsCallbackList_Module_Model();
        $res = array('success' => true);
        $id = $request->get('id');
        try {
            $moduleModel->updateTake($id);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function updateRequestTime(Vtiger_Request $request){
        $moduleModel = new BhsCallbackList_Module_Model();
        $res = array('success' => true);
        $data = array();
        $data['requestTime'] = $request->get('requestTime');
        $id=$request->get('id');
        try {
          $moduleModel->updateTime($data,$id);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

}
