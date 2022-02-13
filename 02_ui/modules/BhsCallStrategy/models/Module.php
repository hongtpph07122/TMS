<?php

class BhsCallStrategy_Module_Model extends Vtiger_BhsModule_Model
{

    public function __construct()
    {
        parent::__construct();
        // $this->setFilters(array(
        //     array(
        //         'name' => 'orgId',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'csId',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'name',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'shortname',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'dscr',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'modifyby',
        //         'datatype' => 'text',
        //     ),
        //     array(
        //         'name' => 'modifydate',
        //         'datatype' => 'text',
        //     ),
        // ));
    }

    public function getFirstRecord()
    {

        $request = $this->buildDummyRequest();
        //$res = $this->makeApiCall('GET', 'campaign/callstrategy/1', $request);
        $this->firstRecord = $res;
        if (!$this->firstRecord['body']->headers) {
            $cols = ['phonestatus',
                'attempts',
                'duration',
                'day',
            ];
            $this->firstRecord['body']->headers = $cols;
        }
    }

    public function getColumnDefs()
    {
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult();
        // $columnDefs[] = array('data' => 'bhs_check_item', 'targets' => 0);
        // $columnDefs[] = array('width' => '20%', 'targets' => array(6));
        // $columnDefs[] = array(
        //     'className' => 'text-left',
        //     'targets' => array(6)
        // );
        // $columnDefs[] = array(
        //     'orderable' => false,
        //     'targets' => 0
        // );
        return $columnDefs;
    }
    public function saveConfig($params)
    {
        $campaign = [
            'campaignId' => $params['campaignId'],
            'callingList' => $params['clGroupId'],
            'agentGroupIdList' => $params['orGroupIds'],
            'ruleId' => $params['ruleId'],
            'strategyId' => $params['strategyId'],
        ];
        $request['json'] = $campaign;
        $request['headers']['Content-Type'] = 'application/json';
        $res = $this->makeApiCall('PUT', 'campaign/config', $request);
        return $res;
    }
    public function getCallStrategy($id, $opts = array())
    {
        $districts = array();
        $res = $this->makeApiCall('GET', 'campaign/callstrategy/' . $id, array());
        if ($res['code'] == 200) {
            $body = $res['body'];
            $calls = $body->data;
        }
        $res2 = $this->makeApiCall('GET', 'synonym/call', array());
        $callStatus= $res2['body']->data;
        foreach($callStatus as $status) {
            $statusID[] = $status->id;
        }
        foreach($calls as $call) {
           if(in_array($call->callStatus,$statusID)){
            $indx=array_search($call->callStatus,$statusID);
            $call->callStatus=$callStatus[$indx]->name;
           }
        }
        return $calls;
    }
    public function saveCampaign($params)
    {
        $owner_ID =Vtiger_Session::get('agent_info')->info->userId;
        $orgId = Vtiger_Session::get('agent_info')->info->orgId;
        $orgId = $orgId ? $orgId : 1;
        $owner=$params->get('owner')?$params->get('owner'):$owner_ID;
        $id = intval($params->get('cpId'));
        $startdate = '';
        if($params->get('startdate') != '') {
            $startdate = new DateTime($params->get('startdate') );
            $startdate = $startdate->format('YmdHis');
        }
        $stopdate = '';
        if($params->get('stopdate')!= '') {
            $stopdate = new DateTime($params->get('stopdate') );
            $stopdate = $stopdate->format('YmdHis');
        }
        
        $status=$params->get('status')?$params->get('status'):36;
        $campaign = [
            'name' => $params->get('name'),
            'owner' => $owner,
            'status' => $status,
            'startdate' => $startdate ,
            'stopdate' => $stopdate,
            "orgId" => $orgId,
            'campaignid' => $id,
        ];
        $method = 'POST';
        if ($id != 0) {
            $method = 'PUT';
        }
        $request['json'] = $campaign;
        $request['headers']['Content-Type'] = 'application/json';
        //var_dump($request);
        $res = $this->makeApiCall($method, 'campaign', $request);
        $id = $res['body']->data;
        return  $id;
    }

}
