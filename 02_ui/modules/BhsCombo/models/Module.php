<?php

class BhsCombo_Module_Model extends Vtiger_BhsModule_Model {
	public function __construct() {
		parent::__construct();
		$this->setFilters( [
			[
				'name'     => 'name',
				'datatype' => 'text',
			],
			[
				'name'     => 'price',
				'datatype' => 'text',
			],
			[
				'name'     => 'status',
				'datatype' => 'select',
				'data'     => $this->getStatusList(),
			],
		] );
	}

	public function getStatusList() {
		return [
			(object) [
				'name'  => 'ACTIVE',
				'id'    => '1',
				'type'  => 'Combo status',
				'value' => '1',
			],
			(object) [
				'name'  => 'INACTIVE',
				'id'    => '0',
				'type'  => 'Combo status',
				'value' => '0',
			],
		];
	}

	public function deleteRecordById( $id ) {
		$functions                          = 'products/' . $id;
		$request['headers']['Content-Type'] = 'application/json';
   
		return $this->makeApiCall( 'DELETE', $functions, $request );
	}

	public function getAllCombosActive() {
		$apiResult = $this->makeApiCall( 'GET', 'products?productType=2&status=1' );
		$rows      = $this->getContentsFromApiResult( $apiResult );

		return $rows;
	}

    public function getAllCombos($request)
    {
        try {
            $request2 = [];
            $form_params = $_REQUEST;
            $columns = $form_params['columns'];
            $dateFieldsFilter = ['stopdate', 'startdate', 'createdate', 'modifyDate', 'starttime', 'requestTime'];

			$params['columns']         = $columns;
			$params['draw']            = $request->get( 'draw' );
			$params['columns']         = $request->get( 'columns' );
			$params['order']           = $request->get( 'order' );
			$params['search']['value'] = $request->get( 'keyword' );
			$params['limit']           = $request->get( 'length' );
			$params['offset']          = $request->get( 'start' );
			$params['productType']     = 2;
			foreach ( $columns as $column ) {
				if ( isset( $column['search']['value'] ) && $column['search']['value'] != '' ) {
					if ( in_array( $column['name'], $dateFieldsFilter ) && ! empty( $column['search']['value'] ) ) {
						$dateValue                 = explode( '-', $column['search']['value'] );
						$startDate                 = date( 'YmdHis',
							strtotime( str_replace( '/', '-', $dateValue[0] ) ) );
						$endDate                   = date( 'YmdHis',
							strtotime( str_replace( '/', '-', $dateValue[1] ) ) );
						$dateValue                 = $startDate . '|' . $endDate;
						$params[ $column['name'] ] = $dateValue;
					} else {
						$params[ $column['name'] ] = $column['search']['value'];
					}
				}
			}

            $request2['query'] = $params;
            $apiResult = $this->makeApiCall('GET', 'products', $request2);
            $apiResult['body']->recordsTotal = $apiResult['body']->total;
            $apiResult['body']->recordsFiltered = $apiResult['body']->total;
            $this->getTotalFromApiResult($apiResult);
            return $this->getContentsFromApiResult($apiResult);
        } catch (Exception $exception) {
            return [];
        }
    }

    public function saveRecord($params)
    {
        $method = 'POST';
        if ($params['comboInfo']['prodId'] > 0) {
            $method = 'PUT';
        }
        $request = [];
        $request['json'] = $params;
        $request['headers']['Content-Type'] = 'application/json';
        $res = $this->makeApiCall($method, 'products/v2', $request);
    }

	public function getFirstRecord() {
		if ( ! $this->firstRecord['body']->headers ) {
			$cols                               = [
				'name',
				'price',
				'status',
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
        $columnDefs[] = ['data' => 'bhs_action', 'targets' => 3];
        $columnDefs[] = [
            'className' => 'text-right pr-4',
            'targets' => [1],
        ];
        $columnDefs[] = [
            'bSortable' => false,
            'targets' => [3]
        ];
        return $columnDefs;
    }

	public function getCurrencyWithOrgId() {
		$orgId = Vtiger_Session::get( 'agent_info' )->info->orgId;
		$orgId = $orgId ? $orgId : 1;
		$res   = $this->makeApiCall( 'GET', "synonym/currency?orgId=$orgId" );

		return $res['body']->data;
	}
}
