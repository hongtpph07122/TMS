<?php

class BhsShippingPending_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $this->setFilters(array(
            array(
                'name' => 'soId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'customerName',
                'datatype' => 'text',
            ),
            array(
                'name' => 'customerPhone',
                'datatype' => 'text',
            ),
            array(
                'name' => 'product',
                'datatype' => 'text',
            ),
            array(
                'name' => 'source',
                'datatype' => 'text',
            ),
            array(
                'name' => 'agent',
                'datatype' => 'text',
            ),
            array(
                'name' => 'address',
                'datatype' => 'text',
            ),
            array(
                'name' => 'fulfillment',
                'datatype' => 'text',
            ),
            array(
                'name' => 'carrier',
                'datatype' => 'text',
            ),
            array(
                'name' => 'errorMessage',
                'datatype' => 'text',
            ),
            array(
                'name' => 'soUpdatedate',
                'datatype' => 'daterange',
            ),
        ));
    }

    public function getFirstRecord(){
        $res = $this->makeApiCall('GET', 'DO/pending');
        $this->firstRecord = $res;
        if(! $this->firstRecord['body']->headers) {
            $cols = [
                'checkbox',
                'soId',
                'soIdkw',
                'customerName',
                'customerPhone',
                'product',
                'amount',
                'source',
                'agent',
                'address',
                'fulfillment',
                'carrier',
                'errorMessage',
                'soUpdatedate',
            ];
            $this->firstRecord['body']->headers = $cols;
        }
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult(0);
        //$columnDefs[] = array('data' => 'bhs_check_item', 'targets' => 0);
       // $columnDefs[] = array( 'orderable' => false, 'targets' => 0 );
        $columnDefs[] = array(
            'width' => '15%',
            'targets' => array(3,4,5,7)
        );
        $columnDefs[] = array(
            'width' => '30%',
            'targets' => array(9)
        );
        $columnDefs[] = array(
            'orderable' => false,
            'targets' => array(0)
        );
        $columnDefs[] = array(
            'className' => 'text-right pr-4',
            'targets' => array(6),
        );
        return $columnDefs;
    }

}