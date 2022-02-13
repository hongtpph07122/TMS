<?php

class BhsSearchOrders_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $this->setFilters(array(
            array(
                'name' => 'odId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'marketing_source',
                'datatype' => 'text',
            ),
            array(
                'name' => 'agent',
                'datatype' => 'text',
            ),
            array(
                'name' => 'createdate',
                'datatype' => 'text',
            ),
            array(
                'name' => 'status',
                'datatype' => 'text',
            ),
            array(
                'name' => 'comments',
                'datatype' => 'text',
            ),
        ));
    }

    public function getFirstRecord(){
        $request = $this->buildDummyRequest();
        $res = $this->makeApiCall('GET', 'SO', $request);
        $this->firstRecord = $res;
        if(! $this->firstRecord['body']->headers) {
            $cols = ['odId', 'marketing_source','agent',
            'status',
            'createdate',
            'comments',
            
        ];
            $this->firstRecord['body']->headers = $cols;
        }
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult();
        // $columnDefs[] = array(
        //     'className' => 'text-left',
        //     'targets' => array(6)
        // );
        return $columnDefs;
    }
}