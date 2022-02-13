<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsCampaign_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('deleteCampaign');
        $this->exposeMethod('saveCampaign');
    }
    
    function getList(Vtiger_Request $request){
        global $adb;
        $moduleModel = new BhsCampaign_Module_Model();
        $res = array();
        $draw = intval($request->get('draw'));
        try{
            $data = array();
            $rows = $moduleModel->getRowsRefactor($request, 'monitor/campaign');
            foreach ($rows as $row){
                $id = $row['cp_id'];
                $item = $row;
                $item['bhs_action'] = '<div class="btn-group btn-group-action btn-group-action-custom">
                      <a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                         <i class="fa fa-ellipsis-h" aria-hidden="true"></i>
                      </a>
                      <div class="dropdown-menu dropdown-menu-right">' . $this->createActionButtonsOnList($id) . '</div>
                    </div>';
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
