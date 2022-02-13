<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsCallbackManagement_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('getAssigned');
        $this->exposeMethod('assign');
        $this->exposeMethod('unassigned');
    }

    function getList(Vtiger_Request $request){
        $moduleModel = new BhsCallbackManagement_Module_Model();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            $_REQUEST['status'] = 'unassign';
            $rows = $moduleModel->getRowsRefactor($request, 'callback');
            foreach ($rows as $row){
                if(!$row){
                    continue;
                }
                // $id = $row['leadId'];
                // $item = $row;
                // $createdate = new DateTime($item['createdate']);
                // $createdate = $createdate->format('Y-m-d');
                // $item['createdate'] = $createdate;
                // $requestTime = new DateTime($item['requestTime']);
                // $requestTime = $requestTime->format('Y-m-d');
                // $item['requestTime'] = $requestTime;
                // $item['bhs_action'] = $this->createActionCallBackOnList($id);
                // $item['bhs_record_id'] = $id; // Id or primary key
                // $data[] = $item;
                $id = $row['leadId'];
                $item = $row;
                $item['bhs_check_item'] = '<input type="checkbox" class="check-item" data-id="'.$id.'"/>';
                $item['bhs_record_id'] = $id; // Id or primary key
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
    function getAssigned(Vtiger_Request $request){
        $moduleModel = new BhsCallbackManagement_Module_Model();
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
                $item['bhs_check_item'] = '<input type="checkbox" class="check-item" data-id="'.$id.'"/>';
                $item['bhs_record_id'] = $id; // Id or primary key
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

    public function assign(Vtiger_Request $request){
        $moduleModel = new BhsCallbackManagement_Module_Model();
        try{

            $request1['json'] = explode(',',$request->get('leadIds'));
            $request1['headers']['Content-Type'] = 'application/json';
            $data = $moduleModel->makeApiCall('PUT', 'callback/assign', $request1);
            $res['success'] = $data['code'] == 200;
            $res['error'] = $data['code'] != 200;
        } catch (Exception $exception){
        }
        echo json_encode($res);
    }

    public function unassigned(Vtiger_Request $request){
        $moduleModel = new BhsCallbackManagement_Module_Model();
        $res['success'] = false;
        $res['error'] = true;
        try{
            $request1['json'] = explode(',',$request->get('leadIds'));
            $request1['headers']['Content-Type'] = 'application/json';
            $data = $moduleModel->makeApiCall('PUT', 'callback/unassign',$request1);
            $res['success'] = $data['code'] == 200;
            $res['error'] = $data['code'] != 200;
        } catch (Exception $exception){
        }
        echo json_encode($res);
    }
}

