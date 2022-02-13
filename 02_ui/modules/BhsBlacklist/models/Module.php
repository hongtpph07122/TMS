<?php

class BhsBlacklist_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $this->setFilters(array(
            array(
                'name' => 'lead_name',
                'datatype' => 'text',
            ),
            array(
                'name' => 'name',
                'datatype' => 'text',
            ),
            array(
                'name' => 'phone',
                'datatype' => 'text',
            ),
        ));
    }

    public function getFirstRecord(){
        $request = $this->buildDummyRequest();
        $res = $this->makeApiCall('POST', 'blacklist', $request);
        $this->firstRecord = $res;
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs =  $this->getColumnDefsFromApiResult();
        $columnDefs[] = array('data' => 'bhs_action', 'targets' => 7);
        $columnDefs = array(
            'className' => 'text-left',
            'targets' => array(5)
        );
        return $columnDefs;
    }
}