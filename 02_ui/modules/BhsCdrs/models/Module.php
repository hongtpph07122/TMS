<?php

class BhsCdrs_Module_Model extends Vtiger_BhsModule_Model
{

    public function __construct()
    {
        parent::__construct();
        $data=$this->getStatusList();
        $this->setFilters(array(
            array(
                'name' => 'leadId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'callId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'userName',
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
                'name' => 'starttime',
                'datatype' => 'daterange',
            ),
            array(
                'name' => 'callStatusName',
                'datatype' => 'text',
//                'datatype' => 'select',
//                'data' =>$data['status']
            ),
        ));
    }

    public function getFirstRecord()
    {
        if (!$this->firstRecord['body']->headers) {
            $cols = [
                'checkbox',
                'play',
                'download',
                'callId',
                'channel',
                'userName',
                'leadId',
                'leadName',
                'leadPhone',
                'starttime',
                'duration',
                'callStatusName',
            ];
            $this->firstRecord['body']->headers = $cols;
        }
    }
    public function getStatusList() {
        $data=array();
        $res = $this->makeApiCall('GET', 'synonym/call');
        if($res['code'] == 200) {
            $data['status']=$res['body']->data;
        }

        if (!empty($data)) {
            return $data;
        }
        return [];
    }
    public function getColumnDefs()
    {
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }

        $columnDefs = $this->getColumnDefsFromApiResult();
        $columnDefs[] = array(
            'width' => '10%',
            'targets' => array(3, 4, 5)
        );
        $columnDefs[] = array(
            'width' => '5%',
            'targets' => array(4)
        );
        $columnDefs[] = array(
            'width' => '3%',
            'targets' => array(0, 2, 1, 6)
        );
        $columnDefs[] = array(
            'orderable' => false,
            'targets' => array(0, 6)
        );

        return $columnDefs;
    }

    public function getOrders($params)
    {

        $doData = [
            'doIds' => $params->get('doIds'),
            'status' => 52
        ];
        $request['json'] = $doData;
        $request['headers']['Content-Type'] = 'application/json';
        $res = $this->makeApiCall('PUT', 'DO/update/status', $request);

        return $res;
    }
}