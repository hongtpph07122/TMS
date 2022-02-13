<?php

class BhsShipping_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
       $data=$this->getStatusList();
        $this->setFilters(array(
            array(
                'name' => 'trackingCode',
                'datatype' => 'text',
            ),array(
                'name' => 'ffmCode',
                'datatype' => 'text',
            ),
            array(
                'name' => 'customerId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'soId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'doId',
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
                'name' => 'customerAddress',
                'datatype' => 'text',
            ),
            array(
                'name' => 'packageName',
                'datatype' => 'text',
            ),
            array(
                'name' => 'AGENCY',
                'datatype' => 'text',
            ),
            array(
                'name' => 'status',
                'datatype' => 'select',
                 'data' =>$data['delivery']
            ),
            array(
                'name' => 'warehouseName',
                'datatype' => 'text',
            ),
            array(
                'name' => 'createdate',
                'datatype' => 'daterange',
            ),
            array(
                'name' => 'carrierName',
                'datatype' => 'text',
            ),
        ));
    }

    public function getFirstRecord(){
        $request = $this->buildDummyRequest();
        $res = $this->makeApiCall('GET', 'DO', $request);

        $this->firstRecord = $res;
        if(! $this->firstRecord['body']->headers) {
            $cols = [
                'checkbox',
                'trackingCode',
                'customerId',
                'soId',
                'doId',
                'customerName',
                'customerPhone',
                'customerAddress',
                //'prodId',
                'packageName',
                'agency',
                'statusSO',
                'status',
                'warehouseName',
                'carrierName',
                'createdate',
                'bhs_action',
        ];
            $this->firstRecord['body']->headers = $cols;
        }
       
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        
        $columnDefs = $this->getColumnDefsFromApiResult();
        $columnDefs[] = array('data' => 'bhs_action', 'targets' => 9);
        $columnDefs[] = array(
        'width' => '10%',
        'targets' => array(4,5,6,8)
    );
    $columnDefs[] = array(
        'orderable' => false,
        'targets' => array(0,9)
    );
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
        return $this->makeApiCall('PUT', $func);
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
    public function getProducts($id)
    {
        try {
            $data = array();
            $leads = [];
                $res2 = $this->makeApiCall('GET', 'SO/SaleOrderItems/' . $id);
                $leads = $res2['body']->data;
            $data['amount']=0;    
            foreach ($leads as $lead) {  
                $price = $lead->price;
                $price = explode('|', $price);
                $price = $price[0];
                $price = CurrencyField::convertToUserFormat($price, null, true);
                $data['amount']+=$lead->prodAmount;
                $data['products'][] = array(
                    $lead->prodName,
                    $lead->quantity,
                    $price,
                    CurrencyField::convertToUserFormat($lead->prodAmount, null, true),
                );
            }
            $data['amount']=CurrencyField::convertToUserFormat($data['amount'], null, true);
            return  $data;
        } catch (Exception $exception) {
            return  $data;
        }
        
    }
    public function orderDetail($params) {
        $res = $this->makeApiCall('GET', 'DO/'. $params, $request)['body']->data;
        return $res;
    }
    public function getCustomer($params,$type=null) {
        if($type){
            $res = $this->makeApiCall('GET', 'DO/pending?doId='. $params, $request)['body']->data[0]->customer;
        }else{
            $res = $this->makeApiCall('GET', 'DO/?doId='. $params, $request)['body']->data[0]->customer;
        }
        return $res;
    }
    public function getStocks($params)
    {
         $ids=explode(',', $params);
        try {
            $data = array();
            foreach ($ids as $id) {
                $row = $this->makeApiCall('GET', 'products/StockByProduct?prodId='.$id,  $request)['body']->data[0];
                    $data[] = $row->partnerName;
            }
           return join(",",array_unique($data));
        } catch (Exception $exception) {
            return $data;
        }
    }
    public function getStatus($params)
    {
        try {
            $data = '';
            $rows = $this->makeApiCall('GET', 'synonym/delivery',  $request)['body']->data;
            foreach ($rows as $row) {
               if($row->id==$params){
                $data = $row->name;
                break;
               }    
            }
           return $data;
        } catch (Exception $exception) {
            return $data;
        }
    }
    public function getStatusList() {
        $data=array();
       
        $res = $this->makeApiCall('GET', 'synonym/lead');
        if($res['code'] == 200) {
            $data['lead']=$res['body']->data;
        }
        //
        $res = $this->makeApiCall('GET', 'synonym/delivery');
        if($res['code'] == 200) {
            $data['delivery']=$res['body']->data;
        }
        if (!empty($data)) {
           return $data;
       }
        return [];
    }
    public function validateDO($params) {
        $ids=$params->get('ids');
        $status=$params->get('status');
            $campaign = [
                'doIds' => $ids,
                'status' => $status 
            ];
            $request['json'] = $campaign;
            $request['headers']['Content-Type'] = 'application/json';
            $res = $this->makeApiCall('PUT', 'DO/update/status', $request);
        //return $res;
    }

    public function getToken() {
        $access_token = Vtiger_Session::get('access_token');
        return $access_token;
    }

    public function updateRescue()
    {
        $res = $this->makeApiCall('POST', 'DO/job-auto-update');
        return $res['body'];
    }
}