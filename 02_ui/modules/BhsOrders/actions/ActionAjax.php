<?php

/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */

class BhsOrders_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action
{

    function __construct()
    {
        parent::__construct();
        $this->exposeMethod('cancelOrder');
        $this->exposeMethod('saveCampaign');
        $this->exposeMethod('getCallingList');
        $this->exposeMethod('saveLead');
        $this->exposeMethod('uploadExcel');
        $this->exposeMethod('getToken');
        $this->exposeMethod('assignAgentToOrder');
    }


    function getList(Vtiger_Request $request)
    {
        global $ROLE_VALIDATION,
               $NORI_CUSTOMER_ORGID,
                $ROLE_AGENT;
        $moduleName = $request->getModule();

        $moduleModel = new BhsOrders_Module_Model();
        $draw = intval($request->get('draw'));
        try {
            $data = array();
            $rows = $moduleModel->getRowsRefactor($request, 'SO');
            $userTMS = Vtiger_Session::get('agent_info');
//            $notAccessEditSo = !empty($userTMS) &&  (($userTMS->roleId[0] == $ROLE_VALIDATION && $userTMS->info->orgId == $NORI_CUSTOMER_ORGID) ||  $userTMS->roleId[0] == $ROLE_AGENT);
            
            foreach ($rows as $row) {
                if (!$row) {
                    continue;
                }
                $leadId = $row['leadId'];
                $item = $row;
                
                $item['bhs_check_item'] = '<input type="checkbox" class="check-item" data-id ="'.$leadId.'"/>';
                $item['leadId'] = '<a class="text-info" href="index.php?module=BhsOrderProcessing&view=Edit&type=UpdateLead&recordId=' . $leadId . '&returnUrl=BhsOrders">' . $leadId . '</a>';
//                if (!$notAccessEditSo) {
                    $item['soId'] = '<a class="text-info" href="index.php?module=BhsOrderProcessing&view=Edit&type=order&recordId='. $item['soId'] .'&returnUrl=BhsOrders">'. $item['soId'] .'</a>';
//                }
                $item['bhs_record_id'] = $leadId; // Id or primary key
                $item['name'] = '<span class="textColorGreen fontBold">' . $item['leadName'] . '</span>';
                $item['amount'] = '<span class="textColorOrage fontBold">' . CurrencyField::convertToUserFormat($item['amount'], null, true) . '</span>';
                if ($item['leadStatus'] == 'new') {
                    $item['leadStatus'] = '<span class="btn btn-custom btnYellor text-tr-bold">' . $item['leadStatus'] . '</span>';
                } elseif ($item['leadStatus'] == 'rejected') {
                    $item['leadStatus'] = '<span  title="' . $item['reason'] . '" class="btn btn-custom btnOrage ">' . $item['leadStatus'] . ' <img style="margin-top: -3px;" src="layouts/v7/resources/Images/icon/icon-info-black.png" alt=""></span>';
                } elseif ($item['leadStatus'] == 'callback propect' || $item['leadStatus'] == 'callback consult') {
                    $item['leadStatus'] = '<span  title="' . $item['reason'] . '" class="btn btn-custom btnblue ">Callback <img style="margin-top: -3px;" src="layouts/v7/resources/Images/icon/icon-info-black.png" alt=""></span>';
                } elseif ($item['leadStatus'] == 'unreachable' || $item['leadStatus'] == 'busy' || $item['leadStatus'] == 'noanswer') {
                    $item['leadStatus'] = '<span  title="' . $item['reason'] . '" class="btn btn-custom btnViolet ">UnCall <img style="margin-top: -3px;" src="layouts/v7/resources/Images/icon/icon-info-black.png" alt=""></span>';
                } elseif ($item['leadStatus'] == 'invalid' || $item['leadStatus'] == 'duplicated'|| $item['leadStatus'] == 'trash') {
                    $item['leadStatus'] = '<span  title="' . $item['reason'] . '" class="btn btn-custom btnblack ">Trash <img style="margin-top: -3px;" src="layouts/v7/resources/Images/icon/icon-info-black.png" alt=""></span>';
                } else {
                    $item['leadStatus'] = '<span class="btn btn-custom btnGreen ">' . $item['leadStatus'] . '</span>';
                }
                $item['address'] = '<span title="' . $item['address'] . '">' . $item['address'] . '</span>';
                $item['createdate'] = $row['createdate'] ? Date('d/m/Y H:i:s', strtotime($row['createdate'])) : "";
                $item['modifyDate'] = $row['createdate'] ? Date('d/m/Y H:i:s', strtotime($row['modifyDate'])) : "";
                $data[] = $item;
            }

            $res = array(
                'recordsTotal' => $moduleModel->getRecordTotal(),
                'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'draw' => $draw,
                'data' => $data,
            );

        } catch (Exception $exception) {
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
    public function assignAgentToOrder (Vtiger_Request $request)
    {
        $moduleModel = new BhsOrders_Module_Model();
        $json['leadIds'] = $request->get('leadIds'); // array
        $json['agentId'] = $request->get('agentId');
        if (!empty($request->get('leadStatus'))) {
            $json['status'] = $request->get('leadStatus');
        }
        $requestBody['json'] = $json;
        $res = $moduleModel->makeApiCall('POST', 'lead/reassign', $requestBody);
        echo json_encode($res) ;
    }
    public function getLastLines($string, $n = 1)
    {
        $lines = explode("\n", $string);
        // if(!$Lines[count($lines)-1]) {
        //     $n = $n +1;
        // }
        $lines = array_slice($lines, -$n);

        return implode("\n", $lines);
    }

    public function cancelOrder(Vtiger_Request $request)
    {
        $res = array('success' => true);
        $id = $request->get('id');
        $moduleModel = new BhsOrders_Module_Model();
        try {
            $row = $moduleModel->cancelOrder($request);
            //  $res['message']=$row['message'];
            //  $res['success'] = false;
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function getCallingList(Vtiger_Request $request)
    {
        $res = array('success' => true);
        $moduleModel = new BhsOrders_Module_Model();
        try {
            $cpid = $request->get('id');
            $res = $moduleModel->getCallingList($cpid);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function saveLead(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsOrders_Module_Model();
            $dataStatus = [
                "address" => $request->get('address'),
                "name" => $request->get('name'),
                "district" => $request->get('district'),
                "province" => $request->get('province'),
                "subdistrict" => $request->get('subdistrict'),
                "neighborhood" => $request->get('neighborhood'),
                "postalCode" => $request->get('postalCode'),
                "comment" => $request->get('comment'),
                "leadStatus" => 2,
                "leadType" => "M",
                "phone" => $request->get('phone'),
                "callinglistId" => $request->get('callinglistId'),
                "customerEmail" => $request->get('customerEmail'),
                "customerAge" => $request->get('customerAge'),
                "cpId" => $request->get('cpId'),
                "prodId" => $request->get('prodId'),
                "price" => $request->get('price'),
                "prodName" => $request->get('prodName')
            ];
            $status = $moduleModel->saveLeadInfo($dataStatus);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    public function uploadExcel(Vtiger_Request $request)
    {
        $moduleModel = new BhsOrders_Module_Model();
        $formData = $request->get('formData');
        $res = $moduleModel->uploadExcel($request);
        if ($res['code'] == 200) {
            $res['success'] = true;
            $res['message'] = 'success';
        } else {
            $res['success'] = false;
            $res['message'] = $res['message'];
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    function getToken(Vtiger_Request $request)
    {
        $moduleModel = new BhsOrders_Module_Model();
        $res = $moduleModel->getToken();
        //die($res);
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
}
