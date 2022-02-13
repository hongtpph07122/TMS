<?php

class BhsCallbackManagement_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $this->setFilters(array(
            array(
                'name' => 'leadId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'callbackTime',
                'datatype' => 'daterange',
            ),
            array(
                'name' => 'requestTime',
                'datatype' => 'daterange',
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
                'name' => 'owner',
                'datatype' => 'text',
            ),
            array(
                'name' => 'assignedName',
                'datatype' => 'text',
            ),
            array(
                'name' => 'createdate',
                'datatype' => 'daterange',
            ),
        ));
    }

    public function getFirstRecord(){
        if(! $this->firstRecord['body']->headers) {
            $cols = ['leadId', 'callbackTime',
                'requestTime',
                'name',
                'phone',
                'owner',
                'assignedName',
                'createdate',
            ];
            $this->firstRecord['body']->headers = $cols;
        }
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult(1);
        $columnDefs[] = array('data' => 'bhs_check_item', 'targets' => 0);
        $columnDefs[] = array( 'orderable' => false, 'targets' => 0 );
        // $columnDefs[] = array(
        //     'className' => 'text-left',
        //     'targets' => array(5)
        // );
        return $columnDefs;
    }
}