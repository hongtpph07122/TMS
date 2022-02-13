<?php

class BhsAgentReport_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
    }

    public function getFirstRecord(){
        $request = $this->buildDummyRequest();
        $res = $this->makeApiCall('POST', 'customer', $request);
        $this->firstRecord = $res;
    }

    public function getColumnDefs_ActivityDuration(){
        $columnDefs = [];
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array(0, 1)
        );
        return $columnDefs;
    }
    public function getColumnDefs_AgentLoginTime(){
        $columnDefs = [];
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array(0, 1)
        );
        return $columnDefs;
    }
}