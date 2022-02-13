<?php
require 'api/guzzle-master/vendor/autoload.php';
class BhsOrders_Module_Model extends Vtiger_BhsModule_Model {
    private $roleAgent;
    private $noriCustomerOrgId;
    private $userTMS;
    public function __construct(){
        parent::__construct();
        global $ROLE_AGENT,
               $NORI_CUSTOMER_ORGID;
        $this->roleAgent = $ROLE_AGENT;
        $this->noriCustomerOrgId = $NORI_CUSTOMER_ORGID;
        $this->userTMS = Vtiger_Session::get('agent_info');
        $this->notAccessSource = !empty($this->userTMS) && $this->userTMS->roleId[0] == $this->roleAgent && $this->userTMS->info->orgId == $this->noriCustomerOrgId;
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
                 'name' => 'leadName',
                 'datatype' => 'text',
             ),
             array(
                 'name' => 'leadPhone',
                 'datatype' => 'text',
             ),
             array(
                 'name' => 'cpName',
                 'datatype' => 'text',
             ),

             array(
                 'name' => 'productName',
                 'datatype' => 'text',
             ),
             array(
                 'name' => 'productCrosssell',
                 'datatype' => 'text',
             ),
             array(
                 'name' => 'amount',
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
                 'name' => 'address',
                 'datatype' => 'text',
             ),
             array(
                 'name' => 'lastmileName',
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
                 'name' => 'createdate',
                 'datatype' => 'daterange',
             ),
             array(
                 'name' => 'modifyDate',
                 'datatype' => 'daterange',
             ),

             array(
                 'name' => 'totalCall',
                 'datatype' => 'text',
             ),
             array(
                 'name' => 'cpName',
                 'datatype' => 'text',
             ),
             array(
                 'name' => 'paymentMethod',
                  'datatype' => 'select',
                'data' =>$data['payment']
             ),
             array(
                 'name' => 'affiliateId',
                 'datatype' => 'text',
             ),

//             array(
//                 'name' => 'agentName',
//                 'datatype' => 'text',
//             ),
//             array(
//                 'name' => 'status',
//                 'datatype' => 'text',
//             ),
         ));
    }

    public function getFirstRecord(){
        $this->firstRecord = [];
        $cols = [
            'leadId',
            'soId',
            'leadName',
            'leadPhone',
            'productName',
            'productCrosssell',
            'affiliateId',
            'amount',
            'sourceName',
            'agName',
            'cpName',
            'address',
            'lastmileName',
            'comment',
            'leadStatus',
            'orderStatus',
            'deliveryStatus',
            'createdate',
            'modifyDate',
            'totalCall',
            'paymentMethod',
        ];
        if ($this->notAccessSource) {
            unset($cols[8]);
        }
        $this->firstRecord['body']->headers = $cols;
        //$this->firstRecord = $res;
    }

    public function getRowsRefactor(Vtiger_Request $request, $function, $needRemove){

        try {
            if (empty($needRemove)){
                $needRemove = array();
            }
            $request2 = array();
            //$request2['is_gridview'] = true;
            $form_params = $_REQUEST;
            $columns = $form_params['columns'];
            if (!empty($needRemove)) {
                $columns = $this->removeFirstColumns($columns, $needRemove);
            }

            $dateFieldsFilter = ['stopdate', 'startdate','createdate','modifyDate'];

            $form_params['columns'] = $columns;
            $form_params['draw'] = $request->get('draw');
            $form_params['columns'] = $request->get('columns');
            $form_params['order'] = $request->get('order');
            $form_params['search']['value'] = $request->get('keyword');
            $form_params['limit'] = $request->get('length');
            $form_params['offset'] = $request->get('start');
            foreach($columns as $column) {
                if(isset($column['search']['value']) && $column['search']['value'] != '')  {
                    if(in_array($column['name'], $dateFieldsFilter) && !empty($column['search']['value'])) {
                        $dateValue = explode('-',$column['search']['value'] );
                        $startDate = date('YmdHis',strtotime(str_replace('/','-',$dateValue[0])));
                        $endDate = date('YmdHis',strtotime(str_replace('/','-',$dateValue[1])));
                        $dateValue = $startDate.'|'.$endDate;
                        $form_params[$column['name']] = $dateValue;
                    } elseif ($column['name'] === 'leadStatus' && $column['search']['value'] === 'uncall') {
                        $form_params[$column['name']] = 'busy,noanswer,unreachable';
                    } else {
                        $form_params[$column['name']] = $column['search']['value'];
                    }

                }
            }
            $request2['query'] = $form_params;
            $apiResult = $this->makeApiCall('GET', $function, $request2);
            $this->logApi('Response', $function, $apiResult);
            $apiResult['body']->recordsTotal = $apiResult['body']->total;
            $apiResult['body']->recordsFiltered = $apiResult['body']->total;
            $this->getTotalFromApiResult($apiResult);
            return $this->getContentsFromApiResult($apiResult);
        } catch (Exception $exception){
            return array();
        }
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult(1);
        $columnDefs[] = array('data' => 'bhs_check_item', 'targets' => 0);
        $columnDefs[] = array(
            'width' => '8%',
            'targets' => array(3,4,5,12,11,13),
        );
        $columnDefs[] = array(
            'width' => '500px',
            'targets' => array(9)
        );
        $columnDefs[] = array(
            'className' => 'text-right',
            'targets' => array(6, 8),
        );
        return $columnDefs;
    }
    public function cancelOrder($params) {
        $func = 'SO/cancelSO/'.$params->get('id');
        $res = $this->makeApiCall('PUT', $func, $request);
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
            array_unshift($data['lead'],(object)[
                "name" => "uncall",
                "id" => "uncall",
                "type" => "lead status",
                "value" => "0"
            ]);
        }
        //
        $orderValidateQuery['query']= ['isfilter' => 1];
        $res = $this->makeApiCall('GET', 'synonym/orderValidate', $orderValidateQuery);
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
    public function getCampaignList() {
        $res = $this->makeApiCall('GET', 'campaign',array());
        return $res['body']->data;
    }
    public function getCallingList($cpid) {
        $res = $this->makeApiCall('GET', 'campaign/'. $cpid.'/callinglist');
        return $res['body']->data;
    }
    public function createOrder($data){
        $request = array();
        $request['json'] = $data;
        $res = $this->makeApiCall('POST', 'SO', $request);
        return $res['body'];
    }
    public function saveLeadInfo($data ) {
        $request = array();
        $request['json'] = $data;

        $apiMethod = 'POST';
        $function = 'lead';
        $this->logApi('Request', $function, $request, $apiMethod);
        $res = $this->makeApiCall($apiMethod, $function, $request);
        $this->logApi('Response', $function, $res, $apiMethod);

        return $res['body'];
    }

    public function uploadExcel( $data ) {
        //var_dump($data);die;
        $request = array();
        $request['headers']['Content-Type'] = 'multipart/form-data';
        $request['headers']['Accept'] = 'application/json';
        $request['form_params']['file'] = $data;
        // $request['multipart'] = [
        //     [
        //         'name'     => $data['uploadfile']['name'],
        //         'contents' => fopen($data['uploadfile']['tmp_name'], 'r'),//file_get_contents($data['uploadfile']['tmp_name']),
        //         'filename' => $data['uploadfile']['name']
        //     ],
        // ];
        $res = $this->makeApiCall('POST', 'lead/excel/upload', $request);
    //    // $gu = new \GuzzleHttp();
    //     $res = $gu->post('http://116.212.40.2:8083/api/v1/lead/excel/upload', [
    //         'headers' => ['Content-Type' => 'multipart/form-data;'],
    //         'json'    => ['field1' => 'value1', 'field2' => 'value2', 'field3' => 'value3',
    //             'file' => fopen($data['uploadfile']['tmp_name'], 'r'),]
    //     ]);
        return $res;
    }

    public function getToken() {
        $access_token = Vtiger_Session::get('access_token');
        return $access_token;
    }
}
