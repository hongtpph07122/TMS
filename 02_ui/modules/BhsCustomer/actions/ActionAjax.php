<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsCustomer_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('deleteCustomer');
        $this->exposeMethod('saveCustomer');
    }

    function deleteCustomer(Vtiger_Request $request){
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

    function saveCustomer(Vtiger_Request $request){
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

    function getList(Vtiger_Request $request){
        $moduleName = $request->getModule();
        $moduleModel = new BhsCustomer_Module_Model();
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            $rows = $moduleModel->getRows($request, 'customer', array(0));
            foreach ($rows as $row){
                $id = $row['lead_id'];
                $start += 1;
                $item = $row;
                $item['bhs_check_item'] = '<input type="checkbox" class="check-item" />';
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
}
