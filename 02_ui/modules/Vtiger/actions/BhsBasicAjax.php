<?php
/*+***********************************************************************************
 * The contents of this file are subject to the vtiger CRM Public License Version 1.0
 * ("License"); You may not use this file except in compliance with the License
 * The Original Code is:  vtiger CRM Open Source
 * The Initial Developer of the Original Code is vtiger.
 * Portions created by vtiger are Copyright (C) vtiger.
 * All Rights Reserved.
 *************************************************************************************/

class Vtiger_BhsBasicAjax_Action extends Vtiger_BasicAjax_Action {

    function checkPermission(Vtiger_Request $request) {
        return;
    }

    function customEmit($data){
        header('Content-type: text/json; charset=UTF-8');
        echo Zend_Json::encode($data);
    }

    function __construct() {
        parent::__construct();
        $this->exposeMethod('getList');
    }

    function process(Vtiger_Request $request) {
        $mode = $request->get('mode');
        if(!empty($mode)) {
            $this->invokeExposedMethod($mode, $request);
            return;
        }
    }

    function createActionButtonsOnList($id){
        $deleteBtn = '<a class="btnBhsDelete btnBhsAction" href="javascript: void(0);" title="Delete"  data-id="' . $id . '"><i class="fa fa-trash"> Delete</i></a>';
        $updateBtn = '<a class="btnBhsUpdate btnBhsAction" href="javascript: void(0);" title="Edit"  data-id="' . $id . '"><i class="fa fa-pencil"></i> Edit</a>';
        return $updateBtn .' '. $deleteBtn;
    }
    function createActionStartStop($id){
        $startBtn = '<a class="btnBhsStart btnBhsAction" href="javascript: void(0);" title="Play" data-id="' . $id . '"><i class="fa fa-play"></i></a>';
        $stopBtn = '<a class="btnBhsStop btnBhsAction" href="javascript: void(0);" title="Stop"  data-id="' . $id . '"><i class="fa fa-stop"></i></a>';
        $pauseBtn = '<a class="btnBhsPause btnBhsAction" href="javascript: void(0);" title="Pause"  data-id="' . $id . '"><i class="fa fa-pause"></i></a>';
       
        return $startBtn .' '. $stopBtn.' '. $pauseBtn;
    }
    function createActionCallBackOnList($id){
        $userTMS = Vtiger_Session::get('agent_info');
        $roleId = $userTMS->roleId[0];
//        $bhModule = new Vtiger_BhsModule_Model();
//        $roleList = $bhModule->roleList();
        if($roleId == 16){
            $deleteBtn = '<a style="font-size: 20px;" class="btnBhsCallBackEdit" href="javascript: void(0);" data-id="' . $id . '"><i class="fa fa-pencil"></i></a>';
        } else {
            $deleteBtn = '<a style="font-size: 25px;" class="btnBhsCallBack" href="javascript: void(0);" data-id="' . $id . '"><i class="fa fa-phone"></i></a>';
        }
        return $deleteBtn ;
    }


    function getList(Vtiger_Request $request){
        //@Todo
    }
}
