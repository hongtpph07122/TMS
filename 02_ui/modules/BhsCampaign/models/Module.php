<?php

class BhsCampaign_Module_Model extends Vtiger_BhsModule_Model {

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
        ));
    }

    public function getFirstRecord(){
        if(! $this->firstRecord['body']->headers) {
            $cols = ['cpName', 'startDate',
            'endDate',
            'campaignStatus',
            'actual',
            'lead', "totalCall",
            "totalAns"];
            $this->firstRecord['body']->headers = $cols;
        }
    }

    public function getColumnDefs(){
        if (empty($this->firstRecord)) {
            $this->getFirstRecord();
        }
        $columnDefs = $this->getColumnDefsFromApiResult();
        $columnDefs[] = array('data' => 'bhs_action', 'targets' => 8);
        $columnDefs[] = array(
            'bSortable' => false,
            'targets' => array(9)
        );
        return $columnDefs;
    }
}