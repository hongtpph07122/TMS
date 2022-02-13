<?php

class BhsCustomer_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $this->setFilters(array(
            array(
                'name' => 'prod_name',
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
            array(
                'name' => 'address',
                'datatype' => 'text',
            ),
            array(
                'name' => 'province',
                'datatype' => 'text',
            ),
            array(
                'name' => 'district',
                'datatype' => 'text',
            ),
        ));
    }

    public function getFirstRecord(){
        $request = $this->buildDummyRequest();
        $res = $this->makeApiCall('POST', 'customer', $request);
        $this->firstRecord = $res;
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult(1);
        $columnDefs[] = array('data' => 'bhs_check_item', 'targets' => 0);
        $columnDefs[] = array('width' => '20%', 'targets' => array(6));
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array(6)
        );
        $columnDefs[] = array(
            'orderable' => false,
            'targets' => 0
        );
        return $columnDefs;
    }
}