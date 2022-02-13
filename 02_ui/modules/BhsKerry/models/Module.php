<?php

class BhsKerry_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $this->setFilters(array(
            array(
                'name' => 'trackingCode',
                'datatype' => 'text',
            ),
            array(
                'name' => 'createdate',
                'datatype' => 'daterange',
            ),
        ));
    }

    public function getFirstRecord(){
        if(!$this->firstRecord['body']->headers) {
            $cols = [
                'checkbox',
                'doCode',
                'ffmCode',
                'trackingCode',
                'warehouseName',
                'carrierName',
                'customerName',
                'customerPhone',
                'customerAddress',
                'packageName',
                'amountcod',
                'statusName',
                'createdate',
                'bhs_action'
            ];
            $this->firstRecord['body']->headers = $cols;
        }
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        
        $columnDefs = $this->getColumnDefsFromApiResult();
        $columnDefs[] = array(
            'width' => '10%',
            'targets' => array(4,5,6,8,11)
        );
        $columnDefs[] = array(
            'orderable' => false,
            'targets' => array(0,1)
        );

        return $columnDefs;
    }
    public function getOrders($params) {

        $doData = [
            'doIds' => $params->get('doIds'),
            'status' => 52 // status 52: ready to pick
        ];
        $request['json'] = $doData;
        $request['headers']['Content-Type'] = 'application/json';
        $res = $this->makeApiCall('PUT', 'DO/update/status', $request);

        return $res;
    }
}