<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsCampaignManagement_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action
{

    private $model;

    public function __construct()
    {
        parent::__construct();
        $this->exposeMethod('deleteCampaign');
        $this->exposeMethod('saveCampaign');
        $this->exposeMethod('exportExcel');
        $this->exposeMethod('start');
        $this->exposeMethod('stop');
        $this->exposeMethod('pause');
        $this->model = new BhsCampaignManagement_Module_Model();

    }

    public function getList(Vtiger_Request $request)
    { 
        global $adb;
        $moduleName = $request->getModule();
        $moduleModel = new BhsCampaignManagement_Module_Model();
        $res = array();
        $draw = intval($request->get('draw'));
        $agentOptions = $moduleModel->agentOptions();
        try {
            $data = array();
            $rows = $moduleModel->getRowsRefactor($request, 'campaign');
            $statusCam = $moduleModel->getStatusList();
            $dots = [
                'running' => 'bg-success',
                'stopped' => 'bg-danger',
                'paused' => 'bg-warning',
                'stopping' => 'bg-dark',
                'new' => 'bg-black'
            ];
            $camStatus = [
                'new' => [ 1, 0, 0, 1, 1],
                'running' => [ 0, 1, 1, 0, 0],
                'paused' => [ 1, 1, 0, 1, 0],
                'stopping' => [ 0, 0, 0, 0, 0],
                'stopped' => [ 0, 0, 0, 0, 1]
            ];
            foreach ($rows as $row) {
                if(!$row){
                    continue;
                }
                $id = $row['cpId'];
                $campaignProgress = $moduleModel->makeApiCall('GET', 'campaign/progress?cpId='.$id)['body']->data[0];
                $item = $row;
                $item['cpId'] = '<span style="color:#367BF5">' . $id . '</span>';
                $item['owner_id'] = $item['owner'];
                $item['name'] = $item['cpName'];
                $item['owner'] = $agentOptions[$item['owner']];
                foreach($statusCam as $st) {
                    if($st->value ==  $item['status']) {
                        foreach ($camStatus[$st->name] as $index => $action){
                            switch ($index) {
                                case 0: $run = ($action) ? '' : 'disabled';
                                case 1: $stop = ($action) ? '' : 'disabled';
                                case 2: $pause = ($action) ? '' : 'disabled';
                                case 3: $edit = ($action) ? '' : 'disabled';
                                case 4: $delete = ($action) ? '' : 'disabled';
                            }

                        }
                        $startBtn = '<a class="btnBhsAction  btnBhsStart ' . $run . '"  href="javascript: void(0);" title="Play" data-id="' . $id . '"><i class="fa fa-play"></i></a>';
                        $stopBtn = '<a class="btnBhsStop btnBhsAction  ' . $stop . '"  href="javascript: void(0);" title="Stop"  data-id="' . $id . '"><i class="fa fa-stop"></i></a>';
                        $pauseBtn = '<a class="btnBhsAction btnBhsPause ' . $pause . '" href="javascript: void(0);" title="Pause"  data-id="' . $id . '"><i class="fa fa-pause"></i></a>';
                        $deleteBtn = '<a class="btnBhsAction btnBhsDelete ' . $delete . '" href="javascript: void(0);" title="Delete"  data-id="' . $id . '"><i class="fa fa-trash"> Delete</i></a>';
                        $updateBtn = '<a class="btnBhsAction btnBhsUpdate ' . $edit . '" href="javascript: void(0);" title="Edit"  data-id="' . $id . '"><i class="fa fa-pencil"> Edit</i></a>';
                        $item['bhs_action'] = $startBtn . ' ' . $stopBtn . ' ' . $pauseBtn . ' <div class="btn-group btn-group-action">
                          <a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            <i class="fa fa-ellipsis-h" aria-hidden="true"></i>
                          </a>
                          <div class="dropdown-menu dropdown-menu-right" style="padding:5px; min-width: 125px;">'. $updateBtn . '' . $deleteBtn . '</div>
                        </div>';

                    }
                }
                // add dot for status name
                foreach($statusCam as $st) {
                    if($st->value ==  $item['status']) {
                        $item['status'] = '<span class="dot '. $dots[$st->name].'"></span>&nbsp&nbsp' . ucfirst($st->name);
                    }
                }
                //hard code
                $status='#4CAF50';
                $item['outboundagent'] = !empty($campaignProgress->outboundAgent )? $campaignProgress->outboundAgent:0;
                $process = !empty($campaignProgress->progress)?number_format($campaignProgress->progress,2):"0.00";
                if((1<$process)&&($process<25)){
                    $status='#F44336;';
                }
                if((25<=$process)&&($process<50)){
                    $status='#FFC107;';
                }
                if((50<=$process)&&($process<75)){
                    $status='#FF9800;';
                }
                if((75<=$process)&&($process<99)){
                    $status='#2196F3;';
                }
                $item['progress']=
                '<span style="color:'.$status.';" title="'.$campaignProgress->leadInProgress.'/'.$campaignProgress->leadInCampaign.'">
                                       <span class="text-right" >'.$process.'% </span>
                                   <div class="progress" >
                                       <span class="progress-bar" style="width:'.$process.'%;background: '.$status.';" ></span>
                                   </div>
                                   </span>';                    
                $data[] = $item;
            }
            $res = array(
                'success' => true,
                'message' => 'success',
                'recordsTotal' => $moduleModel->getRecordTotal(),
                'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'draw' => $draw,
                'data' => $data,
            );


        } catch (Exception $exception) {
            $res = array(
                'success' => false,
                'message' => $exception->getMessage(),
                'error' => $exception->getMessage(),
                'recordsTotal' => 0,
                'recordsFiltered' => 0,
                'draw' => $draw,
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }

    public function deleteCampaign(Vtiger_Request $request)
    {
        $res = array('success' => true);
        $id = $request->get('id');
        $moduleModel = new BhsCampaignManagement_Module_Model();
        try {
            $moduleModel->deleteCampaign($id);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function saveCampaign(Vtiger_Request $request)
    {
        $moduleModel = new BhsCampaignManagement_Module_Model();
        $res = array('success' => true);
        try {
            if ($request->get('cpId')) {
                $moduleModel->updateCampaign($request);
            } else {
                $moduleModel->saveCampaign($request);
            }
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

    public function start(Vtiger_Request $request)
    {
        $res = array('success' => true);
        $id = $request->get('id');
        try {
           $status=$this->model->campaignStart($id);
           if($status!=200){
            $res['success'] = false;
           }
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function stop(Vtiger_Request $request)
    {
        $res = array('success' => true);
        $id = $request->get('id');
        try {
            $this->model->campaignStop($id);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    public function pause(Vtiger_Request $request)
    {
        $res = array('success' => true);
        $id = $request->get('id');
        try {
            $this->model->campaignPause($id);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
}
