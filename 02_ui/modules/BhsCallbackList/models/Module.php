<?php

class BhsCallbackList_Module_Model extends Vtiger_BhsModule_Model {

    public function __construct(){
        parent::__construct();
        $this->setFilters(array(
            array(
                'name' => 'leadId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'callbackTime',
                'datatype' => 'daterange',
            ),
            array(
                'name' => 'requestTime',
                'datatype' => 'daterange',
            ),
            array(
                'name' => 'name',
                'datatype' => 'text',
            ),
            array(
                'name' => 'phone',
                'datatype' => 'text',
            ),
            array(
                'name' => 'owner',
                'datatype' => 'text',
            ),
            array(
                'name' => 'assignedName',
                'datatype' => 'text',
            ),
            array(
                'name' => 'createdate',
                'datatype' => 'daterange',
            )
        ));
    }

//    public function getFirstRecord(){
//        $request = $this->buildDummyRequest();
//        $res = $this->makeApiCall('POST', 'callback_list', $request);
//        $this->firstRecord = $res;
//    }

    public function getFirstRecord(){
        $res = $this->makeApiCall('GET', 'callback');
        $this->firstRecord = $res;
        if(! $this->firstRecord['body']->headers) {
            $cols = [
                'leadId',
                'callbackTime',
                'requestTime',
                'name',
                'phone',
                'owner',
                'assignedName',
                'createdate',
            ];
            $this->firstRecord['body']->headers = $cols;
        }
    }


    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult();
        $columnDefs[] = array('data' => 'bhs_action', 'targets' => 8);
        $columnDefs[] = array( 'orderable' => false, 'targets' => 8 );
        // $columnDefs[] = array(
        //     'className' => 'text-left',
        //     'targets' => array(5)
        // );
        return $columnDefs;
    }

    public function getHearderUnassign(){
        $headerCol =  [
            'leadId',
            'callbackTime',
            'requestTime',
            'name',
            'phone',
            'owner',
            'assignedName',
            'createdate',
            'takeAction',
            'bhs_action'
        ];

        $headersResult = $headerCol;
        $header = array();
        foreach ($headersResult as $item) {
            $label = 'LBL_' . $item;
            $datatype = $this->getDatatypeByColumnName($item); // datatype: number, text, date, currency...
            $data = $this->getData($item);
            $has_filter = 0;
            if (empty($datatype)) {
                $datatype = 'text';
            } else {
                $has_filter = 1;
            }
            $name = $item;
            $header[] = array('label' => strtoupper($label), 'datatype' => $datatype, 'name' => $name, 'has_filter' => $has_filter, 'datax' => $data);
        }
        return $header;
    }

    public function getColumnDefsUnassign() {
        $headerCols =  [
            'leadId',
            'callbackTime',
            'requestTime',
            'name',
            'phone',
            'owner',
            'assignedName',
            'createdate',
            'takeAction',
            'bhs_action'
        ];
        $filters = array(
            array(
                'name' => 'leadId',
                'datatype' => 'text',
            ),
            array(
                'name' => 'callbackTime',
                'datatype' => 'daterange',
            ),
            array(
                'name' => 'requestTime',
                'datatype' => 'daterange',
            ),
            array(
                'name' => 'name',
                'datatype' => 'text',
            ),
            array(
                'name' => 'phone',
                'datatype' => 'text',
            ),
            array(
                'name' => 'owner',
                'datatype' => 'text',
            ),
            array(
                'name' => 'assignedName',
                'datatype' => 'text',
            ),
            array(
                'name' => 'createdate',
                'datatype' => 'daterange',
            ),
        );
        $columnDefs = array();
        $idx = 0;
        $allowSearch = array();
        $notAllowSearch = array();
        foreach ($headerCols as $header) {
            $flag = false;
            foreach ($filters as $filterCol) {
                if ($filterCol['name'] == $header) {
                    $flag = true;
                    $allowSearch[] = $idx;
                    break;
                }
            }
            if (!$flag) {
                $notAllowSearch[] = $idx;
            }
            $columnDefs[] = array('name' => $header, 'targets' => $idx);
            $columnDefs[] = array('data' => $header, 'targets' => $idx);
            $idx += 1;
        }
        $columnDefs[] = array('searchable' => true, 'targets' => $allowSearch);
        $columnDefs[] = array('searchable' => false, 'targets' => $notAllowSearch);
        $columnDefs[] = array('data' => 'bhs_action', 'targets' => 9);
        $columnDefs[] = array( 'orderable' => false, 'targets' => 9 );
        $columnDefs[] = array( 'orderable' => false, 'targets' => 8 );
        return $columnDefs;
    }

    /**
     * Save campaign
     */
    public function updateTake($id) {

        $callback = $id ;
        $request['json'] = $callback;
        $request['headers']['Content-Type'] = 'application/json';
        $res = $this->makeApiCall('PUT', 'callback/assign', $request);
        return $res;
    }
    public function updateTime($data,$id) {
        $request['json'] = $data;
        $request['headers']['Content-Type'] = 'application/json';
        $res = $this->makeApiCall('PUT', 'callback/'.$id, $request);
        return $res['body'];
    }

}