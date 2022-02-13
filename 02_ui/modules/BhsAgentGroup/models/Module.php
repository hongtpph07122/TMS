<?php

class BhsAgentGroup_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $this->setFilters(array(
            array(
                'name' => 'userId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'userType',
                'datatype' => 'select',
                'data' =>$this->getUserType()
            ),
            array(
                'name' => 'userLock',
                'datatype' => 'text',
            ),
            array(
                'name' => 'fullname',
                'datatype' => 'text',
            ),
            array(
                'name' => 'phone',
                'datatype' => 'text',
            ),
            array(
                'name' => 'email',
                'datatype' => 'text',
            ),
            array(
                'name' => 'personalPhone1',
                'datatype' => 'text',
            ),
            array(
                'name' => 'homeAddress',
                'datatype' => 'text',
            ),
            array(
                'name' => 'birthday',
                'datatype' => 'daterange',
            ),            
            array(
                'name' => 'modifyby',
                'datatype' => 'text',
            ),
            array(
                'name' => 'modifydate',
                'datatype' => 'daterange',
            ),
        ));
    }

    public function getFirstRecord(){
        // $request = $this->buildDummyRequest();
        // $res = $this->makeApiCall('GET', 'agent', $request);

        // $this->firstRecord = $res;        
        if(! $this->firstRecord['body']->headers) {
            $cols = [
                'userId',
                'userType',
                'fullname',
                'personalPhone1',
                'email',
                'homeAddress',
                'phone',
                'birthday',
                'modifyby',
                'modifydate',
                'password'
            ];
            $this->firstRecord['body']->headers = $cols;
        }
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult();
        $columnDefs[] = array('data' => 'bhs_action', 'targets' => 11);
        $columnDefs[] = array('data' => 'password', 'targets' => 10);
        $columnDefs[] = array( 'orderable' => false, 'targets' => 0 );
        $columnDefs[] = array(
            'className' => 'text-left',
            'targets' => array()
        );
        return $columnDefs;
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

            $dateFieldsFilter = ['modifydate','birthday'];

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
                    }
                     else {
                        $form_params[$column['name']] = $column['search']['value'];
                    }

                }
            }
            $request2['query'] = $form_params;
            $apiResult = $this->makeApiCall('GET', $function, $request2);
            $apiResult['body']->recordsTotal = $apiResult['body']->total;
            $apiResult['body']->recordsFiltered = $apiResult['body']->total;
            $this->getTotalFromApiResult($apiResult);
            return $this->getContentsFromApiResult($apiResult);
        } catch (Exception $exception){
            return array();
        }
    }
    public function getAgentGroup() {
        $res = $this->makeApiCall("GET", 'agentgroup');
        $data = [];
        if ($res['code'] == 200) {
            $body = $res['body'];
            $data = $body->data;
        }

        return $data;
    }

    public function getListAgent() {
        $res = $this->makeApiCall("GET", 'agentgroup/getListAgent');
        $data = [];
        if ($res['code'] == 200) {
            $body = $res['body'];
            $data = $body->data;
        }

        return $data;
    }

    public function getAgentByGroup($groupId) {
        $res = $this->makeApiCall("GET", 'agentgroup/'.$groupId);
        $data = [];
        if ($res['code'] == 200) {
            $body = $res['body'];
            $data = $body->data;
        }

        return $data;
    }

    public function getAgent() {
        $res = $this->makeApiCall("GET", 'agent');
        $data = [];
        if ($res['code'] == 200) {
            $body = $res['body'];
            $data = $body->data;
        }

        return $data;
    }

    public function getUserType() {
        $res = $this->makeApiCall("GET", 'agent/userType');
        if($res['code'] == 200) {
            // for($i = 0; $i < $res['body']->data->length; $i++)
            // {
            //     $res['body']->data[$i]->id = $res['body']->data[$i]->name;
            // }
            return $this->setIdequalName($res['body']->data);
        }
        
        return [];
    }

    public function setIdequalName($data){
        foreach($data as $status){
            $status->id=$status->name;
        }
        return $data;
    }
    public function getQuota() {
        $res = $this->makeApiCall('GET', 'agent/quota');
        if($res['code'] == 200) {
            return $res['body'];
        }
        
        return [];
    }

    public function getUserByID($ID) {
        $res = $this->makeApiCall('GET', 'agent/' . $ID);
        if($res['code'] == 200) {
            return $res['body']->data;
        }
        return [];
    }
}