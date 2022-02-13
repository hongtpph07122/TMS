<?php

class BhsProduct_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $this->setFilters(array(
            array(
                'name' => 'prodId',
                'datatype' => 'number',
            ),
            array(
                'name' => 'orgId',
                'datatype' => 'number',
            ),
            array(
                'name' => 'code',
                'datatype' => 'text',
            ),
            array(
                'name' => 'category',
                'datatype' => 'text',
            ),
            array(
                'name' => 'name',
                'datatype' => 'text',
            ),
            array(
                'name' => 'status',
                'datatype' => 'select',
                'data'     => $this->getStatusList(),
            ),
        ));
    }

    public function getStatusList() {
        return [
            (object) [
                'name'  => 'ACTIVE',
                'id'    => '1',
                'type'  => 'Product status',
                'value' => '1',
            ],
            (object) [
                'name'  => 'INACTIVE',
                'id'    => '0',
                'type'  => 'Product status',
                'value' => '0',
            ],
        ];
    }

    public function deleteRecordById($id) {
        $functions = 'products/' . $id;
        $request['headers']['Content-Type'] = 'application/json';
        return $this->makeApiCall('DELETE', $functions, $request);
    }

    public function getAllProductsActive(){
        $request['query'] = [
            'status' => 1
        ];
        $apiResult = $this->makeApiCall('GET', 'products', $request);
        $rows = $this->getContentsFromApiResult($apiResult);
        return $rows;
    }

    public function getAllProducts(){
        $apiResult = $this->makeApiCall('GET', 'products');
        $rows = $this->getContentsFromApiResult($apiResult);
        return $rows;
    }

    public function saveRecord($params){
        $method = 'POST';
        if ($params['prodId'] > 0){
            $method = 'PUT';
        }
        $request = array();
        $request['json'] = $params;
        $request['headers']['Content-Type'] = 'application/json';
        $res = $this->makeApiCall($method, 'products', $request);
    }

    public function getFirstRecord(){
        $res = $this->makeApiCall('GET', 'products');
        $this->firstRecord = $res;
        if(! $this->firstRecord['body']->headers) {
            $cols = [
                'prodId',
                //'orgId',
                'code',
                'category',
                'name',
                'price',
                'status',
            ];
            $this->firstRecord['body']->headers = $cols;
        }
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult();
        $columnDefs[] = array('data' => 'bhs_action', 'targets' => 6);
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array()
        );
        $columnDefs[] = array(
            'bSortable' => false,
            'targets' => array(6)
        );
        return $columnDefs;
    }
}