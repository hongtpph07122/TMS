<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsBlacklist_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('deleteCampaign');
        $this->exposeMethod('saveCampaign');
    }

    function getList(Vtiger_Request $request){
        $moduleModel = new BhsBlacklist_Module_Model();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            $rows = $moduleModel->getRows($request, 'blacklist');
            foreach ($rows as $row){
                $id = $row['lead_id'];
                $item = $row;
                $item['bhs_action'] = '<button type="button" class="btn btn-lg btn-toggle" data-toggle="button" aria-pressed="false" autocomplete="off">
							<div class="handle"></div>
						</button>';
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

    function saveCampaign(Vtiger_Request $request){
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
}
