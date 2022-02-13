<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsValidation_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();

        $this->exposeMethod('validateOrder');
    }

    function validateOrder(Vtiger_Request $request){
        $res = ['success' => true];
        $moduleModel = new BhsValidation_Module_Model();
        try{
            // @Todo
            $resApi = $moduleModel->validateOrder($request);
            if(!empty($resApi['body'])&&$resApi['body']->code == 200){
                $res['success'] = true;
                $res['message'] = $resApi['body']->message;
            } else {
                $res['success'] = false;
                $res['message'] = $resApi['body']->message;
            }
        } catch (Exception $exception){
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    function getList(Vtiger_Request $request){
        $userTMS = Vtiger_Session::get('agent_info');
        global $ROLE_VALIDATION,
               $NORI_CUSTOMER_ORGID;
        $draw = intval($request->get('draw'));
        $moduleModel = new BhsValidation_Module_Model();
        $notAccessEditSo = !empty($userTMS) && $userTMS->roleId[0] == $ROLE_VALIDATION && $userTMS->info->orgId == $NORI_CUSTOMER_ORGID;
        try{
            $data = [];
            $rows = $moduleModel->getRowsRefactor($request, 'SO/ValidationList', [], [7 => ['data' => 'amount','name' => 'amount']]);
            
            foreach ($rows as $row){
                if(!$row){
                    continue;
                }
                $item = $row;
                $item['checkbox']= '<input type="checkbox" data-id='. $item['soId'].' data-orderstatus = "'.$item['orderStatus'].'"class="check-item"/>';
                $item['play']= '<a class="btnPlay" href="javascript:void(0);" data-id='. $item['leadId'].' data-lead-name="'. $item['leadName'].'" data-lead-phone="'. $item['leadPhone'].'"><i class="fa fa-play"></i></a>';
                if (!$notAccessEditSo) {
                    $item['soId']= '<a class="text-info" href="index.php?module=BhsOrderProcessing&view=Edit&type=order&recordId='. $item['soId'] .'&returnUrl=BhsValidation">'. $item['soId'] .'</a>';
                }
                $item['totalprice']=CurrencyField::convertToUserFormat($row['amount'], null, true);
                $item['carrier']=$item['lastmileName'];
                $item['assign']=$item['agentName'];
                if ($item['leadStatus'] == 'new') {
                    $item['leadStatus'] = '<span class="btn btn-custom btnYellor text-tr-bold">' . $item['leadStatus'] . '</span>';
                } elseif ($item['leadStatus'] == 'rejected') {
                    $item['leadStatus'] = '<span  title="' . $item['comment'] . '" class="btn btn-custom btnOrage ">' . $item['leadStatus'] . ' <img style="margin-top: -3px;" src="layouts/v7/resources/Images/icon/icon-info-black.png" alt=""></span>';
                } elseif ($item['leadStatus'] == 'callback propect' || $item['leadStatus'] == 'callback consult') {
                    $item['leadStatus'] = '<span  title="' . $item['comment'] . '" class="btn btn-custom btnblue ">Callback <img style="margin-top: -3px;" src="layouts/v7/resources/Images/icon/icon-info-black.png" alt=""></span>';
                } elseif ($item['leadStatus'] == 'unreachable' || $item['leadStatus'] == 'busy' || $item['leadStatus'] == 'noanswer') {
                    $item['leadStatus'] = '<span  title="' . $item['comment'] . '" class="btn btn-custom btnViolet ">UnCall <img style="margin-top: -3px;" src="layouts/v7/resources/Images/icon/icon-info-black.png" alt=""></span>';
                } elseif ($item['leadStatus'] == 'invalid' || $item['leadStatus'] == 'duplicated') {
                    $item['leadStatus'] = '<span class="btn btn-custom btnblack ">Trash</span>';
                } else {
                    $item['leadStatus'] = '<span class="btn btn-custom btnGreen ">' . $item['leadStatus'] . '</span>';
                }
                $item['comment']='<span title="'. $item['comment'].'">'.$item['comment'].'</span>';
                $item['address']='<span title="'. $item['address'].'">'.$item['address'].'</span>';
                $item['createdate']=$row['createdate'] ? Date('d/m/Y H:i:s', strtotime($row['createdate'])) : "";
                $item['modifyDate']=$row['modifyDate'] ? Date('d/m/Y H:i:s', strtotime($row['modifyDate'])) : "";
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
}
