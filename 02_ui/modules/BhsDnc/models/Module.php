<?php

class BhsDnc_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $this->setFilters(array(
            array(
                'name' => 'name',
                'datatype' => 'text',
            ),
        ));
    }

    public function getFirstRecord(){
        $request = $this->buildDummyRequest();
        $res = $this->makeApiCall('POST', 'dnc_list', $request);
        $this->firstRecord = $res;
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult();
        $columnDefs[] = array('data' => 'bhs_action', 'targets' => 7);
        $columnDefs[] = array(
            'bSortable' => false,
            'targets' => array(1)
        );
        return $columnDefs;
    }
}