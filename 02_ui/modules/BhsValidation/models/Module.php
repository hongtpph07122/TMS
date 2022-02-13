<?php

class BhsValidation_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $data=$this->getStatusList();
        $this->setFilters(array(
            array(
                'name' => 'leadId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'soId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'leadPhone',
                'datatype' => 'text',
            ),
            array(
                'name' => 'createdate',
                'datatype' => 'daterange',
            ),
            array(
                'name' => 'modifyDate',
                'datatype' => 'daterange',
            ),
            array(
                'name' => 'leadName',
                'datatype' => 'text',
            ),
            array(
                'name' => 'assign',
                'datatype' => 'text',
            ),
            array(
                'name' => 'sourceName',
                'datatype' => 'text',
            ),
            array(
                'name' => 'agName',
                'datatype' => 'text',
            ),
            array(
                'name' => 'productCrosssell',
                'datatype' => 'text',
            ),
            array(
                'name' => 'totalprice',
                'datatype' => 'text',
            ),
            array(
                'name' => 'carrier',
                'datatype' => 'text',
            ),
            array(
                'name' => 'leadStatus',
                'datatype' => 'select',
                'data' =>$data['lead']
            ),
            array(
                'name' => 'orderStatus',
                'datatype' => 'select',
                 'data' =>$data['order']
            ),
            array(
                'name' => 'deliveryStatus',
                'datatype' => 'select',
                 'data' =>$data['delivery']
            ),
            array(
                'name' => 'paymentMethod',
                'datatype' => 'select',
                'data' =>$data['payment']
            ),
        ));
    }

    public function getFirstRecord(){
        if(! $this->firstRecord['body']->headers) {
            $cols = [
                'checkbox',
                'play',
                'leadId',
                'soId',
                'leadName',
                'leadPhone',
                //'productName',
                'productCrosssell',
                'totalprice',
                'sourceName',
                'agName',
                'createdate',
                'modifyDate',
                'comment',
                'address',
                'locationStatus',
                'lastmileName',
                'leadStatus',
                'orderStatus',
                'deliveryStatus',
                'paymentMethod',
            ];
            $this->firstRecord['body']->headers = $cols;
        }
       
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        
         $columnDefs = $this->getColumnDefsFromApiResult();
     //  $columnDefs[] = array('data' => 'bhs_action', 'targets' => 17);
        $columnDefs[] = array(
            'width' => '100px',
            'targets' => array(3,5,10,13)
        );
        $columnDefs[] = array(
            'width' => '500px',
            'targets' => array(11,12)
        );
        $columnDefs[] = array(
        'className' => 'text-right',
        'targets' => array(6, 7),
        );
        $columnDefs[] = array(
            'width' => '200px',
            'targets' =>  array(7)
        );
        return $columnDefs;
    }
    public function getStatus() {
        $func = 'synonym/orderValidate';
        $res = $this->makeApiCall('GET', $func, $request);
        return $res['body']->data;
    }
    public function validateOrder($params) {
      
        $campaign = [
            'soIds' => $params->get('ids'),
            'status' => $params->get('status')
        ];
        //var_dump($campaign);
        $request['json'] = $campaign;
        $request['headers']['Content-Type'] = 'application/json';
        //var_dump($request);
        $res = $this->makeApiCall('PUT', 'SO/Validate', $request);

        return $res;
    }
    public function setIdequalName($data){
        foreach($data as $status){
            $status->id=$status->name;
        }
        return $data;
    }
    public function getStatusList() {
        $data=array();
        $res = $this->makeApiCall('GET', 'synonym/lead');
        if($res['code'] == 200) {
            $data['lead']=$this->setIdequalName($res['body']->data);
        }
        //
        $res = $this->makeApiCall('GET', 'synonym/orderValidate?isfilter=1');
        if($res['code'] == 200) {
            $data['order']=$this->setIdequalName($res['body']->data);
        }
        //
        $res = $this->makeApiCall('GET', 'synonym/delivery');
        if($res['code'] == 200) {
            $data['delivery']=$this->setIdequalName($res['body']->data);
        }
        //payment
        $res = $this->makeApiCall('GET', 'synonym/payment');
        if($res['code'] == 200) {
            $data['payment']=$this->setIdequalName($res['body']->data);
        }
        if (!empty($data)) {
           return $data;
       }
        return [];
    }

}
