<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsCallStrategy_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('getCallStrategy');
        $this->exposeMethod('saveConfig');
        $this->exposeMethod('getListAgent');
        $this->exposeMethod('saveCampaign');
    }
    
    function getList(Vtiger_Request $request){
        global $adb;
        $start = intval($request->get('start'));
        $length = intval($request->get('length'));
        $search = $request->get('search');
        $moduleModel = new BhsCallStrategy_Module_Model();
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $rows = $moduleModel->getRowsRefactor($request, 'campaign/callstrategy/1', array(0));
            $data = array();
            foreach ($rows as $row){
                
                $item = $row;
                $item['phonestatus'] =$row['callStatus'];
                $item['attempts'] = $row['attempt']; 
                $item['duration'] = $row['duration']; 
                $item['day'] = $row['day']; 
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
    function deleteCampaign(Vtiger_Request $request){
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

    function saveConfig(Vtiger_Request $request){
        $res = array('success' => true);
        $moduleModel = new BhsCallStrategy_Module_Model();
        try{
        $row= $moduleModel->saveConfig($request->get('data'));
        //$res['id'] = $row;
        } catch (Exception $exception){
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    function getCallStrategy(Vtiger_Request $request){
        $res = array('success' => true);
        try{
            $moduleModel = new BhsCallStrategy_Module_Model();
            $id = $request->get('id');
            $res['callstrategy'] = $moduleModel->getCallStrategy($id);
        } catch (Exception $exception){
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    public function saveCampaign(Vtiger_Request $request)
    {
        $moduleModel = new BhsCallStrategy_Module_Model();
        $res = array('success' => true);
        try {
               $row= $moduleModel->saveCampaign($request);
                // $res['success'] = false;
                $res['id'] = $row;
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
}
