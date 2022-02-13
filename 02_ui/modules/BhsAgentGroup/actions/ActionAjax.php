<?php
/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */
class BhsAgentGroup_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action {

    function __construct() {
        parent::__construct();
        $this->exposeMethod('saveRecord');
        $this->exposeMethod('deleteAgent');
        $this->exposeMethod('getLstAgentGroup');
        $this->exposeMethod('getGroup');
        $this->exposeMethod('getGroupDetail');
        $this->exposeMethod('getListAgent');
    }

    function getGroup(Vtiger_Request $request) {
        $modulModel = new BhsAgentGroup_Module_Model();
        $draw = intval($request->get('draw'));
        try {
            $agentGroup = $modulModel->makeApiCall("GET", 'agentgroup');
            $agentGroup = $agentGroup['body'];
            $res = array(
                'success' => true,
                'error' => '',
                'data' => $agentGroup,
            );
        }catch (Exception $exception) {
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

    function getLstAgentGroup(Vtiger_Request $request) {
        $modulModel = new BhsAgentGroup_Module_Model();
        $sFullname = $request->get('fullname');
        $grAgId = $request->get('grAgId');
        $isTransform = $request->get('isTransform', true);

        $build_query = [
            'offset' => $request->get('start', 0),
            'limit' => $request->get('length', 10)
        ];
        if (!empty($sFullname)) {
            $build_query['fullname'] = $sFullname;
        }
        if (!empty($grAgId)) {
            $build_query['grAgId'] = $grAgId;
        }
        $http_build_query = http_build_query($build_query);
        $draw = intval($request->get('draw'));
        try {
            $agentGroup = $modulModel->makeApiCall("GET", 'agentgroup/getListAgent?'.$http_build_query);
            $body = $agentGroup['body'];

            if (!empty($body->data)) {
                $data = $body->data;

                if ($isTransform != 'false') {
                    $data = array_map(function ($item) {
                        return [
                            '',
                            $item->grAgId,
                            $item->fullname,
                            vtranslate('LBL_NONE_GROUP', 'BhsAgentGroup'),
                            $item->agSkillLevel,
                            ''
                        ];
                    }, $data);
                }
            }

            $res = array(
                'success' => true,
                'error' => '',
                'data' => empty($data) ? [] : $data,
                'draw' => $draw,
                'recordsTotal' => $body->total?$body->total:0,
                'recordsFiltered' => $body->total?$body->total:0,
            );
        }catch (Exception $exception) {
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

    function getGroupDetail(Vtiger_Request $request) {
        $modulModel = new BhsAgentGroup_Module_Model();
        $groupId = $request->get('groupId');
        $sFullname = $request->get('fullname');
        $grAgId = $request->get('grAgId');
        $isTransform = $request->get('isTransform', true);

        $build_query = [
            'offset' => $request->get('start', 0),
            'limit' => $request->get('length', 10)
        ];
        if (!empty($sFullname)) {
            $build_query['fullname'] = $sFullname;
        }
        if (!empty($grAgId)) {
            $build_query['grAgId'] = $grAgId;
        }
        $http_build_query = http_build_query($build_query);
        $draw = intval($request->get('draw'));
        try {
            $agentGroup = $modulModel->makeApiCall("GET", 'agentgroup/'.$groupId.'/agent?'.$http_build_query);
            $body = $agentGroup['body'];

            if (!empty($body->data)) {
                $data = $body->data;

                if ($isTransform != 'false') {
                    $data = array_map(function ($item) {
                        return [
                            '<input type="hidden" class="btnHiddenGroupId" value="'.$item->groupId.'" />'.
                            '<input type="hidden" class="btnHiddenGrAgId" value="'.$item->grAgId.'" />',
                            $item->grAgId,
                            $item->fullname,
                            $item->name,
                            $item->agSkillLevel,
                            '<a class="btnInlineEdit btnBhsAction" href="javascript: void(0);" title="Edit"  data-id="' . $item->grAgId . '"><i class="fa fa-pencil"></i></a>
                            <a class="btnInlineRemove btnBhsAction" href="javascript: void(0);" title="Edit"  data-id="' . $item->grAgId . '"><i class="fa fa-trash"></i></a>'
                        ];
                    }, $data);
                }
            }

            $res = array(
                'success' => true,
                'error' => '',
                'data' => empty($data) ? [] : $data,
                'draw' => $draw,
                'recordsTotal' => $body->total,
                'recordsFiltered' => $body->total,

            );
        }catch (Exception $exception) {
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

    /**
     * Add New Agent
     */
    function saveRecord(Vtiger_Request $request) {
        $modulModel = new BhsAgentGroup_Module_Model();
        $oGrId = $request->get('oGrId');
        $grAgId = $request->get('grAgId');
        $draw = intval($request->get('draw'));
        $json = $this->dataTransformObject($request);
//        print_r("<pre>");
//        var_dump($json);
        try {
            $method = 'POST';
            if ($grAgId > 0){
                $method = 'PUT';
            }
            $agentGroup = $modulModel->makeApiCall($method, 'agentgroup/'.$oGrId.'/agent', [
                'json' => $json,
                'headers' => [
                    'Content-Type' => 'application/json'
                ]
            ]);

            $agentGroup = $agentGroup['body'];
            $res = array(
                'success' => true,
                'error' => '',
                'data' => empty($agentGroup) ? [] : $agentGroup,
            );
        }catch (Exception $exception) {
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

    /**
     * Delete Agent
     */
    function deleteAgent(Vtiger_Request $request) {
        $modulModel = new BhsAgentGroup_Module_Model();
        $groupId = $request->get('groupId');
        $draw = intval($request->get('draw'));
        $grAgId = $request->get('grAgId');
        try {
            $agentGroup = $modulModel->makeApiCall("DELETE", 'agentgroup/'.$groupId.'/agent?grAgId='.$grAgId);
            $agentGroup = $agentGroup['body'];
            $res = array(
                'success' => true,
                'error' => '',
                'data' => empty($agentGroup) ? [] : $agentGroup,
            );
        }catch (Exception $exception) {
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

    function getListAgent(Vtiger_Request $request) {
        $moduleModel = new BhsAgentGroup_Module_Model();
        $draw = $request->get('draw');
        try {
            $responseApi = $moduleModel->makeApiCall('GET', 'agentgroup/getListAgent');
            $responseApiBody = $responseApi['body'];

            $res = array(
                'success' => true,
                'error' => '',
                'data' => $responseApiBody
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

    protected function dataTransformObject(Vtiger_Request $request) {
        $keys = [
            'grAgId',
            'groupId',
            'agentId',
            'agSkillLevel'
        ];

        $input = [];
        foreach ($keys as $key) {
            $val = $request->get($key);
            if (isset($val) && $val != null) {
                $input[$key] = $val;
            }
        }

        return $input;
    }
}
