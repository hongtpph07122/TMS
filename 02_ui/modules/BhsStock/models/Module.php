<?php

class BhsStock_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        // $this->setFilters(array(
        //     array(
        //         'name' => 'trackingCode',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'soId',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'phonenumber',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'status',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'address',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'createdate',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'CustomerName',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'AGENCY',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'prodId',
        //         'datatype' => 'text',
        //     ),
        // ));
    }

    public function getFirstRecord(){
        // $request = $this->buildDummyRequest();
        // $res = $this->makeApiCall('GET', 'DO', $request);

        // $this->firstRecord = $res;
        if(! $this->firstRecord['body']->headers) {
            $cols = ['partnerId', 'name','partnerName',
            'quantityAvailable',
            'quantityTotal'         
        ];
            $this->firstRecord['body']->headers = $cols;
        }
       
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        
         $columnDefs = $this->getColumnDefsFromApiResult();
     //  $columnDefs[] = array('data' => 'bhs_action', 'targets' => 9);
        // $columnDefs[] = array(
        //     'className' => 'text-left',
        //     'targets' => array(1,2,5)
        // );
          // "columnDefs": [
            //     // { "bSortable": false, "aTargets": [ 0, 6 ] },
            //     { className: "text-left", "targets": [ 1, 2, 5 ] },
            //     // { className: "text-right", "targets": [ 6 ] },
            //     { "width": "10%", "targets": [0, 3] },
            //     { "width": "15%", "targets": [1, 4] },
            //     { "width": "20%", "targets": [2] },
            //     { "width": "30%", "targets": [5] },
            // ]
        // $columnDefs[] = array(
        //     'bSortable' => false,
            
        // );
        return $columnDefs;
    }
    public function cancelOrder($id) {
        $func = 'DO/cancel/' .  $id ;
        $res = $this->makeApiCall('PUT', $func, $request);
    }
    public function updateOrder($params) {
        $campaign = [
            "createby"=>  $params->get('createby'),
            "createdate"=> $params->get('createdate'),
            "customerId"=> $params->get('customerId'),
            "doId"=> $params->get('doId'),
            "modifyby"=> $params->get('modifyby'),
            "modifydate"=> $params->get('modifydate'),
            "partnerId"=> $params->get('partnerId'),
            "prodId"=> $params->get('prodId'),
            "soId"=> $params->get('soId'),
            "status"=> $params->get('status'),
            "trackingCode"=> $params->get('trackingCode')
        ];
       ;
        $request['json'] = $campaign;
        $request['headers']['Content-Type'] = 'application/json';
        $res = $this->makeApiCall('PUT', 'DO', $request);

        return $res;
    }
    
}