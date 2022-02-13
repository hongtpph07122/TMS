<?php

class BhsCampaignReport_Module_Model extends Vtiger_BhsModule_Model {

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

    public function getColumnDefs_PhoneNotContacted(){
        $columnDefs = [];
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array(0)
        );
        $columnDefs[] = array(
            'orderable' => false,
            'targets' => [0, 1, 2, 3, 4]
        );
        return $columnDefs;
    }

    public function getColumnDefs_PhoneContacted(){
        $columnDefs = [];
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array(0)
        );
        $columnDefs[] = array(
            'orderable' => false,
            'targets' => [0, 1, 2, 3, 4]
        );
        return $columnDefs;
    }
}