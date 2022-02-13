<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsAgentMonitoring_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action
{

    public function __construct()
    {
        parent::__construct();
        $this->exposeMethod('showCalling');
        $this->exposeMethod('getToken');
        $this->exposeMethod('getTotalRecords');
        $this->exposeMethod('getEditUserAgent');
        $this->exposeMethod('saveUser');
        $this->exposeMethod('editUser');
        $this->exposeMethod('deactiveUser');
        $this->exposeMethod('activeUser');
        $this->exposeMethod('deleteUser');
        $this->exposeMethod('getQuota');
        $this->exposeMethod('getUserType');
        $this->exposeMethod('getResetPassword');
    }

    public function getList(Vtiger_Request $request)
    {
        // global $adb;
        // $start = intval($request->get('start'));
        // $length = intval($request->get('length'));
        // $search = $request->get('search');
        $moduleModel = new BhsAgentGroup_Module_Model();
        $res = array();
        $draw = intval($request->get('draw'));
        try {
            $data = array();
            $agentStatus = ['Oncall', 'Available', 'Offline'];

            $rows = $moduleModel->getRowsRefactor($request, 'agent');
            foreach ($rows as $row){
                if(!$row){
                    continue;
                }                
                
                $id = $row['userId'];
                $item = $row;
                $disable = $row['userLock'] == '0' ? '' : 'style="pointer-events: none;color: #ccc;"';
                $enable = $row['userLock'] == '1' ? '' : 'style="pointer-events: none;color: #ccc;"';
                $disableBtn = '<a class="btnBhsAction btnBhsDisable" '. $disable .' id="BhsDisable" href="javascript: void(0);" title="Disable"  data-id="' . $row['userId'] . '" data-fullname="'. $row['fullname']. '"><i class="fa fa-pause"> Disable</i></a>';
                $enableBtn = '<a class="btnBhsAction btnBhsEnable"  ' . $enable . ' id="BhsEnable" href="javascript: void(0);" title="Enable"  data-id="' . $row['userId'] . '" data-fullname="'. $row['fullname']. '"><i class="fa fa-play"> Enable</i></a>';
                $editBtn = '<a class="btnBhsAction btnBhsEdit" id="BhsEdit" href="javascript: void(0);" title="Edit"  data-id="' . $row['userId'] . '" data-fullname="'. $row['fullname'] . '" data-usertype="' . $row['userType'] . '" data-userlock="' . $row['userLock'] . '" data-email="' . $row['email'] . '" data-sipphone="' . $row['phone'] . '" data-born="' . $row['birthday'] . '" data-phone="'. $row['personalPhone1'] .'" data-address="'. $row['homeAddress'] .'"><i class="fa fa-edit"> Edit</i></a>';
                $deleteBtn = '<a class="btnBhsAction btnBhsDelete" id="BhsDelete" href="javascript: void(0);" title="Delete"  data-id="' . $row['userId'] . '" data-fullname="'. $row['fullname']. '"><i class="fa fa-trash"> Delete</i></a>';
                $item['bhs_action'] = '<a class="headphone" href="" data-id="'. $start. '"><i class="fa fa-headphones" aria-hidden="true"></i></a>
                <div class="btn-group btn-group-action btn-group-action-custom" style="position: absolute;">
                <a class="dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <i class="fa fa-ellipsis-h" aria-hidden="true"></i>
                </a>
                    <div class="dropdown-menu dropdown-menu-right"  style="padding:5px; min-width: 125px;right: 20px;top: 0px;">'. $editBtn . '' . $deleteBtn . ''. $disableBtn . '' . $enableBtn . '</div>
                </div>';
                $item['password'] = '<button type="button" id="resetpassword" class="resetpassword btn btn-info" data-id="'. $row['userId']. '" data-email="'. $row['email']. '">Reset</button>';
                $item['bhs_record_id'] = $id; // Id or primary key
                if($row['modifyby'] != null || $row['modifyby'] != '')
                {
                    $userInfo = $moduleModel->getUserByID($row['modifyby']);
                    $item['modifyby'] = $userInfo[0]->fullname;
                }
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

    function showCalling(Vtiger_Request $request) {
        $res = [
            'status' => 'Success',
            'message' => 'On listening',
            'data' => [
                'id' => $request->get('id')
            ]
        ];
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    function getTotalRecords(Vtiger_Request $request) {
        $moduleModel = new BhsAgentGroup_Module_Model();
        $res = $moduleModel->getListAgent();
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    function getToken(Vtiger_Request $request)
    {
        $moduleModel = new BhsAgentMonitoring_Module_Model();
        $res = [
            'token' => $moduleModel->getToken(),
            'data' => [
                'id' => $request->get('userID'),
                'email' => $request->get("email"),
            ]
        ];
        //die($res);
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    function getEditUserAgent(Vtiger_Request $request) {
        $moduleModel = new BhsAgentMonitoring_Module_Model();
        $res = [
            'token' => $moduleModel->getToken(),
            'data' => [
                'id' => $request->get('userID'),
                'email' => $request->get("email"),
                'usertype' => $request->get("usertype"),
                'fullname' => $request->get("fullname"),
                'userlock' => $request->get("userlock"),
                'phone' => $request->get("phone"),
                'born' => $request->get("born"),
                'sipphone' => $request->get("sipphone"),
                'address' => $request->get("address")
            ]
        ];
        //die($res);
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    function saveUser(Vtiger_Request $request) {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsAgentMonitoring_Module_Model();
            $userTMS = Vtiger_Session::get('agent_info');
            $dataStatus = [
                "user_type" => $request->get('user_type'),
                "fullname" => $request->get('fullname'),
                "email" => $request->get('email'),
                "phone" => $request->get('phone'),
                "birthday" => $request->get('birthday'),
                "user_lock" => $request->get('user_lock'),
                "user_name" => $request->get('user_name'),
                "password" => $request->get('password'),
                "home_phone_1" => $request->get('home_phone_1'),
                "home_phone_2" => $request->get('home_phone_2'),
                "personal_phone_1" => $request->get('personal_phone_1'),
                "personal_phone_2" => $request->get('personal_phone_2'),
                "work_mail" => $request->get('work_mail'),
                "personal_mail" => $request->get('personal_mail'),
                "home_address" => $request->get('home_address'),
                // "chat_id" => $request->get('chat_id'),
                "modifyby" => $userTMS->info->userId
            ];
            $res['data'] = $moduleModel->postCreateUser($dataStatus);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    function editUser(Vtiger_Request $request) {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsAgentMonitoring_Module_Model();
            $dataStatus = [
                "user_type" => $request->get('user_type'),
                "fullname" => $request->get('fullname'),
                "email" => $request->get('email'),
                "phone" => $request->get('phone'),
                "birthday" => $request->get('birthday'),
                "user_lock" => $request->get('user_lock'),
                "user_id" => $request->get('user_id'),
                "home_phone_1" => $request->get('home_phone_1'),
                "home_phone_2" => $request->get('home_phone_2'),
                "personal_phone_1" => $request->get('personal_phone_1'),
                "personal_phone_2" => $request->get('personal_phone_2'),
                "work_mail" => $request->get('work_mail'),
                "personal_mail" => $request->get('personal_mail'),
                "home_address" => $request->get('home_address'),
                "chat_id" => $request->get('chat_id')
            ];
            $res['data'] = $moduleModel->putEditUser($dataStatus);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    function deactiveUser(Vtiger_Request $request) {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsAgentMonitoring_Module_Model();
            $UserID = $request->get('userID');
            $res['data'] = $moduleModel->postDeactiveUser($UserID);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    function activeUser(Vtiger_Request $request) {
        $res = array('success' => true);
        try {
            $moduleModel = new BhsAgentMonitoring_Module_Model();
            $UserID = $request->get('userID');
            $res['data'] = $moduleModel->postActiveUser($UserID);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    public function deleteUser(Vtiger_Request $request)
    {
        $res = array('success' => true);
        $id = $request->get('userID');
        $moduleModel = new BhsAgentMonitoring_Module_Model();
        try {
            $res['data'] = $moduleModel->deleteUser($id);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    public function getQuota(Vtiger_Request $request)
    {
        $res = [
            'success' => true,
            'data' => ''
        ];
        $moduleModel = new BhsAgentMonitoring_Module_Model();
        try {
            $res['data'] = $moduleModel->getQuota();
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    public function getUserType(Vtiger_Request $request)
    {
        $res = [
            'success' => true,
            'data' => ''
        ];
        $moduleModel = new BhsAgentMonitoring_Module_Model();
        try {
            $res['data'] = $moduleModel->getUserType();
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
    public function getResetPassword(Vtiger_Request $request)
    {
        $res = [
            'success' => true,
            'data' => ''
        ];
        $ID = $request->get('user_id');
        $moduleModel = new BhsAgentMonitoring_Module_Model();
        try {
            $res['data'] = $moduleModel->getResetPassword($ID);
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }
}
