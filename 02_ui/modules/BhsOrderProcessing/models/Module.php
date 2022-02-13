<?php

class BhsOrderProcessing_Module_Model extends Vtiger_BhsModule_Model {

    public $roleAgent;
    private $noriCustomerOrgId;
    public $userTMS;
    private $notAccessSource;
    public function __construct(){
        parent::__construct();
        global $ROLE_AGENT,
               $NORI_CUSTOMER_ORGID;
        $this->roleAgent = $ROLE_AGENT;
        $this->noriCustomerOrgId = $NORI_CUSTOMER_ORGID;
        $this->userTMS = Vtiger_Session::get('agent_info');
        $this->notAccessSource = !empty($this->userTMS) && $this->userTMS->roleId[0] == $this->roleAgent && $this->userTMS->info->orgId == $this->noriCustomerOrgId;
        $this->setFilters(array(
            array(
                'name' => 'leadId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'name',
                'datatype' => 'text',
            ),
            array(
                'name' => 'prodName',
                'datatype' => 'text',
            ),
            array(
                'name' => 'leadStatusName',
                'datatype' => 'text',
            ),
            array(
                'name' => 'ccCode',
                'datatype' => 'text',
            ),
            array(
                'name' => 'address',
                'datatype' => 'text',
            ),
        ));
    }

    public function callToCustomer($phone, $leadId){
        $res = $this->makeApiCall('GET', 'phone/' . $phone.'?leadId='.$leadId);
        return $res;
    }

    public function hangupToCustomer($ext){
        $res = $this->makeApiCall('GET', 'phone/hangup/' . $ext);
        return $res;
    }

    public function setCallbackSchedule($data){
        $request = array();
        $request['json'] = $data;

        $apiMethod = 'POST';
        $function = 'callback/create';
        $this->logApi('Request', $function, $request, $apiMethod);
        $res = $this->makeApiCall($apiMethod, $function, $request);
        $this->logApi('Response', $function, $res, $apiMethod);

        return $res['body'];
    }

    public function createOrder($data){

        $request = array();
        $request['json'] = $data;

        $apiMethod = 'POST';
        $function = 'SO/v2';
        $this->logApi('Request', $function, $request, $apiMethod);
        $res = $this->makeApiCall($apiMethod, $function, $request);
        $this->logApi('Response', $function, $res, $apiMethod);

        return $res['body'];
    }

    public function updateOrder($data){
        $request = array();
        $request['json'] = $data;

        $apiMethod = 'PUT';
        $function = 'SO/updateSO';
        $this->logApi('Request', $function, $request, $apiMethod);
        $res = $this->makeApiCall($apiMethod, $function, $request);
        $this->logApi('Response', $function, $res, $apiMethod);

        return  $res['body'];
    }

    public function saveLeadInfo($data ) {
        $request = array();
        $request['json'] = $data;

        $apiMethod = 'PUT';
        $function = 'SO/saveLeadInfo';
        $this->logApi('Request', $function, $request, $apiMethod);
        $res = $this->makeApiCall($apiMethod, $function, $request);
        $this->logApi('Response', $function, $res, $apiMethod);
        return $res['body'];
    }

    public function getFirstRecord(){
        $this->firstRecord = array();

        if(! $this->firstRecord['body']->headers) {
            $cols = [
                'checkbox',
                'leadId',
                'name',
                'prodName',
                'leadStatusName',
                'ccCode',
                // 'assigned',
                'address',
                'phone',
                'amount',
                'source',
                "agentName",
                "createdate",
                "modifydate",
                "totalCall"
            ];
            if ($this->notAccessSource) {
                unset($cols[9]);
            }
            $this->firstRecord['body']->headers = $cols;
        }
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult();
        $columnDefs[] = array(
            'className' => 'text-right pr-4',
            'targets' => array(5, 8),
        );
        $columnDefs[] = array(
            'width' => '200px',
            'targets' => array(3,7,8,9,10)
        );
        // $columnDefs[] = array(
        //     'className' => 'overflow-hidden',
        //     'targets' => array(6)
        // );
        return $columnDefs;
    }

    public function getListStock(){
        $stocks = array();
        $res = $this->makeApiCall('GET', 'stock/orderStock');
        if ($res['code'] == 200){
            $body = $res['body'];
            $stocks = $body->data;
        }
        return $stocks;
    }
    public function getSOProduct(){
        $stocks = array();
        $res = $this->makeApiCall('GET', 'products/StockByProduct');
        if ($res['code'] == 200){
            $body = $res['body'];
            $stocks = $body->data;
        }
        return $stocks;
    }

    public function getLeadStatus(){
        $leadStatus = array();
        $res = $this->makeApiCall('GET', 'synonym/lead');
        if ($res['code'] == 200){
            $body = $res['body'];
            $status = $body->data;
        }
        foreach($status as $st) {
            $leadStatus[$st->name] = $st->id;
        }

        return $leadStatus;
    }

    public function getListPaymentMethod(){
        $paymentMethod = array();
        $res = $this->makeApiCall('GET', 'synonym/payment');
        if ($res['code'] == 200){
            $body = $res['body'];
            $paymentMethod = $body->data;
        }
        return $paymentMethod;
    }

    public function leadStatusText() {
        $leadStatusText = [];
        $rejectReasons = $this->makeApiCall('GET', 'synonym/reject')['body']->data;
        $trashReasons = $this->makeApiCall('GET', 'synonym/trash')['body']->data;
        foreach ($rejectReasons as $rejectReason) {
            $leadStatusText['rejected'][] = $rejectReason->name;
        }
        foreach ($trashReasons as $trashReason) {
            $leadStatusText['trash'][] = $trashReason->name;
        }
        return $leadStatusText;
    }

    public function getClassStatusLead($status) {
        //die('$status: ' . $status);
        $class = '';
        if($status == 'new') {
            $class = 'btnYellor';
        }else if($status == 'rejected') {
            $class = 'btnOrage';
        }else if( in_array($status, ['callback propect', 'callback consult']) ) {
            $class = 'btnblue';
        }else if( in_array($status, ['unreachable', 'busy', 'noanswer']) ) {
            $class = 'btnViolet';
        }else if( in_array($status, ['invalid', 'duplicated', 'trash']) ) {
            $class = 'btnblack';
        }else {
            $class = 'btnGreen';
        }

        return $class;
    }

    public function getClassStatusSo($status) {

        if ($status == 'new') {
            $class = 'btnYellor';
        } else if ($status == 'cancel') {
            $class = 'btnOrage';
        } else if ($status == 'unassigned') {
            $class = 'btnblue';
        } else if ($status == 'pending') {
            $class = 'btnViolet';
        } else {
            $class = 'btnGreen';
        }

        return $class;
    }

    public function getCampaignList() {
        $res = $this->makeApiCall('GET', 'campaign',array());
        return $res['body']->data;
    }
    public function getAgent($cpid) {
        $res = $this->makeApiCall('GET', 'campaign/config/'. $cpid);
        return $res['body']->data->agentGroupIdList;
    }

}
