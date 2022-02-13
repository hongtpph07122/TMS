<?php

/* ********************************************************************************
 * The content of this file is subject to the VTEQBO("License");
 * You may not use this file except in compliance with the License
 * The Initial Developer of the Original Code is VTExperts.com
 * Portions created by VTExperts.com. are Copyright(C) VTExperts.com.
 * All Rights Reserved.
 * ****************************************************************************** */

class BhsCdrs_ActionAjax_Action extends Vtiger_BhsBasicAjax_Action
{

    function __construct()
    {
        parent::__construct();
        $this->exposeMethod('deleteCrds');
    }

    function deleteCrds(Vtiger_Request $request)
    {
        $res = array('success' => true);
        try {
            // @Todo
        } catch (Exception $exception) {
            $res['success'] = false;
            $res['message'] = $exception->getMessage();
        }
        $response = new Vtiger_Response();
        $response->setResult($res);
        $response->emit();
    }

    function getList(Vtiger_Request $request)
    {
        $moduleModel = new BhsCdrs_Module_Model();
        $draw = intval($request->get('draw'));
        try {
            $data = array();
            $rows = $this->getRowsRefactor($request, 'lead/cdr', $moduleModel);
            foreach ($rows as $row) {
                if (!$row) {
                    continue;
                }
                $item = $row;
                $item['checkbox'] = '<input type="checkbox" data-id="' . $item['callId'] . '"class="check-item"/>';
                if (!empty($item['playbackUrl'])) {
                    $item['play'] = '<a class="btnPlay" href="javascript:void(0);" data-id="' . $item['callId'] .'" data-lead-name="'. $item['leadName'].'" data-lead-phone="'. $item['leadPhone']. ' "><i class="fa fa-play"></i></a>';
                    $item['download'] = '<a download href="'. $item['playbackUrl'] .'"><i class="fa fa-download"></i></a>';
                }
                $item['starttime'] = $row['starttime'] ? Date('d/m/Y H:i:s', strtotime($row['starttime'])) : "";
                $data[] = $item;
            }
            $res = array(
                'recordsTotal' => $moduleModel->getRecordTotal(),
                'recordsFiltered' => $moduleModel->getRecordsFiltered(),
                'data' => $data,
                'draw' => $draw,
            );
        } catch (Exception $exception) {
            $res = array(
                'error' => $exception->getMessage(),
                'recordsTotal' => 0,
                'recordsFiltered' => 0,
                'draw' => $draw,
                'data' => array(),
            );
        }
        $this->customEmit($res);
    }

    public function getRowsRefactor(Vtiger_Request $request, $function, BhsCdrs_Module_Model $moduleModel)
    {

        try {
            $request2 = array();
            //$request2['is_gridview'] = true;
            $form_params = $_REQUEST;
            $columns = $form_params['columns'];
            $dateFieldsFilter = ['stopdate', 'startdate', 'createdate', 'modifyDate', 'starttime', 'requestTime'];

            $form_params['columns'] = $columns;
            $form_params['draw'] = $request->get('draw');
            $form_params['columns'] = $request->get('columns');
            $form_params['order'] = $request->get('order');
            $form_params['search']['value'] = $request->get('keyword');
            $form_params['limit'] = $request->get('length');
            $form_params['offset'] = $request->get('start');
            foreach ($columns as $column) {
                if (isset($column['search']['value']) && $column['search']['value'] != '') {
                    if (in_array($column['name'], $dateFieldsFilter) && !empty($column['search']['value'])) {
                        $dateValue = explode('-', $column['search']['value']);
                        $startDate = date('YmdHis', strtotime(str_replace('/', '-', $dateValue[0])));
                        $endDate = date('YmdHis', strtotime(str_replace('/', '-', $dateValue[1])));
                        $dateValue = $startDate . '|' . $endDate;
                        $form_params[$column['name']] = $dateValue;
                    } elseif ($column['name'] == 'callStatusName') {
                        // @TO DO doi cung cap api thi se search theo call status
                        $form_params['callStatusName']= $column['search']['value'];
                    }else {
                        $form_params[$column['name']] = $column['search']['value'];
                    }

                }
            }
            $request2['query'] = $form_params;
            $apiResult = $moduleModel->makeApiCall('GET', $function, $request2);
            $apiResult['body']->recordsTotal = $apiResult['body']->total;
            $apiResult['body']->recordsFiltered = $apiResult['body']->total;
            $moduleModel->getTotalFromApiResult($apiResult);
            return $moduleModel->getContentsFromApiResult($apiResult);
        } catch (Exception $exception) {
            return array();
        }
    }
}
