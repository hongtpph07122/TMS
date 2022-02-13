<?php

class BhsCampaignManagement_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $this->setFilters(array(
            array(
                'name' => 'name',
                'datatype' => 'text',
            ),
            array(
                'name' => 'startdate',
                'datatype' => 'date',
            ),
            array(
                'name' => 'stopdate',
                'datatype' => 'date',
            ),
            array(
                'name' => 'status',
                'datatype' => 'select',
                'data' =>$this->getStatusList()
            ),
            array(
                'name' => 'owner',
                'datatype' => 'text',
            ),
            array(
                'name' => 'progress',
                'datatype' => 'text',
            ),
            array(
                'name' => 'outboundagent',
                'datatype' => 'text',
            ),
        ));
    }

    public function getFirstRecord(){
        // $request = $this->buildDummyRequest();
        // $res = $this->makeApiCall('GET', 'campaign', $request);

        // $this->firstRecord = $res;
        if(! $this->firstRecord['body']->headers) {
            $cols = ['cpId', 'name',
            'startdate',
            'stopdate',
            'status',
        //    'owner'
            'progress',
            'outboundagent',
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
        $columnDefs[] = array('data' => 'bhs_action', 'targets' => 7);
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array(1)
        );
        $columnDefs[] = array(
            'bSortable' => false,
            'targets' => array(6)
        );
        return $columnDefs;
    }
    /**
     * Save campaign
     */
    public function saveCampaign($params) {
        //var_dump($params->get('name'));
        $startdate = new DateTime($params->get('startdate'));
        $startdate = $startdate->format('Ymd');
        $stopdate = new DateTime($params->get('stopdate'));
        $stopdate = $stopdate->format('Ymd');
        $campaign = [
            'name' => $params->get('name'),
            'owner' => $params->get('owner'),
            'status' => $params->get('status'),
            'startdate' =>$startdate,
            'stopdate' => $stopdate,
            "orgId" => 1,
        ];
        //var_dump($campaign);
        $request['json'] = $campaign;
        $request['headers']['Content-Type'] = 'application/json';
        //var_dump($request);
        $res = $this->makeApiCall('POST', 'campaign', $request);

        return $res;
    }
    /**
     * Update campaign
     */
    public function updateCampaign($params) {
        //var_dump($params->get('name'));
        $startdate = new DateTime($params->get('startdate'));
        $startdate = $startdate->format('Ymd');
        $stopdate = new DateTime($params->get('stopdate'));
        $stopdate = $stopdate->format('Ymd');
        $campaign = [
            'name' => $params->get('name'),
            'owner' => $params->get('owner'),
            'status' => $params->get('status'),
            'startdate' =>$startdate,
            'stopdate' => $stopdate,
            "orgId" => 1,
            'campaignid' =>$params->get('cpId')
        ];
        //var_dump($campaign);
        $request['json'] = $campaign;
        $request['headers']['Content-Type'] = 'application/json';
        //var_dump($request);
        $res = $this->makeApiCall('PUT', 'campaign', $request);

        return $res;
    }
     /**
     * Delete campaign
     */
   
    public function deleteCampaign($id) {
        $func = 'campaign/' .  $id ;
        $res = $this->makeApiCall('DELETE', $func, $request);
    }
    /**
     * Get Agent list
     */

    public function getAgents() {
        $res = $this->makeApiCall('GET', 'agent');
        if($res['code'] == 200) {
            return $res['body']->data;
        }
        
        return [];
    }
    public function getStatusList() {
        $res = $this->makeApiCall('GET', 'synonym/campaign');
        if($res['code'] == 200) {
            return $res['body']->data;
        }
        
        return [];
    }
    public function agentOptions() {
        $options = $this->getAgents();
        //var_dump($options);
        $agentOptions = [];
        if(count($options) > 0) {
            foreach ($options as $opt) {
                $agentOptions[$opt->userId] = $opt->fullname;
            }
        }
        //var_dump($agentOptions);
        return $agentOptions;
    }

    public function campaignStart($id) {
        $func = 'campaign/changeStatus/' .  $id .'/start';
        $res = $this->makeApiCall('PUT', $func, $request);
        return $res['body']->code;
    }

    public function campaignStop($id) {
        $func = 'campaign/changeStatus/' .  $id .'/stopped';
        $res = $this->makeApiCall('PUT', $func, $request);
    }
    public function campaignPause($id) {
        $func = 'campaign/changeStatus/' .  $id .'/pause';
        $res = $this->makeApiCall('PUT', $func, $request);
    }
    
}